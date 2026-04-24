package org.lzx.lakemart.model.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductVO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String categoryName;   // 冗余分类名
    private String imageUrl;
    private Integer status;        // 1上架 0下架
    private Integer salesCount;
    private LocalDateTime createTime;
}