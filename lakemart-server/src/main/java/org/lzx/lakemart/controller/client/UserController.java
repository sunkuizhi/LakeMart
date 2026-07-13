package org.lzx.lakemart.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.lzx.lakemart.model.dto.LoginRequest;
import org.lzx.lakemart.model.dto.PasswordChangeRequest;
import org.lzx.lakemart.model.dto.RegisterRequest;
import org.lzx.lakemart.model.entity.Product;
import org.lzx.lakemart.model.entity.User;
import org.lzx.lakemart.model.vo.PointsLogVO;
import org.lzx.lakemart.model.vo.ProductVO;
import org.lzx.lakemart.model.vo.RecommendProductVO;
import org.lzx.lakemart.model.vo.UserVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.security.SecurityUser;
import org.lzx.lakemart.service.ProductService;
import org.lzx.lakemart.service.UserService;
import org.lzx.lakemart.service.client.IRecommendService;
import org.lzx.lakemart.service.common.IPointsLogService;
import org.lzx.lakemart.util.JwtUtil;
import org.lzx.lakemart.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IPointsLogService IPointsLogService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IRecommendService IRecommendService;

    @Autowired
    private ProductService productService;

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
    public Result<Page<PointsLogVO>> getPointsLogs(@AuthenticationPrincipal SecurityUser securityUser,
                                                   @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Long userId = securityUser.getId();
        Page<PointsLogVO> page = IPointsLogService.getUserPointsLogs(userId, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/profile")
    public Result<UserVO> getProfile(@AuthenticationPrincipal SecurityUser securityUser) {
        Long userId = securityUser.getId();
        User user = userService.getById(userId);
        return Result.success(user.toVO());
    }

    @PutMapping("/profile")
    public Result<String> updateProfile(@AuthenticationPrincipal SecurityUser securityUser,
                                        @RequestBody Map<String, Object> updates) {
        Long userId = securityUser.getId();
        userService.updateProfile(userId, updates);
        return Result.success("更新成功");
    }

    @PutMapping("/password")
    public Result<String> changePassword(@AuthenticationPrincipal SecurityUser securityUser,
                                         @Valid @RequestBody PasswordChangeRequest request) {
        Long userId = securityUser.getId();
        userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
        return Result.success("密码修改成功");
    }

    @PostMapping("/avatar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Result<String> uploadAvatar(@AuthenticationPrincipal SecurityUser securityUser,
                                       @RequestParam(name = "file") MultipartFile file) {
        Long userId = securityUser.getId();
        String avatarUrl = minioUtil.uploadFile(file, "avatar");
        userService.updateAvatar(userId, avatarUrl);
        return Result.success(avatarUrl);
    }

    @PostMapping("/send-code")
    public Result<String> sendVerificationCode(@RequestParam(name = "email") String email,
                                               @RequestParam(name = "type") String type) {
        userService.sendVerificationCode(email, type);
        return Result.success("验证码已发送");
    }

    @PutMapping("/email")
    public Result<String> changeEmail(@AuthenticationPrincipal SecurityUser securityUser,
                                      @RequestParam(name = "newEmail") String newEmail,
                                      @RequestParam(name = "code") String code) {
        Long userId = securityUser.getId();
        userService.changeEmail(userId, newEmail, code);
        return Result.success("邮箱修改成功");
    }

    @PostMapping("/reset-password")
    public Result<String> resetPassword(@RequestParam(name = "email") String email,
                                        @RequestParam(name = "code") String code,
                                        @RequestParam(name = "newPassword") String newPassword) {
        userService.resetPassword(email, code, newPassword);
        return Result.success("密码重置成功");
    }

    @GetMapping("/recommend")
    public Result<List<RecommendProductVO>> getRecommendations(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestParam(name = "limit", defaultValue = "12") int limit) {
        System.out.println("======= 推荐接口被调用了！ =======");
        try {
            Long userId = securityUser.getId();
            log.info("=== 推荐接口被调用 ===");
            log.info("用户ID: {}, limit: {}", userId, limit);

            String cacheKey = "recommend:" + userId;
            List<RecommendProductVO> recommendations = null;

            try {
                log.info("尝试从 Redis 读取缓存: {}", cacheKey);
                String cachedJson = (String) redisTemplate.opsForValue().get(cacheKey);
                if (cachedJson != null && !cachedJson.isEmpty()) {
                    recommendations = objectMapper.readValue(cachedJson, new TypeReference<List<RecommendProductVO>>() {});
                    log.info("缓存命中，推荐商品数: {}", recommendations != null ? recommendations.size() : 0);
                } else {
                    log.info("缓存未命中");
                }
            } catch (Exception e) {
                log.warn("读取推荐缓存失败，重新计算", e);
            }

            if (recommendations == null || recommendations.isEmpty()) {
                log.info("开始调用 IRecommendService.recommendForUser");
                recommendations = IRecommendService.recommendForUser(userId, limit);
                log.info("推荐计算完成，商品数: {}", recommendations != null ? recommendations.size() : 0);

                if (recommendations == null) {
                    log.warn("recommendations 为 null，转换为空列表");
                    recommendations = new ArrayList<>();
                }

                // 缓存 1 小时
                try {
                    String json = objectMapper.writeValueAsString(recommendations);
                    redisTemplate.opsForValue().set(cacheKey, json, 1, TimeUnit.HOURS);
                    log.info("推荐结果已缓存");
                } catch (Exception e) {
                    log.warn("写入推荐缓存失败", e);
                }
            }

            if (recommendations.size() > limit) {
                recommendations = recommendations.subList(0, limit);
            }

            log.info("=== 推荐接口返回成功，商品数: {} ===", recommendations.size());
            return Result.success(recommendations);

        } catch (Exception e) {
            log.error("=== 推荐接口异常 ===", e);
            return Result.error("获取推荐失败：" + e.getMessage());
        }
    }

    // 辅助方法：Product -> ProductVO
    private ProductVO convertToProductVO(Product product) {
        return ProductVO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .salesCount(product.getSalesCount())
                .build();
    }
}