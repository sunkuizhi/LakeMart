package org.lzx.lakemart.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticsMapper {

    /**
     * 查询热销商品 TOP N
     * @param limit 数量
     * @return product_id, cnt
     */
    List<Map<String, Object>> getHotProducts(@Param("limit") int limit);

    /**
     * 获取总订单数（已支付、已发货、已完成）
     * @return 订单数
     */
    Integer getTotalOrders();

    /**
     * 获取总销售额
     * @return 销售额
     */
    BigDecimal getTotalSalesAmount();

    /**
     * 获取总用户数
     * @return 用户数
     */
    Long getTotalUsers();

    /**
     * 获取热销商品表中记录数
     * @return 记录数
     */
    Integer getHotProductCount();

    // 以下两个方法可后续扩展，用于行为趋势和分布
    // List<Map<String, Object>> getBehaviorTrend(@Param("minutes") int minutes);
    // List<Map<String, Object>> getActionDistribution();
}