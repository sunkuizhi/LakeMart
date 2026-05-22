package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class ProductQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long categoryId;          // 分类ID（精确或包含子分类）
    private Long parentCategoryId;    // 父分类ID（保留兼容）
    private String keyword;           // 商品名称模糊搜索
    private String sortBy;            // 排序字段：price, salesCount, createTime
    private String sortOrder;         // asc / desc
    private Boolean includeChildren = false;  // 是否包含子分类，默认 false
}