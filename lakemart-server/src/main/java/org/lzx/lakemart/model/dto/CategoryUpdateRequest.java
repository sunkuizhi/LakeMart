package org.lzx.lakemart.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryUpdateRequest {
    @NotNull(message = "分类ID不能为空")
    private Long id;
    private String name;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;   // 1启用 0禁用
}