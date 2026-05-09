package org.lzx.lakemart.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.lzx.lakemart.kafka.UserActionProducer;
import org.lzx.lakemart.model.dto.LoginRequest;
import org.lzx.lakemart.model.dto.PasswordChangeRequest;
import org.lzx.lakemart.model.dto.RegisterRequest;
import org.lzx.lakemart.model.dto.UserActionLogDTO;
import org.lzx.lakemart.model.entity.User;
import org.lzx.lakemart.model.vo.PointsLogVO;
import org.lzx.lakemart.model.vo.UserVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.PointsLogService;
import org.lzx.lakemart.service.UserService;
import org.lzx.lakemart.util.JwtUtil;
import org.lzx.lakemart.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PointsLogService pointsLogService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private UserActionProducer userActionProducer;

    @Autowired
    private HttpServletRequest request;
    @GetMapping("/test")
    public String test() {
        return "Hello, LakeMart!";
    }

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterRequest request) {
        User exist = userService.findByEmail(request.getEmail());
        if (exist != null) {
            return Result.error("邮箱已注册");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole("ROLE_USER");
        user.setStatus(1);
        user.setPoints(0);
        userService.save(user);
        UserVO vo = user.toVO();
        return Result.success(vo);
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.findByEmail(request.getEmail());
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        String token = jwtUtil.generateToken(claims);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("role", user.getRole());
        return Result.success(data);
    }

    @GetMapping("/points/logs")
    public Result<Page<PointsLogVO>> getPointsLogs(@AuthenticationPrincipal Long userId,
                                                   @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<PointsLogVO> page = pointsLogService.getUserPointsLogs(userId, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/profile")
    public Result<UserVO> getProfile(@AuthenticationPrincipal Long userId) {
        User user = userService.getById(userId);
        return Result.success(user.toVO());
    }
    @PutMapping("/profile")
    public Result<String> updateProfile(@AuthenticationPrincipal Long userId,
                                        @RequestBody Map<String, Object> updates) {
        userService.updateProfile(userId, updates);
        return Result.success("更新成功");
    }

    @PutMapping("/password")
    public Result<String> changePassword(@AuthenticationPrincipal Long userId,
                                         @Valid @RequestBody PasswordChangeRequest request) {
        userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
        return Result.success("密码修改成功");
    }

    @PostMapping("/avatar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<String> uploadAvatar(@AuthenticationPrincipal Long userId,
                                       @RequestParam("file") MultipartFile file) {
        String avatarUrl = minioUtil.uploadFile(file, "avatar");
        userService.updateAvatar(userId, avatarUrl);
        return Result.success(avatarUrl);
    }

    @PostMapping("/send-code")
    public Result<String> sendVerificationCode(@RequestParam String email,
                                               @RequestParam String type) {
        userService.sendVerificationCode(email, type);
        return Result.success("验证码已发送");
    }

    @PutMapping("/email")
    public Result<String> changeEmail(@AuthenticationPrincipal Long userId,
                                      @RequestParam String newEmail,
                                      @RequestParam String code) {
        userService.changeEmail(userId, newEmail, code);
        return Result.success("邮箱修改成功");
    }

    @PostMapping("/reset-password")
    public Result<String> resetPassword(@RequestParam String email,
                                        @RequestParam String code,
                                        @RequestParam String newPassword) {
        userService.resetPassword(email, code, newPassword);
        return Result.success("密码重置成功");
    }
    @PostMapping("/action")
    public Result<String> collectAction(@AuthenticationPrincipal Long userId,
                                        @RequestBody UserActionLogDTO action) {
        // 填充后端才能获取的信息
        action.setUserId(userId);
        action.setTimestamp(System.currentTimeMillis());
        action.setIp(request.getRemoteAddr());
        action.setUserAgent(request.getHeader("User-Agent"));

        // 发送到 Kafka
        userActionProducer.sendAction(action);
        return Result.success("ok");
    }
}