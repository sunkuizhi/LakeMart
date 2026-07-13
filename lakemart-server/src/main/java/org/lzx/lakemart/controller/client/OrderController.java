package org.lzx.lakemart.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.lzx.lakemart.model.dto.CancelOrderRequest;
import org.lzx.lakemart.model.dto.CreateOrderRequest;
import org.lzx.lakemart.model.dto.OrderStatisticsDTO;
import org.lzx.lakemart.model.dto.PayOrderRequest;
import org.lzx.lakemart.model.vo.OrderVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.security.SecurityUser;
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
    public Result<OrderVO> createOrder(@AuthenticationPrincipal SecurityUser securityUser,
                                       @Valid @RequestBody CreateOrderRequest request) {
        Long userId = securityUser.getId();
        OrderVO order = orderService.createOrder(userId, request.getCartItemIds(), request.getAddressId());
        return Result.success(order);
    }

    /**
     * 获取当前用户的订单列表
     * 注意：@RequestParam 需要指定 value 属性，否则编译后参数名会丢失
     */
    @GetMapping("/list")
    public Result<Page<OrderVO>> getOrderList(@AuthenticationPrincipal SecurityUser securityUser,
                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Long userId = securityUser.getId();
        Page<OrderVO> page = orderService.getUserOrdersPage(userId, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/detail/{orderId}")
    public Result<OrderVO> getOrderDetail(@AuthenticationPrincipal SecurityUser securityUser,
                                          @PathVariable("orderId") Long orderId) {
        Long userId = securityUser.getId();
        OrderVO order = orderService.getOrderDetail(orderId, userId);
        return Result.success(order);
    }

    /**
     * 模拟支付订单
     */
    @PostMapping("/pay")
    public Result<String> payOrder(@AuthenticationPrincipal SecurityUser securityUser,
                                   @Valid @RequestBody PayOrderRequest request) {
        Long userId = securityUser.getId();
        orderService.payOrder(request.getOrderId(), userId);
        return Result.success("支付成功");
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    public Result<String> cancelOrder(@AuthenticationPrincipal SecurityUser securityUser,
                                      @Valid @RequestBody CancelOrderRequest request) {
        Long userId = securityUser.getId();
        orderService.cancelOrder(request.getOrderId(), userId);
        return Result.success("订单已取消");
    }

    /**
     * 管理员获取每日订单统计（最近一周默认）
     */
    @GetMapping("/statistics/daily")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<OrderStatisticsDTO>> getDailyStatistics(
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate) {
        if (startDate == null) startDate = LocalDate.now().minusDays(7);
        if (endDate == null) endDate = LocalDate.now().minusDays(1);
        List<OrderStatisticsDTO> list = orderService.getDailyStatistics(startDate, endDate);
        return Result.success(list);
    }

    /**
     * 确认收货
     */
    @PostMapping("/confirm/{orderId}")
    public Result<String> confirmReceipt(@AuthenticationPrincipal SecurityUser securityUser,
                                         @PathVariable("orderId") Long orderId) {
        Long userId = securityUser.getId();
        orderService.confirmReceipt(orderId, userId);
        return Result.success("确认收货成功");
    }
}