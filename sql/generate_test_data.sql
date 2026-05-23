# 清空旧数据
TRUNCATE tb_order;
TRUNCATE tb_order_item;
TRUNCATE tb_cart_item;
TRUNCATE tb_points_log;
TRUNCATE user_behavior_log;
# 模拟数据脚本
USE lakemart;
SET FOREIGN_KEY_CHECKS = 0;
SET @old_sql_mode = @@sql_mode;
SET SESSION sql_mode = '';

-- ----------------------------
-- 1. 生成用户行为日志 (user_behavior_log)
-- ----------------------------
-- 生成过去 90 天内的随机用户行为，总数约 5000 条
DROP PROCEDURE IF EXISTS generate_behavior_log;
DELIMITER $$
CREATE PROCEDURE generate_behavior_log()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE total INT DEFAULT 5000;
    DECLARE rand_user_id BIGINT;
    DECLARE rand_action VARCHAR(20);
    DECLARE rand_time DATETIME;
    DECLARE start_date DATE DEFAULT DATE_SUB(CURDATE(), INTERVAL 90 DAY);
    DECLARE end_date DATE DEFAULT CURDATE();

    WHILE i <= total DO
            -- 随机用户（排除 admin 和基础用户，只选 ROLE_USER）
            SELECT id INTO rand_user_id FROM tb_user WHERE role = 'ROLE_USER' ORDER BY RAND() LIMIT 1;
            -- 随机行为，权重：VIEW_PRODUCT 70%，ADD_CART 20%，BUY 10%
            SET rand_action = CASE FLOOR(1 + RAND() * 100)
                                  WHEN <= 70 THEN 'VIEW_PRODUCT'
                                  WHEN <= 90 THEN 'ADD_CART'
                                  ELSE 'BUY'
                END;
            -- 随机时间，偏向后 60 天概率更高
            SET rand_time = DATE_ADD(start_date, INTERVAL FLOOR(RAND() * 90) DAY)
                + INTERVAL FLOOR(RAND() * 86400) SECOND;
            INSERT INTO user_behavior_log (user_id, action, create_time)
            VALUES (rand_user_id, rand_action, rand_time);
            SET i = i + 1;
        END WHILE;
END$$
DELIMITER ;
CALL generate_behavior_log();
DROP PROCEDURE generate_behavior_log;

-- ----------------------------
-- 2. 生成购物车记录 (tb_cart_item)
-- 包含最终下单的和未下单的（放弃购物车）
-- ----------------------------
TRUNCATE tb_cart_item;
INSERT INTO tb_cart_item (user_id, product_id, quantity, create_time)
SELECT
    u.id,
    p.id,
    1 + FLOOR(RAND() * 3) AS quantity,
    DATE_ADD(DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 90) DAY), INTERVAL FLOOR(RAND() * 86400) SECOND) AS create_time
FROM tb_user u
         CROSS JOIN (SELECT id FROM tb_product ORDER BY RAND() LIMIT 2000) p
WHERE u.role = 'ROLE_USER'
  AND RAND() < 0.3   -- 每个用户约 30% 的商品会进入购物车
ORDER BY RAND()
LIMIT 800;   -- 总共 800 条购物车记录

