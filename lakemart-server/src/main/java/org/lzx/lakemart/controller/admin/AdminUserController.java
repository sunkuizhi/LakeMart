package org.lzx.lakemart.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lzx.lakemart.model.dto.AdminPointsAdjustRequest;
import org.lzx.lakemart.model.dto.UserPageQueryDTO;
import org.lzx.lakemart.model.dto.UserPasswordResetRequest;
import org.lzx.lakemart.model.dto.UserStatusUpdateRequest;
import org.lzx.lakemart.model.vo.UserVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.common.IPointsLogService;
import org.lzx.lakemart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private IPointsLogService IPointsLogService;

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

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportUsers(@RequestBody UserPageQueryDTO query) {
        // 不分页，查询所有符合条件的数据
        query.setPageNum(1);
        query.setPageSize(Integer.MAX_VALUE);
        Page<UserVO> page = userService.adminQueryPage(query);
        List<UserVO> users = page.getRecords();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("用户列表");

            // 表头样式
            CellStyle headerStyle = createHeaderStyle(workbook);

            // 表头
            String[] headers = {"用户ID", "用户名", "邮箱", "手机号", "积分", "角色", "状态", "注册时间"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            // 数据行
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;
            for (UserVO user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getUsername());
                row.createCell(2).setCellValue(user.getEmail());
                row.createCell(3).setCellValue(user.getPhone() != null ? user.getPhone() : "");
                row.createCell(4).setCellValue(user.getPoints());
                row.createCell(5).setCellValue(user.getRole());
                row.createCell(6).setCellValue(user.getStatusDesc());
                row.createCell(7).setCellValue(user.getCreateTime() != null ? user.getCreateTime() : "");
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            byte[] bytes = out.toByteArray();

            // 注意：这里将 HttpHeaders headers 改为 HttpHeaders responseHeaders
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeaders.setContentDispositionFormData("attachment", "users_" + System.currentTimeMillis() + ".xlsx");

            return ResponseEntity.ok().headers(responseHeaders).body(bytes);
        } catch (Exception e) {
            throw new RuntimeException("导出用户失败", e);
        }
    }
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}