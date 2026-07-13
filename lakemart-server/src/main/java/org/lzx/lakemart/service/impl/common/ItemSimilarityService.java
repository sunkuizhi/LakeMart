package org.lzx.lakemart.service.impl.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemSimilarityService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 行为权重配置（可根据业务调整）
    private static final double WEIGHT_VIEW = 0.5;
    private static final double WEIGHT_CART = 1.5;
    private static final double WEIGHT_BUY = 3.0;

    /**
     * 每天凌晨2点执行相似度计算
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void computeItemSimilarity() {
        log.info("开始计算商品相似度...");
        long start = System.currentTimeMillis();

        // 1. 获取所有有行为的商品ID（最近30天）
        String activeProductsSql = "SELECT DISTINCT product_id FROM user_behavior_log " +
                "WHERE create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) AND product_id IS NOT NULL";
        List<Long> allProducts = jdbcTemplate.queryForList(activeProductsSql, Long.class);

        // 如果商品过多，只取热门商品 top 500（性能优化）
        if (allProducts.size() > 500) {
            String hotProductsSql = "SELECT product_id FROM user_behavior_log " +
                    "WHERE create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) AND product_id IS NOT NULL " +
                    "GROUP BY product_id ORDER BY COUNT(*) DESC LIMIT 500";
            allProducts = jdbcTemplate.queryForList(hotProductsSql, Long.class);
        }
        log.info("参与计算的商品数量: {}", allProducts.size());
        if (allProducts.size() < 2) {
            log.warn("商品数量不足，跳过相似度计算");
            return;
        }

        // 2. 构建 用户-商品 偏好矩阵 (userId -> (productId -> score))
        String behaviorSql = "SELECT user_id, product_id, action FROM user_behavior_log " +
                "WHERE create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) AND product_id IS NOT NULL";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(behaviorSql);

        Map<Long, Map<Long, Double>> userProductScore = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Long userId = ((Number) row.get("user_id")).longValue();
            Long productId = ((Number) row.get("product_id")).longValue();
            String action = (String) row.get("action");
            double weight = 0;
            if (action.contains("VIEW")) weight = WEIGHT_VIEW;
            else if (action.contains("ADD_CART")) weight = WEIGHT_CART;
            else if (action.contains("BUY")) weight = WEIGHT_BUY;
            if (weight == 0) continue;
            userProductScore.computeIfAbsent(userId, k -> new HashMap<>())
                    .merge(productId, weight, Double::sum);
        }

        // 3. 构建 商品-用户 向量 (productId -> (userId -> score))
        Map<Long, Map<Long, Double>> productUserScore = new HashMap<>();
        for (Map.Entry<Long, Map<Long, Double>> entry : userProductScore.entrySet()) {
            Long userId = entry.getKey();
            for (Map.Entry<Long, Double> productEntry : entry.getValue().entrySet()) {
                Long productId = productEntry.getKey();
                Double score = productEntry.getValue();
                productUserScore.computeIfAbsent(productId, k -> new HashMap<>())
                        .put(userId, score);
            }
        }

        // 4. 计算余弦相似度，保留 top 20 相似商品
        List<Long> productList = new ArrayList<>(productUserScore.keySet());
        Map<Long, List<SimilarItem>> similarityMap = new HashMap<>();

        for (int i = 0; i < productList.size(); i++) {
            Long p1 = productList.get(i);
            Map<Long, Double> vec1 = productUserScore.get(p1);
            double norm1 = norm(vec1);
            if (norm1 == 0) continue;
            for (int j = i + 1; j < productList.size(); j++) {
                Long p2 = productList.get(j);
                Map<Long, Double> vec2 = productUserScore.get(p2);
                double norm2 = norm(vec2);
                if (norm2 == 0) continue;
                double dot = dotProduct(vec1, vec2);
                double similarity = dot / (norm1 * norm2);
                if (similarity > 0.05) {
                    similarityMap.computeIfAbsent(p1, k -> new ArrayList<>()).add(new SimilarItem(p2, similarity));
                    similarityMap.computeIfAbsent(p2, k -> new ArrayList<>()).add(new SimilarItem(p1, similarity));
                }
            }
        }

        // 5. 排序并截取 top 20，存入数据库
        List<Object[]> batchArgs = new ArrayList<>();
        for (Map.Entry<Long, List<SimilarItem>> entry : similarityMap.entrySet()) {
            Long productId = entry.getKey();
            List<SimilarItem> sims = entry.getValue();
            sims.sort((a, b) -> Double.compare(b.score, a.score));
            int limit = Math.min(20, sims.size());
            for (int i = 0; i < limit; i++) {
                SimilarItem sim = sims.get(i);
                batchArgs.add(new Object[]{productId, sim.productId, sim.score});
            }
        }

        // 清空旧数据并批量插入
        jdbcTemplate.update("TRUNCATE TABLE tb_item_similarity");
        String insertSql = "INSERT INTO tb_item_similarity (product_id, similar_product_id, score) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(insertSql, batchArgs);

        log.info("商品相似度计算完成，共 {} 条记录，耗时 {} ms", batchArgs.size(), System.currentTimeMillis() - start);
    }

    private double norm(Map<Long, Double> vector) {
        double sum = 0;
        for (double v : vector.values()) {
            sum += v * v;
        }
        return Math.sqrt(sum);
    }

    private double dotProduct(Map<Long, Double> v1, Map<Long, Double> v2) {
        double sum = 0;
        for (Map.Entry<Long, Double> entry : v1.entrySet()) {
            Long userId = entry.getKey();
            Double val2 = v2.get(userId);
            if (val2 != null) {
                sum += entry.getValue() * val2;
            }
        }
        return sum;
    }

    private static class SimilarItem {
        Long productId;
        double score;
        SimilarItem(Long productId, double score) {
            this.productId = productId;
            this.score = score;
        }
    }
}