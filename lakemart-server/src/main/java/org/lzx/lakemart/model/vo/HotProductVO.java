package org.lzx.lakemart.model.vo;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class HotProductVO {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private String imageUrl;
    private Long heatScore;  // 热度分数（点击次数）
}