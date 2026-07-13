package org.lzx.lakemart.service.impl.client;


import org.lzx.lakemart.model.vo.RecommendProductVO;
import org.lzx.lakemart.service.client.IRecommendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl implements IRecommendService {

    private static final Logger log = LoggerFactory.getLogger(RecommendServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int DEFAULT_LIMIT = 12;

    @Override
    public List<RecommendProductVO> recommendForUser(Long userId, int limit) {
        log.info("开始为用户 {} 推荐商品，limit={}", userId, limit);

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
     * 填充商品详细信息（名称、价格、图片、销量）
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
                        .price((java.math.BigDecimal) product.get("price"))
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
                // 避免重复
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

    // ========== 辅助方法 ==========

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

    private List<Long> getUserRecentProducts(Long userId) {
        String sql = "SELECT DISTINCT product_id FROM user_behavior_log " +
                "WHERE user_id = ? AND product_id IS NOT NULL " +
                "AND create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                "ORDER BY create_time DESC LIMIT 10";
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