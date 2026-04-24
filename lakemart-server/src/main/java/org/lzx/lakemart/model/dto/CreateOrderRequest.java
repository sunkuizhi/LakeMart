package org.lzx.lakemart.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;
// 下单请求（从购物车选中）
@Data
public class CreateOrderRequest {
    @NotNull(message = "购物车项ID列表不能为空")
    private List<Long> cartItemIds;  // 选中的购物车项ID列表
    @NotNull(message = "收货地址ID不能为空")
    private Long addressId;
}