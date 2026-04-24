package org.lzx.lakemart.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    @NotNull(message = "状态不能为空")
    private Integer status;  // 2:已发货, 3:已完成, 4:已取消
}