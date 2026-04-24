package org.lzx.lakemart.model.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class CartItemVO {
    private Long cartId;           // 购物车项ID
    private Long productId;        // 商品ID
    private String productName;    // 商品名称
    private String productImage;   // 商品主图
    private BigDecimal price;      // 商品单价
    private Integer quantity;      // 数量
    private BigDecimal subtotal;   // 小计 = price * quantity
}