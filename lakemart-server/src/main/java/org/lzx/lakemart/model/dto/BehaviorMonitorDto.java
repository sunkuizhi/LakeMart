package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class BehaviorMonitorDto {
    private Long userId;
    private String username;
    private String action;
    private Long productId;
    private String productName;
    private String time;
}