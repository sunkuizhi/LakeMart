package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class ProductPageQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String name;               // 商品名称模糊搜索
    private Long categoryId;           // 分类ID
    private Integer status;            // 状态：1上架 0下架
    private Boolean includeChildren = false;  // 是否包含子分类（默认false，仅精确匹配）
}