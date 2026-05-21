package org.lzx.lakemart.controller;

import org.lzx.lakemart.model.dto.OrderStatisticsDTO;
import org.lzx.lakemart.model.vo.DailyAmountVO;
import org.lzx.lakemart.model.vo.ProductSalesVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/statistics")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatisticsController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ==================== 原有接口 ====================
    @GetMapping("/order/daily")
    public Result<List<OrderStatisticsDTO>> getDailyOrderStatistics(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<OrderStatisticsDTO> statistics = orderService.getDailyStatistics(startDate, endDate);
        return Result.success(statistics);
    }

    @GetMapping("/hot-products")
    public Result<List<ProductSalesVO>> getHotProducts(@RequestParam(name = "limit", defaultValue = "10") int limit) {
        String sql = "SELECT hp.product_id, p.name as product_name, hp.cnt " +
                "FROM hot_products hp " +
                "LEFT JOIN tb_product p ON hp.product_id = p.id " +
                "ORDER BY hp.cnt DESC LIMIT " + limit;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<ProductSalesVO> result = rows.stream().map(row -> {
            Long productId = ((Number) row.get("product_id")).longValue();
            String productName = (String) row.get("product_name");
            Integer cnt = (Integer) row.get("cnt");
            return ProductSalesVO.builder()
                    .productId(productId)
                    .productName(productName != null ? productName : "商品" + productId)
                    .totalQuantity(cnt)
                    .build();
        }).collect(Collectors.toList());
        return Result.success(result);
    }

    @GetMapping("/sales-trend")
    public Result<List<DailyAmountVO>> getSalesTrend(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyAmountVO> list = orderService.getDailySalesAmount(startDate, endDate);
        return Result.success(list);
    }

    @GetMapping("/behavior-trend")
    public Result<List<Map<String, Object>>> getBehaviorTrend(@RequestParam(name = "minutes", defaultValue = "60") int minutes) {
        return Result.success(getBehaviorTrendData(minutes));
    }
    @GetMapping("/action-distribution")
    public Result<List<Map<String, Object>>> getActionDistribution() {
        String sql = "SELECT action, COUNT(*) as cnt FROM user_behavior_log " +
                "WHERE create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                "GROUP BY action ORDER BY cnt DESC";
        List<Map<String, Object>> rows;
        try {
            rows = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            rows = new ArrayList<>();
        }

        // 标准化行为名称，合并相同含义
        Map<String, Integer> merged = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            String action = (String) row.get("action");
            Integer cnt = ((Number) row.get("cnt")).intValue();
            if (action == null) continue;
            String normalized = action.trim();
            // 统一映射（支持中英文混合）
            if (normalized.equalsIgnoreCase("add_cart") || normalized.equalsIgnoreCase("addCart") || normalized.equals("加入购物车")) {
                normalized = "加入购物车";
            } else if (normalized.equalsIgnoreCase("create_order") || normalized.equalsIgnoreCase("order") || normalized.equals("生成订单")) {
                normalized = "生成订单";
            } else if (normalized.equalsIgnoreCase("pay") || normalized.equalsIgnoreCase("payment") || normalized.equals("支付成功")) {
                normalized = "支付成功";
            } else if (normalized.equalsIgnoreCase("browse") || normalized.equalsIgnoreCase("view") || normalized.equals("浏览商品")) {
                normalized = "浏览商品";
            } else if (normalized.equalsIgnoreCase("search") || normalized.equals("搜索")) {
                normalized = "搜索";
            }
            merged.put(normalized, merged.getOrDefault(normalized, 0) + cnt);
        }

        List<Map<String, Object>> result = merged.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("action", entry.getKey());
                    item.put("cnt", entry.getValue());
                    return item;
                })
                .collect(Collectors.toList());

        // 无数据时返回模拟示例
        if (result.isEmpty()) {
            result = List.of(
                    Map.of("action", "浏览商品", "cnt", 1250),
                    Map.of("action", "加入购物车", "cnt", 380),
                    Map.of("action", "生成订单", "cnt", 210),
                    Map.of("action", "支付成功", "cnt", 198),
                    Map.of("action", "搜索", "cnt", 560)
            );
        }
        return Result.success(result);
    }
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        Map<String, Object> data = new HashMap<>();
        Integer totalOrders = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tb_order WHERE status IN (1,2,3)", Integer.class);
        BigDecimal totalSalesAmount = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(total_amount), 0) FROM tb_order WHERE status IN (1,2,3)", BigDecimal.class);
        Long totalUsers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tb_user", Long.class);
        Integer hotProductCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM hot_products", Integer.class);

        data.put("totalOrders", totalOrders != null ? totalOrders : 0);
        data.put("totalSalesAmount", totalSalesAmount != null ? totalSalesAmount : BigDecimal.ZERO);
        data.put("totalUsers", totalUsers != null ? totalUsers : 0);
        data.put("hotProductCount", hotProductCount != null ? hotProductCount : 0);
        return Result.success(data);
    }

    // ==================== 新增实时模拟接口 ====================
    @PostMapping("/behavior/simulate")
    public Result<Map<String, Object>> simulateRealTimeBehavior() {
        // 1. 随机插入一条行为
        String[] actions = {"浏览商品", "加入购物车", "生成订单", "支付成功", "搜索"};
        String randomAction = actions[new Random().nextInt(actions.length)];
        Long randomUserId = (long) (new Random().nextInt(16) + 1);
        String insertSql = "INSERT INTO user_behavior_log (user_id, action, create_time) VALUES (?, ?, NOW())";
        jdbcTemplate.update(insertSql, randomUserId, randomAction);

        // 2. 返回最新的60分钟趋势数据
        List<Map<String, Object>> trend = getBehaviorTrendData(60);
        Map<String, Object> result = new HashMap<>();
        result.put("trend", trend);
        result.put("lastAction", randomAction);
        return Result.success(result);
    }

    // ==================== 公共方法 ====================
    private List<Map<String, Object>> getBehaviorTrendData(int minutes) {
        String sql = "SELECT DATE_FORMAT(create_time, '%Y-%m-%d %H:%i') as minute, COUNT(*) as cnt " +
                "FROM user_behavior_log " +
                "WHERE create_time >= DATE_SUB(NOW(), INTERVAL ? MINUTE) " +
                "GROUP BY DATE_FORMAT(create_time, '%Y%m%d%H%i') " +
                "ORDER BY minute ASC";
        List<Map<String, Object>> rows;
        try {
            rows = jdbcTemplate.queryForList(sql, minutes);
        } catch (Exception e) {
            rows = new ArrayList<>();
        }
        if (rows.isEmpty()) {
            rows = generateMockBehaviorTrend(minutes);
        }
        return rows.stream().map(row -> {
            Map<String, Object> item = new HashMap<>();
            String minuteStr = (String) row.get("minute");
            if (minuteStr != null && minuteStr.length() >= 16) {
                minuteStr = minuteStr.substring(11, 16); // 只保留 HH:MM
            }
            item.put("minute", minuteStr);
            Object cntObj = row.get("cnt");
            item.put("cnt", cntObj instanceof Number ? ((Number) cntObj).intValue() : 0);
            return item;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> generateMockBehaviorTrend(int minutes) {
        List<Map<String, Object>> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = minutes; i > 0; i--) {
            LocalDateTime point = now.minusMinutes(i);
            String minuteStr = point.format(DateTimeFormatter.ofPattern("HH:mm"));
            Map<String, Object> item = new HashMap<>();
            item.put("minute", minuteStr);
            item.put("cnt", (int)(Math.random() * 20) + 1);
            list.add(item);
        }
        return list;
    }

    @GetMapping("/funnel-analysis")
    public Result<Map<String, Object>> getFunnelAnalysis(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // 默认最近30天
        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        String startDateTime = startDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDateTime = endDate.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 统计各阶段的独立用户数
        String sqlBrowse = "SELECT COUNT(DISTINCT user_id) FROM user_behavior_log WHERE action = '浏览商品' AND create_time BETWEEN ? AND ?";
        String sqlCart = "SELECT COUNT(DISTINCT user_id) FROM user_behavior_log WHERE action = '加入购物车' AND create_time BETWEEN ? AND ?";
        String sqlOrder = "SELECT COUNT(DISTINCT user_id) FROM user_behavior_log WHERE action = '生成订单' AND create_time BETWEEN ? AND ?";
        String sqlPay = "SELECT COUNT(DISTINCT user_id) FROM user_behavior_log WHERE action = '支付成功' AND create_time BETWEEN ? AND ?";

        Long browseUsers = jdbcTemplate.queryForObject(sqlBrowse, Long.class, startDateTime, endDateTime);
        Long cartUsers = jdbcTemplate.queryForObject(sqlCart, Long.class, startDateTime, endDateTime);
        Long orderUsers = jdbcTemplate.queryForObject(sqlOrder, Long.class, startDateTime, endDateTime);
        Long payUsers = jdbcTemplate.queryForObject(sqlPay, Long.class, startDateTime, endDateTime);

        // 处理 null
        browseUsers = browseUsers == null ? 0L : browseUsers;
        cartUsers = cartUsers == null ? 0L : cartUsers;
        orderUsers = orderUsers == null ? 0L : orderUsers;
        payUsers = payUsers == null ? 0L : payUsers;

        // 构建漏斗数据
        List<Map<String, Object>> steps = new ArrayList<>();
        double base = browseUsers > 0 ? browseUsers : 1;
        steps.add(Map.of("name", "浏览商品", "count", browseUsers, "rate", 100.0));
        steps.add(Map.of("name", "加入购物车", "count", cartUsers, "rate", Math.round(cartUsers * 100.0 / base * 10) / 10.0));
        steps.add(Map.of("name", "生成订单", "count", orderUsers, "rate", Math.round(orderUsers * 100.0 / base * 10) / 10.0));
        steps.add(Map.of("name", "支付成功", "count", payUsers, "rate", Math.round(payUsers * 100.0 / base * 10) / 10.0));

        Map<String, Object> result = new HashMap<>();
        result.put("steps", steps);
        return Result.success(result);
    }
    @GetMapping("/rfm-analysis")
    public Result<Map<String, Object>> getRfmAnalysis() {
        // 1. 获取所有用户的基本订单统计（仅考虑已完成/已支付订单，状态1,2,3）
        String sql = "SELECT " +
                "    u.id as userId, " +
                "    u.username, " +
                "    MAX(o.create_time) as lastOrderTime, " +
                "    COUNT(o.id) as orderCount, " +
                "    COALESCE(SUM(o.total_amount), 0) as totalAmount " +
                "FROM tb_user u " +
                "LEFT JOIN tb_order o ON u.id = o.user_id AND o.status IN (1,2,3) " +
                "GROUP BY u.id, u.username";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        // 计算最大值用于归一化评分
        long maxRecency = 0; // 最大R值（离今天最远的天数）
        int maxFrequency = 0;
        BigDecimal maxMonetary = BigDecimal.ZERO;

        // 先收集数据并计算最大值
        List<UserRfm> userList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Map<String, Object> row : rows) {
            Long userId = ((Number) row.get("userId")).longValue();
            String username = (String) row.get("username");
            LocalDateTime lastOrderTime = (LocalDateTime) row.get("lastOrderTime");
            Long orderCount = row.get("orderCount") != null ? ((Number) row.get("orderCount")).longValue() : 0L;
            BigDecimal totalAmount = row.get("totalAmount") != null ? (BigDecimal) row.get("totalAmount") : BigDecimal.ZERO;

            long recency = (lastOrderTime == null) ? 365 : today.toEpochDay() - lastOrderTime.toLocalDate().toEpochDay();
            recency = Math.max(0, recency); // 确保非负

            userList.add(new UserRfm(userId, username, recency, orderCount.intValue(), totalAmount));

            maxRecency = Math.max(maxRecency, recency);
            maxFrequency = Math.max(maxFrequency, orderCount.intValue());
            if (totalAmount.compareTo(maxMonetary) > 0) maxMonetary = totalAmount;
        }

        // 归一化并评分（1~5分，分数越高越好，注意R是反向：天数越少分数越高）
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserRfm u : userList) {
            int rScore = maxRecency == 0 ? 5 : (int) Math.round(5 * (1 - (double) u.recency / maxRecency));
            rScore = Math.min(5, Math.max(1, rScore));
            int fScore = maxFrequency == 0 ? 5 : (int) Math.round(5 * (double) u.frequency / maxFrequency);
            fScore = Math.min(5, Math.max(1, fScore));
            int mScore = maxMonetary.compareTo(BigDecimal.ZERO) == 0 ? 5 :
                    (int) Math.round(5 * u.monetary.doubleValue() / maxMonetary.doubleValue());
            mScore = Math.min(5, Math.max(1, mScore));

            String segment = getSegment(rScore, fScore, mScore);
            Map<String, Object> item = new HashMap<>();
            item.put("userId", u.userId);
            item.put("username", u.username);
            item.put("recency", u.recency);
            item.put("frequency", u.frequency);
            item.put("monetary", u.monetary);
            item.put("rScore", rScore);
            item.put("fScore", fScore);
            item.put("mScore", mScore);
            item.put("segment", segment);
            result.add(item);
        }

        // 统计各分层用户数
        Map<String, Long> segmentCount = result.stream()
                .collect(Collectors.groupingBy(m -> (String) m.get("segment"), Collectors.counting()));

        Map<String, Object> finalResult = new HashMap<>();
        finalResult.put("users", result);
        finalResult.put("segmentCount", segmentCount);
        return Result.success(finalResult);
    }

    // 辅助类
    private static class UserRfm {
        Long userId; String username; long recency; int frequency; BigDecimal monetary;
        UserRfm(Long userId, String username, long recency, int frequency, BigDecimal monetary) {
            this.userId = userId; this.username = username; this.recency = recency;
            this.frequency = frequency; this.monetary = monetary;
        }
    }

    // 根据R、F、M评分确定用户分层（规则可自定义）
    private String getSegment(int r, int f, int m) {
        if (r >= 4 && f >= 4 && m >= 4) return "高价值用户";
        if (r >= 3 && f >= 3 && m >= 3) return "忠诚用户";
        if (r >= 4 && f <= 2 && m >= 4) return "潜力用户";
        if (r >= 4 && f <= 2 && m <= 2) return "新用户";
        if (r <= 2 && f <= 2 && m <= 2) return "流失用户";
        if (r <= 2 && f >= 3 && m >= 3) return "沉睡用户";
        if (r >= 3 && f >= 3 && m <= 2) return "价格敏感型";
        if (r >= 3 && f <= 2 && m >= 4) return "豪客";
        return "一般用户";
    }



    @GetMapping("/sales-prediction")
    public Result<Map<String, Object>> getSalesPrediction(
            @RequestParam(name = "productId", required = false) Long productId,
            @RequestParam(name = "historicalDays", defaultValue = "30") int historicalDays,
            @RequestParam(name = "predictDays", defaultValue = "7") int predictDays) {

        if (productId == null) {
            productId = jdbcTemplate.queryForObject(
                    "SELECT product_id FROM hot_products ORDER BY cnt DESC LIMIT 1", Long.class);
        }

        // 获取历史销量
        String sql = "SELECT DATE(o.create_time) as date, SUM(oi.quantity) as quantity " +
                "FROM tb_order o " +
                "JOIN tb_order_item oi ON o.id = oi.order_id " +
                "WHERE oi.product_id = ? AND o.status IN (1,2,3) " +
                "AND o.create_time >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                "GROUP BY DATE(o.create_time) ORDER BY date ASC";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, productId, historicalDays);

        List<Map<String, Object>> historical = new ArrayList<>();
        List<Double> sales = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            String date = row.get("date").toString();
            Double qty = ((Number) row.get("quantity")).doubleValue();
            historical.add(Map.of("date", date, "quantity", qty));
            sales.add(qty);
        }

        // 移动平均预测
        int window = 7;
        List<Double> predictedSales = new ArrayList<>();
        for (int i = 0; i < predictDays; i++) {
            int start = Math.max(0, sales.size() - window);
            double sum = 0;
            int count = 0;
            for (int j = start; j < sales.size(); j++) {
                sum += sales.get(j);
                count++;
            }
            double avg = count > 0 ? sum / count : 0;
            predictedSales.add(avg);
            sales.add(avg);
        }

        List<Map<String, Object>> predicted = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < predictDays; i++) {
            String futureDate = today.plusDays(i + 1).toString();
            predicted.add(Map.of("date", futureDate, "quantity", predictedSales.get(i)));
        }

        String productName = jdbcTemplate.queryForObject(
                "SELECT name FROM tb_product WHERE id = ?", String.class, productId);

        // 模拟 AI 分析文本
        String aiAdvice = "基于过去30天销售数据的移动平均预测，未来7天该商品销量预计将在 "
                + String.format("%.0f", predictedSales.get(0)) + " 到 "
                + String.format("%.0f", predictedSales.get(predictedSales.size()-1))
                + " 之间波动。建议保持当前库存策略。";

        Map<String, Object> result = new HashMap<>();
        result.put("productId", productId);
        result.put("productName", productName);
        result.put("historical", historical);
        result.put("predicted", predicted);
        result.put("aiAdvice", aiAdvice);
        return Result.success(result);
    }
    }