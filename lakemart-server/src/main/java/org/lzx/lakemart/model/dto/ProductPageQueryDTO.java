package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class ProductPageQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String name;      // 商品名称模糊搜索
    private Long categoryId;  // 分类筛选
    private Integer status;   // 上架状态：1上架 0下架
}