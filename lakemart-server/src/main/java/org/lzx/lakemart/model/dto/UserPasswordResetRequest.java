package org.lzx.lakemart.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPasswordResetRequest {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    private String newPassword;  // 可选，如果为空则生成默认密码
}