package org.lzx.lakemart.model.vo;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class ProductSalesVO {
    private Long productId;
    private String productName;
    private Integer totalQuantity;
    private BigDecimal totalAmount;  // 必须是 BigDecimal
    // getters/setters
}