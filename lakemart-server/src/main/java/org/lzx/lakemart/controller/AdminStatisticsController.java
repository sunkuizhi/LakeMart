package org.lzx.lakemart.controller;

import org.lzx.lakemart.model.dto.OrderStatisticsDTO;
import org.lzx.lakemart.model.vo.DailyAmountVO;
import org.lzx.lakemart.model.vo.ProductSalesVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/statistics")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatisticsController {

    @Autowired
    private OrderService orderService;

    /**
     * 获取每日订单统计（订单数、销售额、商品销量）
     * @param startDate 开始日期（可选，默认7天前）
     * @param endDate   结束日期（可选，默认今天）
     * @return 统计列表
     */
    @GetMapping("/order/daily")
    public Result<List<OrderStatisticsDTO>> getDailyOrderStatistics(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<OrderStatisticsDTO> statistics = orderService.getDailyStatistics(startDate, endDate);
        return Result.success(statistics);
    }
    /**
     * 热销商品排行
     * @param limit 数量，默认10
     */
    @GetMapping("/hot-products")
    public Result<List<ProductSalesVO>> getHotProducts(@RequestParam(name = "limit", defaultValue = "10") int limit) {
        List<ProductSalesVO> list = orderService.getHotProducts(limit);
        return Result.success(list);
    }

    /**
     * 近7日销售额趋势
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     */
    @GetMapping("/sales-trend")
    public Result<List<DailyAmountVO>> getSalesTrend(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyAmountVO> list = orderService.getDailySalesAmount(startDate, endDate);
        return Result.success(list);
    }

}