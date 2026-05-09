package org.lzx.lakemart.model.dto;

import lombok.Data;

@Data
public class UserActionLogDTO {
    private Long userId;
    private String actionType;   // view_product, add_cart, buy
    private Long productId;
    private Long timestamp;
    private String ip;
    private String userAgent;
}