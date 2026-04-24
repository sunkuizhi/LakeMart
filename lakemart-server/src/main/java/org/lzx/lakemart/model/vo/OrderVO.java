package org.lzx.lakemart.model.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
// 订单视图对象（返回给前端）
@Data
@Builder
public class OrderVO {
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalAmount;
    private Integer status;          // 0待支付 1已支付 2已发货 3已完成 4已取消
    private String statusDesc;       // 状态描述
    private LocalDateTime createTime;
    private List<OrderItemVO> items; // 订单项列表
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
}