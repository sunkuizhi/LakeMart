package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class ProductQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long categoryId;      // 分类ID（可选）
    private String keyword;       // 模糊搜索商品名称
    private String sortBy;        // 排序字段：price, salesCount, createTime
    private String sortOrder;     // asc / desc
}