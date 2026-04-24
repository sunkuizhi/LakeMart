package org.lzx.lakemart.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductAddRequest {
    @NotBlank(message = "商品名称不能为空")
    private String name;
    private String description;
    @NotNull(message = "价格不能为空")
    private BigDecimal price;
    @NotNull(message = "库存不能为空")
    private Integer stock;
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    private String imageUrl;
}