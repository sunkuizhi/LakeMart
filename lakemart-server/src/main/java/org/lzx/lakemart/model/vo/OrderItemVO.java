package org.lzx.lakemart.model.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
// 订单项视图对象
@Data
@Builder
public class OrderItemVO {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}