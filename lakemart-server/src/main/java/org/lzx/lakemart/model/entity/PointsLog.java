package org.lzx.lakemart.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_points_log")
public class PointsLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer pointsChange;   // 正数增加，负数减少
    private Integer balance;        // 变动后余额
    private String type;            // ORDER_CREATE, ORDER_CANCEL, ADMIN_ADJUST
    private Long relatedId;         // 关联订单ID等
    private String remark;
    private LocalDateTime createTime;
}