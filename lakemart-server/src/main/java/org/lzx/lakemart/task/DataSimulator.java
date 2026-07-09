package org.lzx.lakemart.task;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.lzx.lakemart.service.ProductService;
import org.lzx.lakemart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@Slf4j
@Profile("dev")
public class DataSimulator {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;  // ✅ 用于直接操作数据库

    private final Random random = new Random();
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    private volatile List<Long> userIds = List.of(1L, 2L, 3L);
    private volatile List<Long> productIds = List.of(1L, 2L, 3L);
    private volatile long lastRefreshTime = 0;

    private static final String[] ACTIONS = {"VIEW_PRODUCT", "ADD_CART", "BUY", "FAVORITE"};
    private static final int[] WEIGHTS = {60, 20, 15, 5};

    @Scheduled(fixedDelay = 100)
    public void simulateBehavior() {
        refreshIdCacheIfNeeded();

        if (userIds.isEmpty() || productIds.isEmpty()) {
            log.warn("数据库中没有用户或商品数据，模拟器暂停发送。");
            return;
        }

        Long userId = userIds.get(random.nextInt(userIds.size()));
        Long productId = productIds.get(random.nextInt(productIds.size()));
        String action = pickActionByWeight();

        // 1️⃣ 发送 Kafka 消息（行为日志）
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("productId", productId);
            data.put("action", action);
            data.put("ts", LocalDateTime.now().format(formatter));

            String json = JSONUtil.toJsonStr(data);
            kafkaTemplate.send("user-behaviors", json);
            log.debug("模拟行为发送成功: {}", json);
        } catch (Exception e) {
            log.error("模拟行为发送失败", e);
        }

        // 2️⃣ 如果是 BUY，同时创建真实订单
        if ("BUY".equals(action)) {
            simulateBuyOrder(userId, productId);
        }
    }

    /**
     * 模拟一次真实购买：创建订单和订单项
     */
    private void simulateBuyOrder(Long userId, Long productId) {
        try {
            // 1. 查询商品价格和名称
            String priceSql = "SELECT price, name FROM tb_product WHERE id = ?";
            Map<String, Object> productInfo = jdbcTemplate.queryForMap(priceSql, productId);
            BigDecimal price = (BigDecimal) productInfo.get("price");
            String productName = (String) productInfo.get("name");

            // 2. 随机数量（1~3件）
            int quantity = random.nextInt(3) + 1;
            BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(quantity));

            // 3. 生成订单号
            String orderNo = "ORD" + System.currentTimeMillis() + random.nextInt(1000);

            // 4. 安全获取用户地址（使用 try-catch 兜底）
            String receiverName = "测试用户";
            String receiverPhone = "13800000000";
            String detailAddress = "默认测试地址";
            try {
                String addressSql = "SELECT receiver_name, receiver_phone, detail_address FROM tb_address WHERE user_id = ? LIMIT 1";
                Map<String, Object> address = jdbcTemplate.queryForMap(addressSql, userId);
                receiverName = (String) address.get("receiver_name");
                receiverPhone = (String) address.get("receiver_phone");
                detailAddress = (String) address.get("detail_address");
            } catch (Exception e) {
                log.warn("用户 {} 没有地址，使用默认地址", userId);
            }

            // 5. 插入订单
            String orderSql = "INSERT INTO tb_order (order_no, user_id, total_amount, status, create_time, receiver_name, receiver_phone, receiver_address) " +
                    "VALUES (?, ?, ?, 1, NOW(), ?, ?, ?)";
            jdbcTemplate.update(orderSql, orderNo, userId, totalAmount, receiverName, receiverPhone, detailAddress);

            // 6. 获取刚插入的订单ID
            Long orderId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

            // 7. 插入订单项
            String itemSql = "INSERT INTO tb_order_item (order_id, product_id, product_name, price, quantity, create_time) " +
                    "VALUES (?, ?, ?, ?, ?, NOW())";
            jdbcTemplate.update(itemSql, orderId, productId, productName, price, quantity);

            log.info("✅ 模拟订单创建成功: 用户={}, 商品={}, 数量={}, 金额={}", userId, productName, quantity, totalAmount);

        } catch (Exception e) {
            log.error("❌ 创建模拟订单失败: userId={}, productId={}, error={}", userId, productId, e.getMessage(), e);
        }
    }
    private String pickActionByWeight() {
        int totalWeight = 0;
        for (int w : WEIGHTS) {
            totalWeight += w;
        }
        int rand = random.nextInt(totalWeight);
        int cumulative = 0;
        for (int i = 0; i < WEIGHTS.length; i++) {
            cumulative += WEIGHTS[i];
            if (rand < cumulative) {
                return ACTIONS[i];
            }
        }
        return ACTIONS[0];
    }

    private void refreshIdCacheIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastRefreshTime > 5 * 60 * 1000) {
            try {
                userIds = userService.list().stream()
                        .filter(user -> user.getStatus() != null && user.getStatus() == 1)
                        .map(user -> user.getId())
                        .collect(Collectors.toList());

                productIds = productService.list().stream()
                        .filter(product -> product.getStatus() != null && product.getStatus() == 1)
                        .map(product -> product.getId())
                        .collect(Collectors.toList());

                log.info("模拟器 ID 缓存刷新完成: 用户 {} 个, 商品 {} 个", userIds.size(), productIds.size());
                lastRefreshTime = now;
            } catch (Exception e) {
                log.error("刷新 ID 缓存失败", e);
            }
        }
    }
}