-- ----------------------------
-- 3. 生成订单 (tb_order) 和 订单项 (tb_order_item)
-- ----------------------------
DROP PROCEDURE IF EXISTS generate_orders;
DELIMITER $$
CREATE PROCEDURE generate_orders()
BEGIN
    DECLARE order_count INT DEFAULT 900;      -- 生成 900 个订单
    DECLARE i INT DEFAULT 1;
    DECLARE rand_user_id BIGINT;
    DECLARE rand_total DECIMAL(10,2);
    DECLARE rand_status TINYINT;
    DECLARE rand_pay_time DATETIME;
    DECLARE rand_delivery_time DATETIME;
    DECLARE rand_complete_time DATETIME;
    DECLARE rand_create_time DATETIME;
    DECLARE order_id BIGINT;
    DECLARE item_count INT;
    DECLARE j INT;
    DECLARE rand_product_id BIGINT;
    DECLARE rand_price DECIMAL(10,2);
    DECLARE rand_quantity INT;
    DECLARE sum_amount DECIMAL(10,2);
    DECLARE product_name_var VARCHAR(200);

    DECLARE done INT DEFAULT FALSE;
    DECLARE cur_products CURSOR FOR SELECT id, name, price FROM tb_product ORDER BY RAND();
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    WHILE i <= order_count DO
            -- 随机用户
            SELECT id INTO rand_user_id FROM tb_user WHERE role = 'ROLE_USER' ORDER BY RAND() LIMIT 1;
            -- 随机状态（按真实比例：完成60%，已支付15%，已发货10%，待支付10%，取消5%）
            SET rand_status = CASE FLOOR(1 + RAND() * 100)
                                  WHEN <= 60 THEN 3      -- 已完成
                                  WHEN <= 75 THEN 1      -- 已支付
                                  WHEN <= 85 THEN 2      -- 已发货
                                  WHEN <= 95 THEN 0      -- 待支付
                                  ELSE 4                 -- 已取消
                END;
            -- 下单时间：过去 90 天内，越近概率越高（指数偏好用平方）
            SET rand_create_time = DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 90 DAY),
                                            INTERVAL FLOOR(POW(RAND(), 1.5) * 90) DAY)
                + INTERVAL FLOOR(RAND() * 86400) SECOND;

            -- 支付、发货、完成时间根据状态设置
            SET rand_pay_time = NULL;
            SET rand_delivery_time = NULL;
            SET rand_complete_time = NULL;
            IF rand_status IN (1,2,3) THEN
                SET rand_pay_time = DATE_ADD(rand_create_time, INTERVAL FLOOR(RAND() * 3) HOUR);
                IF rand_status IN (2,3) THEN
                    SET rand_delivery_time = DATE_ADD(rand_pay_time, INTERVAL FLOOR(1 + RAND() * 5) DAY);
                    IF rand_status = 3 THEN
                        SET rand_complete_time = DATE_ADD(rand_delivery_time, INTERVAL FLOOR(1 + RAND() * 3) DAY);
                    END IF;
                END IF;
            END IF;

            -- 插入订单先获取 ID
            INSERT INTO tb_order (order_no, user_id, total_amount, status, pay_time, delivery_time, complete_time, create_time,
                                  receiver_name, receiver_phone, receiver_address)
            VALUES (
                       CONCAT('ORD', UNIX_TIMESTAMP(rand_create_time), FLOOR(RAND()*10000)),
                       rand_user_id,
                       0,   -- 临时金额，后面更新
                       rand_status,
                       rand_pay_time,
                       rand_delivery_time,
                       rand_complete_time,
                       rand_create_time,
                       (SELECT receiver_name FROM tb_address WHERE user_id = rand_user_id ORDER BY is_default DESC LIMIT 1),
                       (SELECT receiver_phone FROM tb_address WHERE user_id = rand_user_id ORDER BY is_default DESC LIMIT 1),
                       (SELECT CONCAT(province, city, district, detail_address) FROM tb_address WHERE user_id = rand_user_id ORDER BY is_default DESC LIMIT 1)
                   );
            SET order_id = LAST_INSERT_ID();

            -- 随机 1~5 个商品作为订单项
            SET item_count = 1 + FLOOR(RAND() * 5);
            SET sum_amount = 0;
            -- 使用游标随机取商品，避免重复（简单版允许重复但用相同 id 会冲突，所以使用临时表去重）
            DROP TEMPORARY TABLE IF EXISTS tmp_order_products;
            CREATE TEMPORARY TABLE tmp_order_products (pid BIGINT PRIMARY KEY);
            INSERT INTO tmp_order_products SELECT id FROM tb_product ORDER BY RAND() LIMIT item_count;

            BEGIN
                DECLARE cur CURSOR FOR SELECT pid FROM tmp_order_products;
                OPEN cur;
                read_loop: LOOP
                    FETCH cur INTO rand_product_id;
                    IF done THEN LEAVE read_loop; END IF;
                    SELECT price, name INTO rand_price, product_name_var FROM tb_product WHERE id = rand_product_id;
                    SET rand_quantity = 1 + FLOOR(RAND() * 3);
                    SET sum_amount = sum_amount + rand_price * rand_quantity;
                    INSERT INTO tb_order_item (order_id, product_id, product_name, price, quantity, create_time)
                    VALUES (order_id, rand_product_id, product_name_var, rand_price, rand_quantity, rand_create_time);
                END LOOP;
                CLOSE cur;
            END;
            DROP TEMPORARY TABLE tmp_order_products;

            -- 更新订单总金额
            UPDATE tb_order SET total_amount = sum_amount WHERE id = order_id;

            SET i = i + 1;
        END WHILE;
