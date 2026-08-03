package org.lzx.lakemart.service.impl.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lzx.lakemart.model.vo.RecommendProductVO;
import org.lzx.lakemart.service.ProductService;
import org.lzx.lakemart.service.client.IRecommendService;
import org.lzx.lakemart.service.common.ABTestManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 推荐服务实现（优先从 Redis 读取离线预计算推荐列表）
 * 依赖独立的 ABTestManager 组件进行分流控制
 */
@Service
public class RecommendServiceImpl implements IRecommendService {

    private static final Logger log = LoggerFactory.getLogger(RecommendServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductService productService; // 保留以备后用

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ABTestManager abTestManager;   // 注入独立组件（不再依赖内部类）

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<RecommendProductVO> recommendForUser(Long userId, int limit) {
        log.info("开始为用户 {} 推荐商品，limit={}", userId, limit);

        // 分流决策：由 ABTestManager 判断是否进入实验组
        boolean useALS = abTestManager.isInExperimentGroup(userId);

        if (useALS) {
            // 实验组：优先从 Redis 读取 ALS 推荐
            List<RecommendProductVO> alsResult = getRecommendFromRedis(userId, limit);
            if (alsResult != null && !alsResult.isEmpty()) {
                log.info("用户 {} 命中 ALS 推荐缓存", userId);
                return alsResult;
            } else {
                log.info("用户 {} 实验组但未命中缓存，降级到普通推荐", userId);
            }
        } else {
            log.info("用户 {} 在对照组，使用普通推荐", userId);
        }

        // 对照组 或 实验组缓存未命中，走降级策略
        return fallbackRecommend(userId, limit);
    }

    /**
     * 从 Redis 获取推荐列表，如果无数据或数据不足则返回 null
     */
    private List<RecommendProductVO> getRecommendFromRedis(Long userId, int limit) {
        String redisKey = "recommend:user:" + userId;
        String json = redisTemplate.opsForValue().get(redisKey);
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            // 解析 JSON 数组（商品 ID 列表）
            List<Long> productIds = objectMapper.readValue(json, new TypeReference<List<Long>>() {});
            if (productIds == null || productIds.isEmpty()) {
                return null;
            }

            // 取前 limit 个（如果 Redis 存储超过 limit）
            List<Long> topIds = productIds.stream().limit(limit).collect(Collectors.toList());

            // 填充商品详情（复用已有方法，并指定推荐理由）
            return fillProductDetailsWithReason(topIds, "为你精选");
        } catch (Exception e) {
            log.warn("解析 Redis 推荐数据失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 降级推荐：采用原策略（品类偏好 → 相似商品 → 热销兜底）
     */
    private List<RecommendProductVO> fallbackRecommend(Long userId, int limit) {
        List<RecommendProductVO> results = new ArrayList<>();

        // 1. 基于偏好品类推荐
        List<Long> categoryIds = getUserPreferCategories(userId);
        if (!categoryIds.isEmpty()) {
            String categoryName = getCategoryName(categoryIds.get(0));
            List<Long> products = getProductsByCategories(categoryIds, limit);
            for (Long productId : products) {
                results.add(RecommendProductVO.builder()
                        .productId(productId)
                        .reason("基于你的偏好品类：" + categoryName)
                        .build());
            }
            if (results.size() >= limit) {
                return fillProductDetails(results, limit);
            }
        }

        // 2. 基于相似商品推荐
        List<Long> userProducts = getUserRecentProducts(userId);
        if (!userProducts.isEmpty()) {
            String productName = getProductName(userProducts.get(0));
            List<Long> similar = getSimilarProducts(userProducts, limit);
            for (Long productId : similar) {
                results.add(RecommendProductVO.builder()
                        .productId(productId)
                        .reason("因为你浏览过 " + productName)
                        .build());
            }
            if (results.size() >= limit) {
                return fillProductDetails(results, limit);
            }
        }

        // 3. 热销兜底
        List<Long> hot = getHotProducts(limit);
        for (Long productId : hot) {
            results.add(RecommendProductVO.builder()
                    .productId(productId)
                    .reason("大家都在买 🔥")
                    .build());
        }

        return fillProductDetails(results, limit);
    }

    /**
     * 根据商品 ID 列表填充详情（统一推荐理由）
     */
    private List<RecommendProductVO> fillProductDetailsWithReason(List<Long> productIds, String reason) {
        if (productIds == null || productIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 批量查询商品详情
        String inSql = String.join(",", Collections.nCopies(productIds.size(), "?"));
        String sql = "SELECT id, name, price, image_url, sales_count FROM tb_product WHERE id IN (" + inSql + ")";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, productIds.toArray());

        Map<Long, Map<String, Object>> productMap = rows.stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row.get("id")).longValue(),
                        row -> row
                ));

        List<RecommendProductVO> filled = new ArrayList<>();
        for (Long productId : productIds) {
            Map<String, Object> product = productMap.get(productId);
            if (product != null) {
                filled.add(RecommendProductVO.builder()
                        .productId(productId)
                        .productName((String) product.get("name"))
                        .price((BigDecimal) product.get("price"))
                        .imageUrl((String) product.get("image_url"))
                        .salesCount(((Number) product.get("sales_count")).intValue())
                        .reason(reason)
                        .build());
            }
        }
        return filled;
    }

