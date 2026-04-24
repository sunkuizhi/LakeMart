package org.lzx.lakemart;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3307/lakemart?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static final int DAYS_BACK = 30;            // 生成过去30天订单
    private static final int MAX_ORDERS_PER_DAY = 25;   // 每天最多订单数
    private static final int MAX_ITEMS_PER_ORDER = 4;   // 每个订单最多商品数

    private static final List<String> CATEGORY_NAMES = Arrays.asList(
            "电子产品", "手机", "电脑", "家居生活", "家具", "厨具", "数码配件", "手机壳", "充电宝",
            "图书文娱", "小说", "漫画", "运动户外", "跑步鞋", "瑜伽垫"
    );
    private static final List<String> PRODUCT_NAMES = Arrays.asList(
            "iPhone 15 Pro", "iPhone 15", "华为 Mate60 Pro", "华为 Mate60", "小米14 Ultra",
            "OPPO Find X7", "vivo X100 Pro", "联想拯救者Y9000P", "戴尔XPS 13", "MacBook Pro",
            "小米手环9", "华为FreeBuds Pro 3", "小米电饭煲", "北欧简约沙发", "瑜伽垫", "跑步鞋",
            "三体", "斗罗大陆漫画", "手机壳（透明）", "20000mAh充电宝"
    );
    private static final List<String> USERNAMES = Arrays.asList(
            "alice", "bob", "charlie", "david", "emma", "frank", "grace", "henry", "irene", "jack"
    );

    private static Connection conn;

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        System.out.println("数据库连接成功！开始生成模拟数据...");

        try {
            // 1. 清理旧数据（可选，谨慎使用）
            // clearTables();

            // 2. 插入分类
            Map<String, Long> categoryIdMap = insertCategories();

            // 3. 插入商品
            List<Long> productIds = insertProducts(categoryIdMap);

            // 4. 插入用户（普通用户 + 管理员）
            List<Long> userIds = insertUsers();

            // 5. 生成订单和订单项
            generateOrders(userIds, productIds);

            // 6. 生成积分明细
            generatePointsLogs();

            System.out.println("模拟数据生成完成！");
        } finally {
            if (conn != null) conn.close();
        }
    }

    private static void clearTables() throws SQLException {
        System.out.println("正在清空现有数据...");
        String[] tables = {"tb_points_log", "tb_order_item", "tb_order", "tb_cart_item", "tb_product", "tb_category", "tb_user"};
        for (String table : tables) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
                stmt.execute("TRUNCATE TABLE " + table);
                stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
                System.out.println("已清空 " + table);
            }
        }
    }

    private static Map<String, Long> insertCategories() throws SQLException {
        System.out.println("插入分类...");
        Map<String, Long> idMap = new HashMap<>();
        String sql = "INSERT INTO tb_category (name, parent_id, sort_order, status, create_time) VALUES (?, ?, ?, 1, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // 一级分类
            for (String name : Arrays.asList("电子产品", "家居生活", "数码配件", "图书文娱", "运动户外")) {
                ps.setString(1, name);
                ps.setLong(2, 0L);
                ps.setInt(3, 0);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idMap.put(name, rs.getLong(1));
            }
            // 二级分类（电子产品下）
            long elecId = idMap.get("电子产品");
            for (String name : Arrays.asList("手机", "电脑")) {
                ps.setString(1, name);
                ps.setLong(2, elecId);
                ps.setInt(3, 0);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idMap.put(name, rs.getLong(1));
            }
            // 家居生活下
            long homeId = idMap.get("家居生活");
            for (String name : Arrays.asList("家具", "厨具")) {
                ps.setString(1, name);
                ps.setLong(2, homeId);
                ps.setInt(3, 0);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idMap.put(name, rs.getLong(1));
            }
            // 数码配件下
            long accId = idMap.get("数码配件");
            for (String name : Arrays.asList("手机壳", "充电宝")) {
                ps.setString(1, name);
                ps.setLong(2, accId);
                ps.setInt(3, 0);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idMap.put(name, rs.getLong(1));
            }
            // 图书文娱下
            long bookId = idMap.get("图书文娱");
            for (String name : Arrays.asList("小说", "漫画")) {
                ps.setString(1, name);
                ps.setLong(2, bookId);
                ps.setInt(3, 0);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idMap.put(name, rs.getLong(1));
            }
            // 运动户外下
            long sportId = idMap.get("运动户外");
            for (String name : Arrays.asList("跑步鞋", "瑜伽垫")) {
                ps.setString(1, name);
                ps.setLong(2, sportId);
                ps.setInt(3, 0);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idMap.put(name, rs.getLong(1));
            }
        }
        System.out.println("插入分类完成，共 " + idMap.size() + " 条。");
        return idMap;
    }

    private static List<Long> insertProducts(Map<String, Long> categoryIdMap) throws SQLException {
        System.out.println("插入商品...");
        List<Long> productIds = new ArrayList<>();
        String sql = "INSERT INTO tb_product (name, description, price, stock, category_id, image_url, status, create_time) VALUES (?, ?, ?, ?, ?, ?, 1, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Random rand = ThreadLocalRandom.current();
            // 为每个二级分类生成若干商品
            for (Map.Entry<String, Long> entry : categoryIdMap.entrySet()) {
                String catName = entry.getKey();
                Long catId = entry.getValue();
                // 只为叶子分类（二级）生成商品
                if (catName.equals("手机") || catName.equals("电脑") || catName.equals("家具") || catName.equals("厨具")
                        || catName.equals("手机壳") || catName.equals("充电宝") || catName.equals("小说") || catName.equals("漫画")
                        || catName.equals("跑步鞋") || catName.equals("瑜伽垫")) {
                    int productCount = rand.nextInt(5, 10); // 每个分类5-10个商品
                    for (int i = 0; i < productCount; i++) {
                        String name = PRODUCT_NAMES.get(rand.nextInt(PRODUCT_NAMES.size())) + (i+1);
                        String desc = "高品质" + name;
                        BigDecimal price = BigDecimal.valueOf(rand.nextInt(50, 15000) / 10.0);
                        int stock = rand.nextInt(10, 500);
                        ps.setString(1, name);
                        ps.setString(2, desc);
                        ps.setBigDecimal(3, price);
                        ps.setInt(4, stock);
                        ps.setLong(5, catId);
                        ps.setString(6, "/images/product/" + name.replaceAll(" ", "") + ".jpg");
                        ps.executeUpdate();
                        ResultSet rs = ps.getGeneratedKeys();
                        if (rs.next()) productIds.add(rs.getLong(1));
                    }
                }
            }
        }
        System.out.println("插入商品完成，共 " + productIds.size() + " 条。");
        return productIds;
    }

    private static List<Long> insertUsers() throws SQLException {
        System.out.println("插入用户...");
        List<Long> userIds = new ArrayList<>();
        String sql = "INSERT INTO tb_user (username, password, email, role, points, status, create_time) VALUES (?, ?, ?, ?, ?, 1, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // 管理员
            ps.setString(1, "admin");
            ps.setString(2, "$2a$10$NkM.2hH6Qy5RqX1qYJqY0OeQqXqXqXqXqXqXqXqXqXqXqXqXqX"); // 密码 admin123
            ps.setString(3, "admin@lakemart.com");
            ps.setString(4, "ROLE_ADMIN");
            ps.setInt(5, 0);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) userIds.add(rs.getLong(1));

            // 普通用户
            for (String name : USERNAMES) {
                ps.setString(1, name);
                ps.setString(2, "$2a$10$NkM.2hH6Qy5RqX1qYJqY0OeQqXqXqXqXqXqXqXqXqXqXqXqXqX"); // 密码 12345678
                ps.setString(3, name + "@example.com");
                ps.setString(4, "ROLE_USER");
                ps.setInt(5, 0);
                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                if (rs.next()) userIds.add(rs.getLong(1));
            }
        }
        System.out.println("插入用户完成，共 " + userIds.size() + " 条。");
        return userIds;
    }

    private static void generateOrders(List<Long> userIds, List<Long> productIds) throws SQLException {
        System.out.println("生成订单...");
        Random rand = ThreadLocalRandom.current();
        String orderSql = "INSERT INTO tb_order (order_no, user_id, total_amount, status, pay_time, create_time) VALUES (?, ?, ?, ?, ?, ?)";
        String itemSql = "INSERT INTO tb_order_item (order_id, product_id, product_name, price, quantity) VALUES (?, ?, ?, ?, ?)";

        // 为过去 DAYS_BACK 天生成订单
        for (int i = 0; i < DAYS_BACK; i++) {
            LocalDate orderDate = LocalDate.now().minusDays(i);
            int ordersToday = rand.nextInt(1, MAX_ORDERS_PER_DAY + 1);
            for (int j = 0; j < ordersToday; j++) {
                // 随机用户
                Long userId = userIds.get(rand.nextInt(userIds.size()));
                // 订单状态加权：0待支付 10%, 1已支付 70%, 2已发货 10%, 3已完成 5%, 4已取消 5%
                int status;
                int r = rand.nextInt(100);
                if (r < 10) status = 0;
                else if (r < 80) status = 1;
                else if (r < 90) status = 2;
                else if (r < 95) status = 3;
                else status = 4;

                LocalDateTime createTime = LocalDateTime.of(orderDate, LocalTime.of(rand.nextInt(8, 22), rand.nextInt(0, 60)));
                LocalDateTime payTime = null;
                if (status == 1 || status == 2 || status == 3) {
                    payTime = createTime.plusMinutes(rand.nextInt(5, 30));
                }
                String orderNo = "ORD" + System.currentTimeMillis() + rand.nextInt(10000);

                // 插入订单
                try (PreparedStatement psOrder = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                    psOrder.setString(1, orderNo);
                    psOrder.setLong(2, userId);
                    psOrder.setBigDecimal(3, BigDecimal.ZERO); // 先设0，后面更新
                    psOrder.setInt(4, status);
                    psOrder.setObject(5, payTime);
                    psOrder.setObject(6, createTime);
                    psOrder.executeUpdate();
                    ResultSet rs = psOrder.getGeneratedKeys();
                    if (!rs.next()) continue;
                    long orderId = rs.getLong(1);

                    // 随机商品数量
                    int itemCount = rand.nextInt(1, MAX_ITEMS_PER_ORDER + 1);
                    BigDecimal total = BigDecimal.ZERO;
                    // 避免重复商品（简单去重）
                    Set<Long> usedProducts = new HashSet<>();
                    for (int k = 0; k < itemCount; k++) {
                        Long productId;
                        do {
                            productId = productIds.get(rand.nextInt(productIds.size()));
                        } while (usedProducts.contains(productId));
                        usedProducts.add(productId);
                        // 查询商品信息
                        String productName = "";
                        BigDecimal price = BigDecimal.ZERO;
                        try (PreparedStatement psProd = conn.prepareStatement("SELECT name, price FROM tb_product WHERE id = ?")) {
                            psProd.setLong(1, productId);
                            ResultSet rsProd = psProd.executeQuery();
                            if (rsProd.next()) {
                                productName = rsProd.getString("name");
                                price = rsProd.getBigDecimal("price");
                            }
                        }
                        int quantity = rand.nextInt(1, 4);
                        total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
                        // 插入订单项
                        try (PreparedStatement psItem = conn.prepareStatement(itemSql)) {
                            psItem.setLong(1, orderId);
                            psItem.setLong(2, productId);
                            psItem.setString(3, productName);
                            psItem.setBigDecimal(4, price);
                            psItem.setInt(5, quantity);
                            psItem.executeUpdate();
                        }
                    }
                    // 更新订单总金额
                    try (PreparedStatement psUpdate = conn.prepareStatement("UPDATE tb_order SET total_amount = ? WHERE id = ?")) {
                        psUpdate.setBigDecimal(1, total);
                        psUpdate.setLong(2, orderId);
                        psUpdate.executeUpdate();
                    }
                }
            }
            System.out.println("已生成 " + orderDate + " 的订单");
        }
        System.out.println("订单生成完成");
    }

    private static void generatePointsLogs() throws SQLException {
        System.out.println("生成积分明细...");
        // 查询所有已支付的订单，按用户和支付时间顺序计算积分余额
        String sql = "SELECT o.id as order_id, o.user_id, FLOOR(o.total_amount / 10) as points_change, o.pay_time " +
                "FROM tb_order o WHERE o.status IN (1,2,3) ORDER BY o.user_id, o.pay_time";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            Long lastUserId = null;
            int balance = 0;
            String insertSql = "INSERT INTO tb_points_log (user_id, points_change, balance, type, related_id, remark, create_time) VALUES (?, ?, ?, 'ORDER_CREATE', ?, '', ?)";
            try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                while (rs.next()) {
                    long userId = rs.getLong("user_id");
                    int change = rs.getInt("points_change");
                    long orderId = rs.getLong("order_id");
                    Timestamp payTime = rs.getTimestamp("pay_time");
                    if (lastUserId == null || !lastUserId.equals(userId)) {
                        // 新用户，从0开始
                        balance = 0;
                        lastUserId = userId;
                    }
                    balance += change;
                    psInsert.setLong(1, userId);
                    psInsert.setInt(2, change);
                    psInsert.setInt(3, balance);
                    psInsert.setLong(4, orderId);
                    psInsert.setTimestamp(5, payTime);
                    psInsert.executeUpdate();
                }
            }
        }
        System.out.println("积分明细生成完成");
    }
}