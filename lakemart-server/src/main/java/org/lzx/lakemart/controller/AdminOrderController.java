package org.lzx.lakemart.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.lzx.lakemart.model.dto.OrderPageQueryDTO;
import org.lzx.lakemart.model.dto.UpdateOrderStatusRequest;
import org.lzx.lakemart.model.vo.OrderVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/order")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/list")
    public Result<Page<OrderVO>> listOrders(@RequestBody OrderPageQueryDTO query) {
        Page<OrderVO> page = orderService.adminQueryOrders(query);
        return Result.success(page);
    }

    @PutMapping("/status")
    public Result<String> updateOrderStatus(@Valid @RequestBody UpdateOrderStatusRequest request) {
        orderService.adminUpdateOrderStatus(request.getOrderId(), request.getStatus());
        return Result.success("订单状态更新成功");
    }

    @GetMapping("/detail/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<OrderVO> getOrderDetail(@PathVariable("orderId") Long orderId) {
        OrderVO order = orderService.getOrderDetailForAdmin(orderId);
        return Result.success(order);
    }
}