END$$
DELIMITER ;
CALL generate_orders();
DROP PROCEDURE generate_orders;

-- ----------------------------
-- 4. 生成积分日志 (tb_points_log)
-- 每个已完成/已支付的订单，按订单金额 10% 增加积分（向上取整）
-- 同时随机抽取部分订单使用积分抵扣（points_change 为负）
-- ----------------------------
TRUNCATE tb_points_log;
INSERT INTO tb_points_log (user_id, points_change, balance, type, related_id, remark, create_time)
SELECT
    o.user_id,
    FLOOR(o.total_amount / 10) AS points_add,
    @new_balance := IFNULL((SELECT balance FROM (SELECT balance FROM tb_points_log WHERE user_id = o.user_id ORDER BY id DESC LIMIT 1) AS last), 0) + FLOOR(o.total_amount / 10),
    'ORDER_CREATE',
    o.id,
    CONCAT('订单 ', o.order_no, ' 消费获得积分'),
    o.create_time
FROM tb_order o
WHERE o.status IN (1,2,3);

-- 随机为部分订单增加积分抵扣（points_change 为负）
UPDATE tb_points_log
SET points_change = -1 * FLOOR(RAND() * 300),
    balance = balance - points_change,
    type = '积分抵扣',
    remark = '订单支付使用积分'
WHERE type = 'ORDER_CREATE'
  AND RAND() < 0.25
  AND points_change > 0   -- 避免抵扣金额大于余额（简单处理，实际可更严格）
;

-- 修复余额连续（重新计算每个用户的余额）
DROP PROCEDURE IF EXISTS recalc_points_balance;
DELIMITER $$
CREATE PROCEDURE recalc_points_balance()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_user_id BIGINT;
    DECLARE cur CURSOR FOR SELECT DISTINCT user_id FROM tb_points_log ORDER BY user_id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO v_user_id;
        IF done THEN LEAVE read_loop; END IF;
        SET @balance = 0;
        UPDATE tb_points_log
        SET balance = (@balance := @balance + points_change)
        WHERE user_id = v_user_id
        ORDER BY id;
    END LOOP;
    CLOSE cur;
END$$
DELIMITER ;
CALL recalc_points_balance();
DROP PROCEDURE recalc_points_balance;

-- 最后更新 tb_user 表中的 points 字段
UPDATE tb_user u SET points = (
    SELECT IFNULL(MAX(balance), 0) FROM tb_points_log WHERE user_id = u.id
);

-- ----------------------------
-- 5. 调整订单项中的商品快照价格与商品当前价格一致（可选）
-- ----------------------------
UPDATE tb_order_item oi
    JOIN tb_product p ON oi.product_id = p.id
SET oi.price = p.price
WHERE oi.price != p.price;

-- ----------------------------
-- 6. 清理临时表和重置会话
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 1;
SET SESSION sql_mode = @old_sql_mode;
