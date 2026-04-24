package org.lzx.lakemart.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.lzx.lakemart.model.dto.AdminPointsAdjustRequest;
import org.lzx.lakemart.model.dto.UserPageQueryDTO;
import org.lzx.lakemart.model.dto.UserPasswordResetRequest;
import org.lzx.lakemart.model.dto.UserStatusUpdateRequest;
import org.lzx.lakemart.model.vo.UserVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.PointsLogService;
import org.lzx.lakemart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PointsLogService pointsLogService;

    @PostMapping("/list")
    public Result<Page<UserVO>> listUsers(@RequestBody UserPageQueryDTO query) {
        Page<UserVO> page = userService.adminQueryPage(query);
        return Result.success(page);
    }

    @PutMapping("/status")
    public Result<String> updateStatus(@Valid @RequestBody UserStatusUpdateRequest request) {
        userService.updateUserStatus(request.getUserId(), request.getStatus());
        return Result.success("状态更新成功");
    }

    @PutMapping("/password/reset")
    public Result<String> resetPassword(@Valid @RequestBody UserPasswordResetRequest request) {
        userService.resetUserPassword(request.getUserId(), request.getNewPassword());
        return Result.success("密码重置成功");
    }
    @PutMapping("/points/adjust")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> adjustPoints(@Valid @RequestBody AdminPointsAdjustRequest request) {
        userService.adminAdjustPoints(request.getUserId(), request.getPointsChange(), request.getRemark());
        return Result.success("积分调整成功");
    }

}