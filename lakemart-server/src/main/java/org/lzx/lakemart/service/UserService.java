package org.lzx.lakemart.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.lzx.lakemart.model.dto.UserPageQueryDTO;
import org.lzx.lakemart.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.lakemart.model.vo.UserVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
public interface UserService extends IService<User> {
    User findByEmail(String email);
    // 分页查询用户（管理端）
    Page<UserVO> adminQueryPage(UserPageQueryDTO query);
    // 更新用户状态
    void updateUserStatus(Long userId, Integer status);
    // 重置密码
    void resetUserPassword(Long userId, String newPassword);

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * 管理员手动调整用户积分
     * @param userId 用户ID
     * @param pointsChange 变动值（正加负减）
     * @param remark 备注
     */
    void adminAdjustPoints(Long userId, Integer pointsChange, String remark);
    void changePassword(Long userId, String oldPassword, String newPassword);
    void updateAvatar(Long userId, String avatarUrl);
    void sendVerificationCode(String email, String type);
    void changeEmail(Long userId, String newEmail, String code);
    void resetPassword(String email, String code, String newPassword);
    void updateProfile(Long userId, Map<String, Object> updates);



}
