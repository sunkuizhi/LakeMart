package org.lzx.lakemart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int DEFAULT_LIMIT = 12;

    /**
     * 为用户生成个性化推荐
     * @param userId 用户ID
     * @param limit 推荐数量
     * @return 商品ID列表
     */
    public List<Long> recommendForUser(Long userId, int limit) {
        // 1. 获取用户最近交互过的商品（最近30天内，按时间倒序，取前10个）
        String userProductsSql = "SELECT DISTINCT product_id FROM user_behavior_log " +
                "WHERE user_id = ? AND product_id IS NOT NULL " +
                "AND create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                "ORDER BY create_time DESC LIMIT 10";
        List<Long> userProducts = jdbcTemplate.queryForList(userProductsSql, Long.class, userId);

        // 2. 如果没有交互记录，返回热销商品
        if (userProducts.isEmpty()) {
            return getHotProducts(limit);
        }

        // 3. 从相似度表中获取相似商品（每个交互商品取 top 相似商品）
        String inSql = String.join(",", Collections.nCopies(userProducts.size(), "?"));
        String similarSql = "SELECT similar_product_id, score FROM tb_item_similarity " +
                "WHERE product_id IN (" + inSql + ") " +
                "ORDER BY score DESC LIMIT ?";
        List<Object> params = new ArrayList<>(userProducts);
        params.add(limit * 2); // 多取一些，用于去重和过滤
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(similarSql, params.toArray());

        // 4. 去重并排除用户已交互过的商品
        Set<Long> excludeSet = new HashSet<>(userProducts);
        List<Long> recommendations = rows.stream()
                .map(row -> ((Number) row.get("similar_product_id")).longValue())
                .filter(id -> !excludeSet.contains(id))
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());

        // 5. 如果推荐数量不足，用热销商品补全
        if (recommendations.size() < limit) {
            List<Long> hot = getHotProducts(limit - recommendations.size());
            recommendations.addAll(hot);
        }
        return recommendations;
    }
    // public List<Long> recommendForUser(Long userId, int limit) {
    //     // 临时：直接返回热销商品，确保能看到推荐区块
    //     return getHotProducts(limit);
    // }

    /**
     * 获取热销商品（从 hot_products 表）
     */
    private List<Long> getHotProducts(int limit) {
        String sql = "SELECT product_id FROM hot_products ORDER BY cnt DESC LIMIT ?";
        return jdbcTemplate.queryForList(sql, Long.class, limit);
    }
}