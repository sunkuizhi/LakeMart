package org.lzx.lakemart.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminPointsAdjustRequest {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotNull(message = "积分变动不能为空")
    private Integer pointsChange;   // 正数增加，负数减少
    private String remark;          // 调整原因
}