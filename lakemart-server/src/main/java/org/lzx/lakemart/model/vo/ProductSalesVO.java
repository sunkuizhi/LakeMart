// ProductSalesVO.java
package org.lzx.lakemart.model.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class ProductSalesVO {
    private Long productId;
    private String productName;   // 新增
    private Integer totalQuantity;
    private BigDecimal totalAmount; // 如果有的话
}