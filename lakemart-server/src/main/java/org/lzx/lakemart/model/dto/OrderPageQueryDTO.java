package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class OrderPageQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long userId;     // 可选，按用户筛选
    private Integer status;  // 可选，按状态筛选
}