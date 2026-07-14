package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class BehaviorMessage {
    private Long userId;
    private String action;
    private Long productId;
    private String ts;           // ISO 格式时间字符串
    private String experimentId; // 新增：实验标识，如 "ALS_EXP" 或 "CONTROL"
}