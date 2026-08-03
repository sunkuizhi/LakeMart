package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class BehaviorMessage {
    private Long userId;
    private String action;
    private Long productId;
    private String ts;
    private String experimentId; // AB 测试标识（已有）

    // ===== 新增：推荐反馈字段 =====
    private String scene;        // 场景：home_recommend / cart_recommend / detail_recommend
    private Integer position;    // 在推荐列表中的位置（从 0 开始）
    private String traceId;      // 推荐追踪 ID（用于关联一次推荐请求）
}