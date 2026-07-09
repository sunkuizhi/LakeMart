package org.lzx.lakemart.controller;

import org.lzx.lakemart.kafka.BehaviorProducer;
import org.lzx.lakemart.model.dto.BehaviorMessage;
import org.lzx.lakemart.model.dto.BehaviorMonitorDto;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.security.SecurityUser;
import org.lzx.lakemart.service.BehaviorLogService;
import org.lzx.lakemart.service.ProductService;
import org.lzx.lakemart.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/behavior")
public class BehaviorController {

    private static final Logger log = LoggerFactory.getLogger(BehaviorController.class);

    @Autowired
    private BehaviorProducer behaviorProducer;

    @Autowired
    private BehaviorLogService behaviorLogService;

    // 可选 WebSocket 推送（暂时不使用）
    // @Autowired
    // private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * 用户行为埋点接口
     */
    @PostMapping("/track")
    public String track(@RequestBody BehaviorMessage message) {
        // 获取当前登录用户
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = null;
        if (principal instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) principal;
            userId = securityUser.getId();
            log.debug("获取到登录用户ID: {}", userId);
        } else {
            log.error("认证信息异常，principal 类型不是 SecurityUser: {}",
                    principal == null ? "null" : principal.getClass().getName());
            throw new RuntimeException("用户认证信息无效");
        }
        message.setUserId(userId);

        // 处理时间戳
        if (message.getTs() == null) {
            String formattedTs = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            message.setTs(formattedTs);
            log.debug("自动生成时间戳: {}", formattedTs);
        }

        // 1. 写入数据库
        behaviorLogService.saveBehaviorLog(message);

        // 2. 异步发送到 Kafka
        behaviorProducer.sendBehavior(message);

        // 3. （可选）实时推送给管理端 WebSocket（暂时注释，改用轮询）
        // try {
        //     String username = userService.getById(userId) != null ? userService.getById(userId).getUsername() : "未知";
        //     String productName = productService.getById(message.getProductId()) != null ? productService.getById(message.getProductId()).getName() : "未知商品";
        //     BehaviorMonitorDto dto = new BehaviorMonitorDto();
        //     dto.setUserId(userId);
        //     dto.setUsername(username);
        //     dto.setAction(message.getAction());
        //     dto.setProductId(message.getProductId());
        //     dto.setProductName(productName);
        //     dto.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        //     messagingTemplate.convertAndSend("/topic/behavior", dto);
        //     log.info("WebSocket 推送行为：userId={}, action={}", userId, message.getAction());
        // } catch (Exception e) {
        //     log.error("WebSocket 推送失败", e);
        // }

        log.info("埋点消息已发送至Kafka: action={}, productId={}, userId={}",
                message.getAction(), message.getProductId(), message.getUserId());
        return "OK";
    }

    /**
     * 获取最近用户行为（供管理端轮询）
     * @param limit 获取数量
     * @return 行为列表
     */
    @GetMapping("/recent")
    public Result<List<BehaviorMonitorDto>> getRecentBehaviors(@RequestParam(defaultValue = "50") int limit) {
        List<BehaviorMonitorDto> list = behaviorLogService.getRecentBehaviors(limit);
        return Result.success(list);
    }
}