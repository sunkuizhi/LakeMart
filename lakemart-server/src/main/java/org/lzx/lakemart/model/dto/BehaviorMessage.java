package org.lzx.lakemart.model.dto;



import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BehaviorMessage {
    private Long userId;
    private String action;
    private Long productId;
    private String ts;   // 改为 String 类型，存储 ISO 格式时间字符串
}