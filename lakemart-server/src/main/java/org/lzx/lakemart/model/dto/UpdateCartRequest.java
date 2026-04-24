package org.lzx.lakemart.model.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class UpdateCartRequest {
    @NotNull(message = "购物车项ID不能为空")
    private Long cartItemId;

    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private Integer quantity;
}