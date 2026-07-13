package org.lzx.lakemart.model.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 推荐商品视图对象（含推荐理由）
 */
@Data
@Builder
public class RecommendProductVO {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private String imageUrl;
    private Integer salesCount;
    private String reason;  // 推荐理由
}