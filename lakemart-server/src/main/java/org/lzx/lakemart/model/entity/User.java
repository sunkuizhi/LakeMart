package org.lzx.lakemart.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.lzx.lakemart.model.vo.UserVO;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
@Getter
@Setter
@ToString
@TableName("tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码（BCrypt 加密）
     */
    @TableField("password")
    private String password;

    /**
     * 邮箱（登录账号）
     */
    @TableField("email")
    private String email;

    /**
     * 手机号（可选）
     */
    @TableField("phone")
    private String phone;

    /**
     * 角色
     */
    @TableField("role")
    private String role;

    /**
     * 积分
     */
    @TableField("points")
    private Integer points;

    /**
     * 头像 URL
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 个人简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 1启用 0禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 注册时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    public UserVO toVO() {
        return UserVO.builder()
                .id(this.getId())
                .username(this.getUsername())
                .email(this.getEmail())
                .phone(this.getPhone())
                .avatarUrl(this.getAvatarUrl())
                .introduction(this.getIntroduction())
                .points(this.getPoints())
                .role(this.getRole())
                .statusDesc(this.getStatus() == 1 ? "启用" : "禁用")
                .createTime(this.getCreateTime() != null ? this.getCreateTime().toString() : null)
                .build();
    }
}
