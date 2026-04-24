package org.lzx.lakemart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lzx.lakemart.model.dto.OrderStatisticsDTO;
import org.lzx.lakemart.model.entity.Order;
import org.lzx.lakemart.model.vo.DailyAmountVO;
import org.lzx.lakemart.model.vo.ProductSalesVO;

import java.time.LocalDate;
import java.util.List;


/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    List<OrderStatisticsDTO> getDailyStatistics(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    // 热销商品排行（按销量）
    List<ProductSalesVO> getHotProducts(@Param("limit") int limit);

    // 近7日销售额趋势
    List<DailyAmountVO> getDailySalesAmount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
