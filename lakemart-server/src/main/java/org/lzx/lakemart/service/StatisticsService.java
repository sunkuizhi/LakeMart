package org.lzx.lakemart.service;

import java.util.List;
import java.util.Map;

public interface StatisticsService {

    /**
     * 获取实时行为趋势（最近N分钟，每分钟行为次数）
     * @param minutes 过去多少分钟
     * @return 包含 minute, cnt 的列表
     */
    List<Map<String, Object>> getBehaviorTrend(int minutes);

    /**
     * 获取用户行为分布（浏览、加购、下单等占比）
     * @return 包含 action, cnt 的列表
     */
    List<Map<String, Object>> getActionDistribution();

    /**
     * 获取概览卡片数据
     * @return 包含 totalOrders, totalSalesAmount, totalUsers, hotProductCount 的 Map
     */
    Map<String, Object> getOverview();
}