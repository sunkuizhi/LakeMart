package org.lzx.lakemart.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
// 支付请求（模拟）
@Data
public class PayOrderRequest {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
}