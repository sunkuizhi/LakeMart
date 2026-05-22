package org.lzx.lakemart.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Font;
import org.lzx.lakemart.model.dto.OrderPageQueryDTO;
import org.lzx.lakemart.model.dto.UpdateOrderStatusRequest;
import org.lzx.lakemart.model.entity.Order;
import org.lzx.lakemart.model.vo.OrderVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
@RestController
@RequestMapping("/api/admin/order")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/list")
    public Result<Page<OrderVO>> listOrders(@RequestBody OrderPageQueryDTO query) {
        Page<OrderVO> page = orderService.adminQueryOrders(query);
        return Result.success(page);
    }

    @PutMapping("/status")
    public Result<String> updateOrderStatus(@Valid @RequestBody UpdateOrderStatusRequest request) {
        orderService.adminUpdateOrderStatus(request.getOrderId(), request.getStatus());
        return Result.success("订单状态更新成功");
    }

    @GetMapping("/detail/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<OrderVO> getOrderDetail(@PathVariable("orderId") Long orderId) {
        OrderVO order = orderService.getOrderDetailForAdmin(orderId);
        return Result.success(order);
    }

    /**
     * 根据下单日期获取订单列表（用于订单趋势下钻）
     * @param date 日期（yyyy-MM-dd）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页订单数据
     */
    @GetMapping("/orders-by-date")
    public Result<Page<Order>> getOrdersByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(Order::getCreateTime, start, end)
                .orderByDesc(Order::getCreateTime);
        Page<Order> orderPage = orderService.page(page, wrapper);

        return Result.success(orderPage);
    }
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportOrders(@RequestBody OrderPageQueryDTO query) {
        query.setPageNum(1);
        query.setPageSize(Integer.MAX_VALUE);
        Page<OrderVO> page = orderService.adminQueryOrders(query);
        List<OrderVO> orders = page.getRecords();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("订单列表");

            // 创建表头样式
            CellStyle headerStyle = getHeaderStyle(workbook);

            // 表头
            String[] headers = {"订单ID", "订单号", "用户ID", "总金额", "订单状态", "下单时间", "收货人", "收货电话", "收货地址"};
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
            for (OrderVO order : orders) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getOrderNo());
                row.createCell(2).setCellValue(order.getUserId());
                row.createCell(3).setCellValue(order.getTotalAmount().doubleValue());
                row.createCell(4).setCellValue(order.getStatusDesc());
                row.createCell(5).setCellValue(order.getCreateTime() != null ? order.getCreateTime().format(formatter) : "");
                row.createCell(6).setCellValue(order.getReceiverName() != null ? order.getReceiverName() : "");
                row.createCell(7).setCellValue(order.getReceiverPhone() != null ? order.getReceiverPhone() : "");
                row.createCell(8).setCellValue(order.getReceiverAddress() != null ? order.getReceiverAddress() : "");
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            byte[] bytes = out.toByteArray();

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeaders.setContentDispositionFormData("attachment", "orders_" + System.currentTimeMillis() + ".xlsx");

            return ResponseEntity.ok().headers(responseHeaders).body(bytes);
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }
    private CellStyle getHeaderStyle(Workbook workbook) {
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