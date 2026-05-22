package org.lzx.lakemart.controller;

import org.lzx.lakemart.model.dto.OrderStatisticsDTO;
import org.lzx.lakemart.model.vo.DailyAmountVO;
import org.lzx.lakemart.model.vo.ProductSalesVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.CategoryService;
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
    private CategoryService categoryService;

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
    public Result<List<ProductSalesVO>> getHotProducts(
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        String sql = "SELECT oi.product_id, p.name as product_name, SUM(oi.quantity) as cnt " +
                "FROM tb_order o " +
                "JOIN tb_order_item oi ON o.id = oi.order_id " +
                "LEFT JOIN tb_product p ON oi.product_id = p.id " +
                "WHERE o.status IN (1,2,3) " +
                "AND DATE(o.create_time) BETWEEN ? AND ? " +
                "GROUP BY oi.product_id, p.name " +
                "ORDER BY cnt DESC LIMIT ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, startDate, endDate, limit);
        List<ProductSalesVO> result = rows.stream().map(row -> {
            Long productId = ((Number) row.get("product_id")).longValue();
            String productName = (String) row.get("product_name");
            Integer cnt = ((Number) row.get("cnt")).intValue();
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
    public Result<List<Map<String, Object>>> getActionDistribution(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        String sql = "SELECT action, COUNT(*) as cnt FROM user_behavior_log " +
                "WHERE DATE(create_time) BETWEEN ? AND ? " +
                "GROUP BY action ORDER BY cnt DESC";
        List<Map<String, Object>> rows;
        try {
            rows = jdbcTemplate.queryForList(sql, startDate, endDate);
        } catch (Exception e) {
            rows = new ArrayList<>();
        }

        // 标准化行为名称
        Map<String, Integer> merged = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            String action = (String) row.get("action");
            Integer cnt = ((Number) row.get("cnt")).intValue();
            if (action == null) continue;
            String normalized = action.trim();
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
        String[] actions = {"浏览商品", "加入购物车", "生成订单", "支付成功", "搜索"};
        String randomAction = actions[new Random().nextInt(actions.length)];
        Long randomUserId = (long) (new Random().nextInt(16) + 1);
        String insertSql = "INSERT INTO user_behavior_log (user_id, action, create_time) VALUES (?, ?, NOW())";
        jdbcTemplate.update(insertSql, randomUserId, randomAction);
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
                minuteStr = minuteStr.substring(11, 16);
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
        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        String startDateTime = startDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDateTime = endDate.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String sqlBrowse = "SELECT COUNT(DISTINCT user_id) FROM user_behavior_log WHERE action = '浏览商品' AND create_time BETWEEN ? AND ?";
        String sqlCart = "SELECT COUNT(DISTINCT user_id) FROM user_behavior_log WHERE action = '加入购物车' AND create_time BETWEEN ? AND ?";
        String sqlOrder = "SELECT COUNT(DISTINCT user_id) FROM user_behavior_log WHERE action = '生成订单' AND create_time BETWEEN ? AND ?";
        String sqlPay = "SELECT COUNT(DISTINCT user_id) FROM user_behavior_log WHERE action = '支付成功' AND create_time BETWEEN ? AND ?";

        Long browseUsers = jdbcTemplate.queryForObject(sqlBrowse, Long.class, startDateTime, endDateTime);
        Long cartUsers = jdbcTemplate.queryForObject(sqlCart, Long.class, startDateTime, endDateTime);
        Long orderUsers = jdbcTemplate.queryForObject(sqlOrder, Long.class, startDateTime, endDateTime);
        Long payUsers = jdbcTemplate.queryForObject(sqlPay, Long.class, startDateTime, endDateTime);

        browseUsers = browseUsers == null ? 0L : browseUsers;
        cartUsers = cartUsers == null ? 0L : cartUsers;
        orderUsers = orderUsers == null ? 0L : orderUsers;
        payUsers = payUsers == null ? 0L : payUsers;

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
    public Result<Map<String, Object>> getRfmAnalysis(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null) startDate = LocalDate.now().minusDays(90);
        if (endDate == null) endDate = LocalDate.now();

        String sql = "SELECT u.id as userId, u.username, MAX(o.create_time) as lastOrderTime, " +
                "COUNT(o.id) as orderCount, COALESCE(SUM(o.total_amount), 0) as totalAmount " +
                "FROM tb_user u " +
                "LEFT JOIN tb_order o ON u.id = o.user_id AND o.status IN (1,2,3) " +
                "AND DATE(o.create_time) BETWEEN ? AND ? " +
                "GROUP BY u.id, u.username";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, startDate, endDate);

        long maxRecency = 0;
        int maxFrequency = 0;
        BigDecimal maxMonetary = BigDecimal.ZERO;

        List<UserRfm> userList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Map<String, Object> row : rows) {
            Long userId = ((Number) row.get("userId")).longValue();
            String username = (String) row.get("username");
            LocalDateTime lastOrderTime = (LocalDateTime) row.get("lastOrderTime");
            Long orderCount = row.get("orderCount") != null ? ((Number) row.get("orderCount")).longValue() : 0L;
            BigDecimal totalAmount = row.get("totalAmount") != null ? (BigDecimal) row.get("totalAmount") : BigDecimal.ZERO;

            long recency = (lastOrderTime == null) ? 365 : today.toEpochDay() - lastOrderTime.toLocalDate().toEpochDay();
            recency = Math.max(0, recency);

            userList.add(new UserRfm(userId, username, recency, orderCount.intValue(), totalAmount));

            maxRecency = Math.max(maxRecency, recency);
            maxFrequency = Math.max(maxFrequency, orderCount.intValue());
            if (totalAmount.compareTo(maxMonetary) > 0) maxMonetary = totalAmount;
        }

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

        Map<String, Long> segmentCount = result.stream()
                .collect(Collectors.groupingBy(m -> (String) m.get("segment"), Collectors.counting()));

        Map<String, Object> finalResult = new HashMap<>();
        finalResult.put("users", result);
        finalResult.put("segmentCount", segmentCount);
        return Result.success(finalResult);
    }

    private static class UserRfm {
        Long userId; String username; long recency; int frequency; BigDecimal monetary;
        UserRfm(Long userId, String username, long recency, int frequency, BigDecimal monetary) {
            this.userId = userId; this.username = username; this.recency = recency;
            this.frequency = frequency; this.monetary = monetary;
        }
    }

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

    // ==================== 销量预测接口 ====================
    @GetMapping("/sales-prediction")
    public Result<Map<String, Object>> getSalesPrediction(
            @RequestParam(name = "productId", required = false) Long productId,
            @RequestParam(name = "historicalDays", defaultValue = "30") int historicalDays,
            @RequestParam(name = "predictDays", defaultValue = "7") int predictDays,
            @RequestParam(name = "method", defaultValue = "simple") String method) {

        if (productId == null) {
            productId = jdbcTemplate.queryForObject(
                    "SELECT product_id FROM hot_products ORDER BY cnt DESC LIMIT 1", Long.class);
        }

        String productName = jdbcTemplate.queryForObject(
                "SELECT name FROM tb_product WHERE id = ?", String.class, productId);

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

        boolean isMock = false;
        if (sales.isEmpty()) {
            isMock = true;
            LocalDate today = LocalDate.now();
            for (int i = historicalDays; i > 0; i--) {
                LocalDate date = today.minusDays(i);
                double simulatedQty = 5 + Math.random() * 25;
                historical.add(Map.of("date", date.toString(), "quantity", simulatedQty));
                sales.add(simulatedQty);
            }
        }

        // 根据方法预测
        List<Double> predictedSales;
        switch (method.toLowerCase()) {
            case "weighted":
                predictedSales = weightedMovingAverage(new ArrayList<>(sales), predictDays);
                break;
            case "exponential":
                predictedSales = exponentialSmoothing(new ArrayList<>(sales), predictDays);
                break;
            default:
                predictedSales = simpleMovingAverage(new ArrayList<>(sales), predictDays);
        }

        List<Map<String, Object>> predicted = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < predictDays; i++) {
            String futureDate = today.plusDays(i + 1).toString();
            predicted.add(Map.of("date", futureDate, "quantity", predictedSales.get(i)));
        }

        String aiAdvice;
        if (isMock) {
            aiAdvice = "⚠️ 当前暂无真实历史销量数据，图表为随机生成的模拟曲线（仅用于预览）。请确保最近30天有订单产生，或手动插入测试订单数据。";
        } else {
            aiAdvice = String.format("基于过去%d天销售数据，使用%s算法预测，未来%d天销量预计将在 %.0f 到 %.0f 之间波动。建议保持当前库存策略。",
                    historicalDays, getMethodName(method), predictDays, predictedSales.get(0), predictedSales.get(predictedSales.size()-1));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("productId", productId);
        result.put("productName", productName);
        result.put("historical", historical);
        result.put("predicted", predicted);
        result.put("aiAdvice", aiAdvice);
        return Result.success(result);
    }

    // 简单移动平均
    private List<Double> simpleMovingAverage(List<Double> sales, int predictDays) {
        List<Double> predictions = new ArrayList<>();
        int window = 7;
        for (int i = 0; i < predictDays; i++) {
            int start = Math.max(0, sales.size() - window);
            double sum = 0;
            int count = 0;
            for (int j = start; j < sales.size(); j++) {
                sum += sales.get(j);
                count++;
            }
            double avg = count > 0 ? sum / count : 0;
            predictions.add(avg);
            sales.add(avg);
        }
        return predictions;
    }

    // 加权移动平均（近7天，权重线性递增）
    private List<Double> weightedMovingAverage(List<Double> sales, int predictDays) {
        List<Double> predictions = new ArrayList<>();
        int window = 7;
        double[] weights = {1, 2, 3, 4, 5, 6, 7};
        double weightSum = 28;
        for (int i = 0; i < predictDays; i++) {
            int start = Math.max(0, sales.size() - window);
            double sum = 0;
            int count = 0;
            for (int j = start; j < sales.size(); j++) {
                int idx = j - start;
                sum += sales.get(j) * weights[idx];
                count++;
            }
            double wma = count > 0 ? sum / weightSum : 0;
            predictions.add(wma);
            sales.add(wma);
        }
        return predictions;
    }

    // 指数平滑（单指数平滑，α=0.3）
    private List<Double> exponentialSmoothing(List<Double> sales, int predictDays) {
        List<Double> predictions = new ArrayList<>();
        double alpha = 0.3;
        double lastSmoothed = sales.isEmpty() ? 0 : sales.get(0);
        for (int i = 1; i < sales.size(); i++) {
            lastSmoothed = alpha * sales.get(i) + (1 - alpha) * lastSmoothed;
        }
        for (int i = 0; i < predictDays; i++) {
            predictions.add(lastSmoothed);
            sales.add(lastSmoothed);
        }
        return predictions;
    }

    private String getMethodName(String method) {
        switch (method) {
            case "weighted": return "加权移动平均";
            case "exponential": return "指数平滑";
            default: return "简单移动平均";
        }
    }
    @GetMapping("/products/by-category")
    public Result<List<Map<String, Object>>> getProductsByCategory(
            @RequestParam(name = "categoryId", required = false) Long categoryId) {
        String sql;
        List<Long> categoryIds;
        if (categoryId != null) {
            categoryIds = getAllSubCategoryIds(categoryId);
            if (categoryIds.isEmpty()) {
                return Result.success(List.of());
            }
            String placeholders = String.join(",", Collections.nCopies(categoryIds.size(), "?"));
            sql = "SELECT id, name FROM tb_product WHERE status = 1 AND category_id IN (" + placeholders + ") ORDER BY name";
            return Result.success(jdbcTemplate.queryForList(sql, categoryIds.toArray()));
        } else {
            sql = "SELECT id, name FROM tb_product WHERE status = 1 ORDER BY name LIMIT 200";
            return Result.success(jdbcTemplate.queryForList(sql));
        }
    }

    private List<Long> getAllSubCategoryIds(Long parentId) {
        return categoryService.getAllSubCategoryIds(parentId);
    }

    @GetMapping("/product-sales-trend/{productId}")
    public Result<List<Map<String, Object>>> getProductSalesTrend(
            @PathVariable("productId") Long productId,
            @RequestParam(name = "days", defaultValue = "30") int days) {

        String sql = "SELECT DATE(o.create_time) as date, SUM(oi.quantity) as quantity " +
                "FROM tb_order o " +
                "JOIN tb_order_item oi ON o.id = oi.order_id " +
                "WHERE oi.product_id = ? AND o.status IN (1,2,3) " +
                "AND o.create_time >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                "GROUP BY DATE(o.create_time) ORDER BY date ASC";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, productId, days);

        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate startDate = LocalDate.now().minusDays(days - 1);
        LocalDate endDate = LocalDate.now();

        Map<String, Integer> salesMap = new HashMap<>();
        for (Map<String, Object> row : rows) {
            String date = row.get("date").toString();
            Integer qty = ((Number) row.get("quantity")).intValue();
            salesMap.put(date, qty);
        }

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String dateStr = date.toString();
            int qty = salesMap.getOrDefault(dateStr, 0);
            result.add(Map.of("date", dateStr, "quantity", qty));
        }

        return Result.success(result);
    }


}