package org.lzx.lakemart.service.impl.admin;

import org.lzx.lakemart.mapper.StatisticsMapper;
import org.lzx.lakemart.service.admin.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;   // 新增注入

    @Override
    public List<Map<String, Object>> getBehaviorTrend(int minutes) {
        // 从 behavior_trend 表查询最近 minutes 分钟的数据
        String sql = "SELECT minute, cnt FROM behavior_trend WHERE minute >= NOW() - INTERVAL ? MINUTE ORDER BY minute";
        return jdbcTemplate.queryForList(sql, minutes);
    }

    @Override
    public List<Map<String, Object>> getActionDistribution() {
        // 从 action_distribution 表查询
        String sql = "SELECT action, cnt FROM action_distribution";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public Map<String, Object> getOverview() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalOrders", statisticsMapper.getTotalOrders());
        data.put("totalSalesAmount", statisticsMapper.getTotalSalesAmount());
        data.put("totalUsers", statisticsMapper.getTotalUsers());
        data.put("hotProductCount", statisticsMapper.getHotProductCount());
        return data;
    }
}