    /**
     * 填充商品详细信息（名称、价格、图片、销量）——用于降级方案
     */
    private List<RecommendProductVO> fillProductDetails(List<RecommendProductVO> results, int limit) {
        if (results.isEmpty()) return Collections.emptyList();

        List<Long> productIds = results.stream()
                .map(RecommendProductVO::getProductId)
                .collect(Collectors.toList());

        String inSql = String.join(",", Collections.nCopies(productIds.size(), "?"));
        String sql = "SELECT id, name, price, image_url, sales_count FROM tb_product WHERE id IN (" + inSql + ")";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, productIds.toArray());

        Map<Long, Map<String, Object>> productMap = rows.stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row.get("id")).longValue(),
                        row -> row
                ));

        List<RecommendProductVO> filled = new ArrayList<>();
        for (RecommendProductVO item : results) {
            Map<String, Object> product = productMap.get(item.getProductId());
            if (product != null) {
                filled.add(RecommendProductVO.builder()
                        .productId(item.getProductId())
                        .productName((String) product.get("name"))
                        .price((BigDecimal) product.get("price"))
                        .imageUrl((String) product.get("image_url"))
                        .salesCount(((Number) product.get("sales_count")).intValue())
                        .reason(item.getReason())
                        .build());
            }
        }
        // 如果推荐数量不足，用热销商品补全
        if (filled.size() < limit) {
            List<Long> hot = getHotProducts(limit - filled.size());
            for (Long productId : hot) {
                boolean exists = filled.stream().anyMatch(p -> p.getProductId().equals(productId));
                if (!exists) {
                    filled.add(RecommendProductVO.builder()
                            .productId(productId)
                            .reason("大家都在买 🔥")
                            .build());
                }
            }
            // 递归调用自己再补全一次（填充详情）
            return fillProductDetails(filled, limit);
        }

        return filled.stream().limit(limit).collect(Collectors.toList());
    }

    // ========== 以下辅助方法保持不变 ==========

    private List<Long> getUserPreferCategories(Long userId) {
        String sql = "SELECT prefer_category_1, prefer_category_2, prefer_category_3 FROM user_profile WHERE user_id = ?";
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, userId);
            List<Long> categories = new ArrayList<>();
            addIfNotNull(result.get("prefer_category_1"), categories);
            addIfNotNull(result.get("prefer_category_2"), categories);
            addIfNotNull(result.get("prefer_category_3"), categories);
            return categories;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private void addIfNotNull(Object obj, List<Long> list) {
        if (obj != null) {
            try {
                list.add(Long.valueOf(obj.toString()));
            } catch (NumberFormatException ignored) {}
        }
    }

    private String getCategoryName(Long categoryId) {
        try {
            return jdbcTemplate.queryForObject("SELECT name FROM tb_category WHERE id = ?", String.class, categoryId);
        } catch (Exception e) {
            return "你喜欢的分类";
        }
    }

    private String getProductName(Long productId) {
        try {
            return jdbcTemplate.queryForObject("SELECT name FROM tb_product WHERE id = ?", String.class, productId);
        } catch (Exception e) {
            return "相关商品";
        }
    }

    private List<Long> getProductsByCategories(List<Long> categoryIds, int limit) {
        String inSql = String.join(",", Collections.nCopies(categoryIds.size(), "?"));
        String sql = "SELECT id FROM tb_product WHERE status = 1 AND category_id IN (" + inSql + ") ORDER BY sales_count DESC LIMIT ?";
        List<Object> params = new ArrayList<>(categoryIds);
        params.add(limit * 2);
        return jdbcTemplate.queryForList(sql, Long.class, params.toArray());
    }

    /**
     * 获取用户最近30天内浏览过的商品ID（去重，按最后浏览时间倒序）
     */
    private List<Long> getUserRecentProducts(Long userId) {
        String sql = "SELECT product_id FROM user_behavior_log " +
                "WHERE user_id = ? AND product_id IS NOT NULL " +
                "AND create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                "GROUP BY product_id ORDER BY MAX(create_time) DESC LIMIT 10";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    private List<Long> getSimilarProducts(List<Long> userProducts, int limit) {
        String inSql = String.join(",", Collections.nCopies(userProducts.size(), "?"));
        String sql = "SELECT similar_product_id FROM tb_item_similarity " +
                "WHERE product_id IN (" + inSql + ") " +
                "ORDER BY score DESC LIMIT ?";
        List<Object> params = new ArrayList<>(userProducts);
        params.add(limit * 2);
        List<Long> results = jdbcTemplate.queryForList(sql, Long.class, params.toArray());
        return results.stream().distinct().limit(limit).collect(Collectors.toList());
    }

    private List<Long> getHotProducts(int limit) {
        String sql = "SELECT product_id FROM hot_products ORDER BY cnt DESC LIMIT ?";
        try {
            return jdbcTemplate.queryForList(sql, Long.class, limit);
        } catch (Exception e) {
            String fallbackSql = "SELECT product_id FROM tb_order_item GROUP BY product_id ORDER BY SUM(quantity) DESC LIMIT ?";
            return jdbcTemplate.queryForList(fallbackSql, Long.class, limit);
        }
    }
}