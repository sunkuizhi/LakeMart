USE lakemart;
SET FOREIGN_KEY_CHECKS = 0;
SET SESSION sql_mode = '';

-- 1. 清空旧数据（谨慎！建议先备份）
TRUNCATE tb_order;
TRUNCATE tb_order_item;
TRUNCATE tb_cart_item;
TRUNCATE tb_points_log;
TRUNCATE user_behavior_log;
TRUNCATE action_distribution;
TRUNCATE behavior_trend;
TRUNCATE hot_products;

-- 2. 生成用户行为日志（浏览、加购、下单、支付、搜索）
DROP PROCEDURE IF EXISTS generate_behavior_log;
DELIMITER $$
CREATE PROCEDURE generate_behavior_log()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE total INT DEFAULT 12000;        -- 总行为数
    DECLARE rand_user_id BIGINT;
    DECLARE rand_action VARCHAR(20);
    DECLARE rand_time DATETIME;
    DECLARE start_date DATE DEFAULT '2026-02-01';
    DECLARE end_date DATE DEFAULT '2026-05-22';

    WHILE i <= total DO
            -- 随机用户（排除 admin 和 role 不是 ROLE_USER 的）
            SELECT id INTO rand_user_id FROM tb_user WHERE role = 'ROLE_USER' ORDER BY RAND() LIMIT 1;
            -- 行为权重：浏览60%，加购20%，下单10%，支付5%，搜索5%
            SET rand_action = CASE FLOOR(1 + RAND()*100)
                                  WHEN <= 60 THEN '浏览商品'
                                  WHEN <= 80 THEN '加入购物车'
                                  WHEN <= 90 THEN '生成订单'
                                  WHEN <= 95 THEN '支付成功'
                                  ELSE '搜索'
                END;
            -- 时间偏向最近60天
            SET rand_time = DATE_ADD(start_date, INTERVAL FLOOR(POW(RAND(),1.5) * DATEDIFF(end_date, start_date)) DAY)
                + INTERVAL FLOOR(RAND() * 86400) SECOND;
            INSERT INTO user_behavior_log (user_id, action, create_time)
            VALUES (rand_user_id, rand_action, rand_time);
            SET i = i + 1;
        END WHILE;
END$$
DELIMITER ;
CALL generate_behavior_log();
DROP PROCEDURE generate_behavior_log;

-- 3. 生成购物车记录（包含最终未下单的）
INSERT INTO tb_cart_item (user_id, product_id, quantity, create_time)
SELECT
    u.id,
    p.id,
    1 + FLOOR(RAND() * 3) AS quantity,
    DATE_ADD('2026-02-01', INTERVAL FLOOR(RAND() * 110) DAY) + INTERVAL FLOOR(RAND() * 86400) SECOND AS create_time
FROM tb_user u
         CROSS JOIN (SELECT id FROM tb_product ORDER BY RAND() LIMIT 1500) p
WHERE u.role = 'ROLE_USER'
  AND RAND() < 0.2   -- 每个用户约20%的商品进入购物车
ORDER BY RAND()
LIMIT 1200;

