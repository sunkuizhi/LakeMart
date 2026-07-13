package org.lzx.lakemart.service.impl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.mapper.UserMapper;
import org.lzx.lakemart.model.dto.UserPageQueryDTO;
import org.lzx.lakemart.model.entity.User;
import org.lzx.lakemart.model.vo.UserVO;
import org.lzx.lakemart.service.common.IPointsLogService;
import org.lzx.lakemart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Override
    public User findByEmail(String email) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
    }
    @Override
    public Page<UserVO> adminQueryPage(UserPageQueryDTO query) {
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (query.getUsername() != null && !query.getUsername().isEmpty()) {
            wrapper.like(User::getUsername, query.getUsername());
        }
        if (query.getEmail() != null && !query.getEmail().isEmpty()) {
            wrapper.like(User::getEmail, query.getEmail());
        }
        if (query.getStatus() != null) {
            wrapper.eq(User::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(User::getCreateTime);
        Page<User> userPage = baseMapper.selectPage(page, wrapper);
        // 转换为 UserVO
        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> voList = userPage.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, Integer status) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus(status);
        baseMapper.updateById(user);
    }

    @Override
    @Transactional
    public void resetUserPassword(Long userId, String newPassword) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            newPassword = "12345678";  // 默认密码
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        baseMapper.updateById(user);
    }

    // 辅助方法：User -> UserVO
    private UserVO toVO(User user) {
        return UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .introduction(user.getIntroduction())
                .points(user.getPoints())
                .role(user.getRole())
                .statusDesc(user.getStatus() == 1 ? "启用" : "禁用")
                .createTime(user.getCreateTime() != null ? user.getCreateTime().toString() : null)
                .build();
    }
    @Autowired
    private IPointsLogService IPointsLogService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (user.getStatus() == 0) {
            throw new UsernameNotFoundException("账号已被禁用");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
    @Override
    @Transactional
    public void adminAdjustPoints(Long userId, Integer pointsChange, String remark) {
        // 查询用户当前积分
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        Integer newPoints = user.getPoints() + pointsChange;
        if (newPoints < 0) {
            throw new RuntimeException("调整后积分不能为负数");
        }
        // 更新积分
        user.setPoints(newPoints);
        baseMapper.updateById(user);
        // 记录积分明细
        IPointsLogService.recordPoints(userId, pointsChange, "ADMIN_ADJUST", null, remark);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        baseMapper.updateById(user);
    }
    @Override
    @Transactional
    public void updateAvatar(Long userId, String avatarUrl) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setAvatarUrl(avatarUrl);
        baseMapper.updateById(user);
    }
    private static final long CODE_EXPIRE_SECONDS = 300; // 5分钟

    @Override
    public void sendVerificationCode(String email, String type) {
        // 生成6位随机数字验证码
        String code = String.format("%06d", new Random().nextInt(999999));
        String key = "verify_code:" + type + ":" + email;
        redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_SECONDS, TimeUnit.SECONDS);

        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("LakeMart 验证码");
        String content = "您正在进行" + ("CHANGE_EMAIL".equals(type) ? "修改邮箱" : "重置密码") +
                "操作，验证码为：" + code + "，有效期5分钟。";
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    @Transactional
    public void changeEmail(Long userId, String newEmail, String code) {
        // 验证验证码
        String key = "verify_code:CHANGE_EMAIL:" + newEmail;
        String storedCode = redisTemplate.opsForValue().get(key);
        if (storedCode == null || !storedCode.equals(code)) {
            throw new RuntimeException("验证码错误或已过期");
        }
        // 检查新邮箱是否已被其他用户使用
        User exist = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, newEmail));
        if (exist != null && !exist.getId().equals(userId)) {
            throw new RuntimeException("邮箱已被使用");
        }
        // 更新邮箱
        User user = baseMapper.selectById(userId);
        if (user == null) throw new RuntimeException("用户不存在");
        user.setEmail(newEmail);
        baseMapper.updateById(user);
        // 删除验证码
        redisTemplate.delete(key);
    }
    @Override
    @Transactional
    public void updateProfile(Long userId, Map<String, Object> updates) {
        User user = baseMapper.selectById(userId);
        if (user == null) throw new RuntimeException("用户不存在");
        if (updates.containsKey("username")) {
            String username = (String) updates.get("username");
            if (username != null && !username.isEmpty()) {
                user.setUsername(username);
            }
        }
        if (updates.containsKey("phone")) {
            String phone = (String) updates.get("phone");
            user.setPhone(phone);
        }
        if (updates.containsKey("introduction")) {
            String introduction = (String) updates.get("introduction");
            user.setIntroduction(introduction);
        }
        // 可继续扩展其他字段（如 avatarUrl 已有单独接口）
        baseMapper.updateById(user);
    }
    @Override
    @Transactional
    public void resetPassword(String email, String code, String newPassword) {
        String key = "verify_code:RESET_PASSWORD:" + email;
        String storedCode = redisTemplate.opsForValue().get(key);
        if (storedCode == null || !storedCode.equals(code)) {
            throw new RuntimeException("验证码错误或已过期");
        }
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (user == null) throw new RuntimeException("用户不存在");
        user.setPassword(passwordEncoder.encode(newPassword));
        baseMapper.updateById(user);
        redisTemplate.delete(key);
    }
}