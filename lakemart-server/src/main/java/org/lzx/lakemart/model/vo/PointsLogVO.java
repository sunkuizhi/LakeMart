package org.lzx.lakemart.model.vo;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PointsLogVO {
    private Long id;
    private Integer pointsChange;   // 积分变动（正数增加，负数减少）
    private Integer balance;        // 变动后积分余额
    private String type;            // 类型：ORDER_CREATE, ORDER_CANCEL, ADMIN_ADJUST
    private Long relatedId;         // 关联业务ID（如订单ID）
    private String remark;          // 备注
    private LocalDateTime createTime;
}