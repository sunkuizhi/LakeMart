package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class UserPageQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String username;   // 模糊搜索
    private String email;      // 模糊搜索
    private Integer status;    // 1启用 0禁用
}