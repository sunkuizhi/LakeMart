// dto/OrderStatisticsDTO.java
package org.lzx.lakemart.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderStatisticsDTO {
    private LocalDate date;           // 日期
    private Integer orderCount;       // 订单数
    private BigDecimal totalAmount;   // 销售额（GMV）
    private Integer totalItems;       // 销售商品总数
}