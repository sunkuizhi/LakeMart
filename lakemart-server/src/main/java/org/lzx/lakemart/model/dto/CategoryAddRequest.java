package org.lzx.lakemart.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryAddRequest {
    @NotBlank(message = "分类名称不能为空")
    private String name;
    @NotNull(message = "父分类ID不能为空")
    private Long parentId;
    private Integer sortOrder;
    private Integer status;  // 默认1启用
}