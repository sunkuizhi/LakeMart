package org.lzx.lakemart.model.vo;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CategoryVO {
    private Long id;
    private String name;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;          // 1启用 0禁用
    private LocalDateTime createTime;
    private List<CategoryVO> children; // 子分类列表
}