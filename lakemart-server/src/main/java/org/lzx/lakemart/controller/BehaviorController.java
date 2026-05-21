package org.lzx.lakemart.controller;

import org.lzx.lakemart.kafka.BehaviorProducer;
import org.lzx.lakemart.model.dto.BehaviorMessage;
import org.lzx.lakemart.security.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/behavior")
public class BehaviorController {

    private static final Logger log = LoggerFactory.getLogger(BehaviorController.class);

    @Autowired
    private BehaviorProducer behaviorProducer;

    @PostMapping("/track")
    public String track(@RequestBody BehaviorMessage message) {
        // 从 SecurityContext 获取当前登录的 SecurityUser
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = null;
        if (principal instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) principal;
            userId = securityUser.getId();
            log.debug("获取到登录用户ID: {}", userId);
        } else {
            // 正常情况下不应该进入这里，因为 SecurityConfig 中要求了认证
            log.error("认证信息异常，principal 类型不是 SecurityUser: {}",
                    principal == null ? "null" : principal.getClass().getName());
            throw new RuntimeException("用户认证信息无效");
        }
        message.setUserId(userId);

        // 处理时间戳：如果前端没有传，则自动生成 ISO 格式字符串
        if (message.getTs() == null) {
            String formattedTs = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            message.setTs(formattedTs);
            log.debug("自动生成时间戳: {}", formattedTs);
        }

        // 异步发送到 Kafka
        behaviorProducer.sendBehavior(message);
        log.info("埋点消息已发送至Kafka: action={}, productId={}, userId={}",
                message.getAction(), message.getProductId(), message.getUserId());
        return "OK";
    }
}