package org.lzx.lakemart.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户行为日志表
 */
@Getter
@Setter
@ToString
@TableName("user_behavior_log")
public class BehaviorLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("action")
    private String action;

    @TableField("product_id")
    private Long productId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("experiment_id")
    private String experimentId;

    // ===== 新增：推荐反馈字段 =====
    @TableField("scene")
    private String scene;

    @TableField("position")
    private Integer position;

    @TableField("trace_id")
    private String traceId;
}