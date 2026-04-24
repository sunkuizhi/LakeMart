package org.lzx.lakemart.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.lzx.lakemart.model.dto.CancelOrderRequest;
import org.lzx.lakemart.model.dto.CreateOrderRequest;
import org.lzx.lakemart.model.dto.OrderStatisticsDTO;
import org.lzx.lakemart.model.dto.PayOrderRequest;
import org.lzx.lakemart.model.vo.OrderVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单（从购物车选中商品下单）
     */
    @PostMapping("/create")
    public Result<OrderVO> createOrder(@AuthenticationPrincipal Long userId,
                                       @Valid @RequestBody CreateOrderRequest request) {
        OrderVO order = orderService.createOrder(userId, request.getCartItemIds(), request.getAddressId());
        return Result.success(order);
    }

    /**
     * 获取当前用户的订单列表
     */
    @GetMapping("/list")
    public Result<Page<OrderVO>> getOrderList(@AuthenticationPrincipal Long userId,
                                              @RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        Page<OrderVO> page = orderService.getUserOrdersPage(userId, pageNum, pageSize);
        return Result.success(page);
    }
    /**
     * 获取订单详情
     */
    @GetMapping("/detail/{orderId}")
    public Result<OrderVO> getOrderDetail(@AuthenticationPrincipal Long userId,
                                          @PathVariable Long orderId) {
        OrderVO order = orderService.getOrderDetail(orderId, userId);
        return Result.success(order);
    }
    /**
     * 模拟支付订单
     */
    @PostMapping("/pay")
    public Result<String> payOrder(@AuthenticationPrincipal Long userId,
                                   @Valid @RequestBody PayOrderRequest request) {
        orderService.payOrder(request.getOrderId(), userId);
        return Result.success("支付成功");
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    public Result<String> cancelOrder(@AuthenticationPrincipal Long userId,
                                      @Valid @RequestBody CancelOrderRequest request) {
        orderService.cancelOrder(request.getOrderId(), userId);
        return Result.success("订单已取消");
    }
    @GetMapping("/statistics/daily")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<OrderStatisticsDTO>> getDailyStatistics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        if (startDate == null) startDate = LocalDate.now().minusDays(7);
        if (endDate == null) endDate = LocalDate.now().minusDays(1);
        List<OrderStatisticsDTO> list = orderService.getDailyStatistics(startDate, endDate);
        return Result.success(list);
    }

    @PostMapping("/confirm/{orderId}")
    public Result<String> confirmReceipt(@AuthenticationPrincipal Long userId,
                                         @PathVariable("orderId") Long orderId) {
        orderService.confirmReceipt(orderId, userId);
        return Result.success("确认收货成功");
    }

}