-- 4. 生成订单和订单项（核心）
DROP PROCEDURE IF EXISTS generate_orders;
DELIMITER $$
CREATE PROCEDURE generate_orders()
BEGIN
    DECLARE order_count INT DEFAULT 2400;
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
    DECLARE start_date DATE DEFAULT '2026-02-01';
    DECLARE end_date DATE DEFAULT '2026-05-22';

    WHILE i <= order_count DO
            SELECT id INTO rand_user_id FROM tb_user WHERE role = 'ROLE_USER' ORDER BY RAND() LIMIT 1;
            -- 状态权重：完成50%，已支付20%，已发货15%，待支付10%，取消5%
            SET rand_status = CASE FLOOR(1 + RAND()*100)
                                  WHEN <= 50 THEN 3
                                  WHEN <= 70 THEN 1
                                  WHEN <= 85 THEN 2
                                  WHEN <= 95 THEN 0
                                  ELSE 4
                END;
            -- 下单时间，越近概率越高
            SET rand_create_time = DATE_ADD(start_date, INTERVAL FLOOR(POW(RAND(),1.2) * DATEDIFF(end_date, start_date)) DAY)
                + INTERVAL FLOOR(RAND() * 86400) SECOND;

            SET rand_pay_time = NULL;
            SET rand_delivery_time = NULL;
            SET rand_complete_time = NULL;
            IF rand_status IN (1,2,3) THEN
                SET rand_pay_time = DATE_ADD(rand_create_time, INTERVAL FLOOR(RAND() * 2) HOUR);
                IF rand_status IN (2,3) THEN
                    SET rand_delivery_time = DATE_ADD(rand_pay_time, INTERVAL FLOOR(1 + RAND() * 5) DAY);
                    IF rand_status = 3 THEN
                        SET rand_complete_time = DATE_ADD(rand_delivery_time, INTERVAL FLOOR(1 + RAND() * 3) DAY);
                    END IF;
                END IF;
            END IF;

            -- 插入订单（先用临时金额0）
            INSERT INTO tb_order (order_no, user_id, total_amount, status, pay_time, delivery_time, complete_time, create_time,
                                  receiver_name, receiver_phone, receiver_address)
            VALUES (
                       CONCAT('ORD', UNIX_TIMESTAMP(rand_create_time), FLOOR(RAND()*10000)),
                       rand_user_id,
                       0,
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

            -- 订单项数量：1~5个
            SET item_count = 1 + FLOOR(RAND() * 5);
            SET sum_amount = 0;

            -- 随机取商品（允许重复，但实际中很少重复，这里为了简单允许重复，但使用去重临时表）
            DROP TEMPORARY TABLE IF EXISTS tmp_prods;
            CREATE TEMPORARY TABLE tmp_prods (pid BIGINT PRIMARY KEY);
            INSERT INTO tmp_prods SELECT id FROM tb_product ORDER BY RAND() LIMIT item_count;

            BEGIN
                DECLARE done INT DEFAULT FALSE;
                DECLARE cur CURSOR FOR SELECT pid FROM tmp_prods;
                DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
                OPEN cur;
                read_loop: LOOP
                    FETCH cur INTO rand_product_id;
                    IF done THEN LEAVE read_loop; END IF;
                    SELECT price, name INTO rand_price, product_name_var FROM tb_product WHERE id = rand_product_id;
                    SET rand_quantity = 1 + FLOOR(RAND() * 4);
                    SET sum_amount = sum_amount + rand_price * rand_quantity;
                    INSERT INTO tb_order_item (order_id, product_id, product_name, price, quantity, create_time)
                    VALUES (order_id, rand_product_id, product_name_var, rand_price, rand_quantity, rand_create_time);
                END LOOP;
                CLOSE cur;
            END;
            DROP TEMPORARY TABLE tmp_prods;

            UPDATE tb_order SET total_amount = sum_amount WHERE id = order_id;

            SET i = i + 1;
        END WHILE;
END$$
DELIMITER ;
CALL generate_orders();
DROP PROCEDURE generate_orders;

-- 5. 生成积分日志（基于已完成/已支付订单，增加积分，部分抵扣）
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

-- 随机将部分积分日志改为抵扣（points_change 负数）
UPDATE tb_points_log
SET points_change = -1 * FLOOR(RAND() * 300),
    balance = balance - points_change,
    type = '积分抵扣',
    remark = '订单支付使用积分'
WHERE type = 'ORDER_CREATE'
  AND RAND() < 0.2
  AND points_change > 0;

-- 重新计算每个用户的积分余额（按时间顺序）
DROP PROCEDURE IF EXISTS recalc_points;
DELIMITER $$
CREATE PROCEDURE recalc_points()
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
CALL recalc_points();
DROP PROCEDURE recalc_points;

-- 更新 tb_user 表中的 points 字段
UPDATE tb_user u SET points = (SELECT IFNULL(MAX(balance),0) FROM tb_points_log WHERE user_id = u.id);

-- 6. 刷新聚合表（供图表使用）
TRUNCATE action_distribution;
INSERT INTO action_distribution (action, cnt, update_time)
SELECT action, COUNT(*), NOW()
FROM user_behavior_log
GROUP BY action;

TRUNCATE behavior_trend;
INSERT INTO behavior_trend (minute, cnt, update_time)
SELECT
    DATE_FORMAT(create_time, '%Y-%m-%d %H:%i:00') AS minute,
    COUNT(*),
    NOW()
FROM user_behavior_log
WHERE create_time >= DATE_SUB(NOW(), INTERVAL 60 MINUTE)
GROUP BY minute
ORDER BY minute;

TRUNCATE hot_products;
INSERT INTO hot_products (product_id, cnt, update_time)
SELECT
    oi.product_id,
    SUM(oi.quantity) AS total_sold,
    NOW()
FROM tb_order o
         JOIN tb_order_item oi ON o.id = oi.order_id
WHERE o.status IN (1,2,3)
GROUP BY oi.product_id
ORDER BY total_sold DESC
LIMIT 50;

SET FOREIGN_KEY_CHECKS = 1;