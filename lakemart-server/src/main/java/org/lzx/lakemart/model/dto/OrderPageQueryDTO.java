package org.lzx.lakemart.model.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class OrderPageQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long userId;       // 可选，按用户筛选
    private Integer status;    // 可选，按状态筛选
    private LocalDate startDate; // 下单开始日期
    private LocalDate endDate;   // 下单结束日期
}