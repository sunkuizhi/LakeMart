package org.lzx.lakemart.model.vo;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder
public class DailyAmountVO {
    private LocalDate date;
    private BigDecimal totalAmount;  // 必须是 BigDecimal，不能是 double
    // getters/setters
}