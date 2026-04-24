package org.lzx.lakemart.model.vo;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

@Data
@Builder
public class UserVO implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String avatarUrl;
    private String introduction;
    private Integer points;
    private String role;
    private String statusDesc;    // 由 status 转换而来，如 "启用" / "禁用"
    private String createTime;    // 格式化后的时间字符串，如 "2025-01-01 12:00:00"
}