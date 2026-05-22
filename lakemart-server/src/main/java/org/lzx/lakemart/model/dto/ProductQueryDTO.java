// ProductQueryDTO.java
package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class ProductQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long categoryId;          // 精确匹配（三级分类ID）
    private Long parentCategoryId;    // 新增：父分类ID（一级或二级），会自动包含其所有子分类
    private String keyword;           // 商品名称模糊搜索
    private String sortBy;            // 排序字段：price, salesCount, createTime
    private String sortOrder;         // asc / desc
}