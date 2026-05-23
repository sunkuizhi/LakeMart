USE lakemart;

-- 基础用户
INSERT INTO `tb_user` (`username`, `password`, `email`, `phone`, `role`, `points`, `avatar_url`, `introduction`, `status`, `create_time`) VALUES
                                                                                                                                              ('user', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'user@qq.com', NULL, 'ROLE_USER', 0, NULL, NULL, 1, NOW()),
                                                                                                                                              ('admin', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'admin@qq.com', NULL, 'ROLE_ADMIN', 0, NULL, NULL, 1, NOW());

-- 水浒传人物
INSERT INTO `tb_user` (`username`, `password`, `email`, `phone`, `role`, `points`, `avatar_url`, `introduction`, `status`, `create_time`) VALUES
                                                                                                                                              ('宋江', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'songjiang@qq.com', '13800000001', 'ROLE_USER', 0, NULL, '及时雨宋江', 1, NOW()),
                                                                                                                                              ('卢俊义', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'lujunyi@qq.com', '13800000002', 'ROLE_USER', 0, NULL, '玉麒麟卢俊义', 1, NOW()),
                                                                                                                                              ('吴用', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'wuyong@qq.com', '13800000003', 'ROLE_USER', 0, NULL, '智多星吴用', 1, NOW()),
                                                                                                                                              ('公孙胜', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'gongsunsheng@qq.com', '13800000004', 'ROLE_USER', 0, NULL, '入云龙公孙胜', 1, NOW()),
                                                                                                                                              ('关胜', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'guansheng@qq.com', '13800000005', 'ROLE_USER', 0, NULL, '大刀关胜', 1, NOW()),
                                                                                                                                              ('林冲', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'linchong@qq.com', '13800000006', 'ROLE_USER', 0, NULL, '豹子头林冲', 1, NOW()),
                                                                                                                                              ('秦明', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'qinming@qq.com', '13800000007', 'ROLE_USER', 0, NULL, '霹雳火秦明', 1, NOW()),
                                                                                                                                              ('呼延灼', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'huyanzhuo@qq.com', '13800000008', 'ROLE_USER', 0, NULL, '双鞭呼延灼', 1, NOW()),
                                                                                                                                              ('花荣', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'huarong@qq.com', '13800000009', 'ROLE_USER', 0, NULL, '小李广花荣', 1, NOW()),
                                                                                                                                              ('柴进', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'chaijin@qq.com', '13800000010', 'ROLE_USER', 0, NULL, '小旋风柴进', 1, NOW()),
                                                                                                                                              ('李逵', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'likui@qq.com', '13800000011', 'ROLE_USER', 0, NULL, '黑旋风李逵', 1, NOW()),
                                                                                                                                              ('武松', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'wusong@qq.com', '13800000012', 'ROLE_USER', 0, NULL, '行者武松', 1, NOW()),
                                                                                                                                              ('鲁智深', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'luzhishen@qq.com', '13800000013', 'ROLE_USER', 0, NULL, '花和尚鲁智深', 1, NOW()),
                                                                                                                                              ('燕青', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'yanqing@qq.com', '13800000014', 'ROLE_USER', 0, NULL, '浪子燕青', 1, NOW()),
                                                                                                                                              ('扈三娘', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'husanniang@qq.com', '13800000015', 'ROLE_USER', 0, NULL, '一丈青扈三娘', 1, NOW());

-- 红楼梦人物
INSERT INTO `tb_user` (`username`, `password`, `email`, `phone`, `role`, `points`, `avatar_url`, `introduction`, `status`, `create_time`) VALUES
                                                                                                                                              ('贾宝玉', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'jiabaoyu@qq.com', '13800000016', 'ROLE_USER', 0, NULL, '怡红公子', 1, NOW()),
                                                                                                                                              ('林黛玉', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'lindaiyu@qq.com', '13800000017', 'ROLE_USER', 0, NULL, '潇湘妃子', 1, NOW()),
                                                                                                                                              ('薛宝钗', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'xuebaochai@qq.com', '13800000018', 'ROLE_USER', 0, NULL, '蘅芜君', 1, NOW()),
                                                                                                                                              ('王熙凤', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'wangxifeng@qq.com', '13800000019', 'ROLE_USER', 0, NULL, '凤辣子', 1, NOW()),
                                                                                                                                              ('史湘云', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'shixiangyun@qq.com', '13800000020', 'ROLE_USER', 0, NULL, '枕霞旧友', 1, NOW()),
                                                                                                                                              ('贾探春', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'jiatanchun@qq.com', '13800000021', 'ROLE_USER', 0, NULL, '蕉下客', 1, NOW()),
                                                                                                                                              ('贾惜春', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'jiaxichun@qq.com', '13800000022', 'ROLE_USER', 0, NULL, '藕榭', 1, NOW()),
                                                                                                                                              ('妙玉', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'miaoyu@qq.com', '13800000023', 'ROLE_USER', 0, NULL, '槛外人', 1, NOW()),
                                                                                                                                              ('李纨', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'liwan@qq.com', '13800000024', 'ROLE_USER', 0, NULL, '稻香老农', 1, NOW()),
                                                                                                                                              ('晴雯', '$2a$10$EF2xNdulBaX/DO1ybHkUL.HlFUO8cz6ZYSP/wOeJkAP9FGqo3GB12', 'qingwen@qq.com', '13800000025', 'ROLE_USER', 0, NULL, '勇晴雯', 1, NOW());

-- ==================== 收货地址表 tb_address（修正字段名） ====================
-- 基础用户地址
INSERT INTO `tb_address` (`user_id`, `receiver_name`, `receiver_phone`, `province`, `city`, `district`, `detail_address`, `is_default`, `create_time`) VALUES
                                                                                                                                                           (1, '用户', '13800000000', '北京市', '北京市', '朝阳区', '建国路88号SOHO现代城A座1001室', 1, NOW()),
                                                                                                                                                           (2, '管理员', '13800000000', '上海市', '上海市', '浦东新区', '陆家嘴环路1000号恒生银行大厦25楼', 1, NOW());

-- 水浒传人物地址
INSERT INTO `tb_address` (`user_id`, `receiver_name`, `receiver_phone`, `province`, `city`, `district`, `detail_address`, `is_default`, `create_time`) VALUES
                                                                                                                                                           (3, '宋江', '13800000001', '山东省', '菏泽市', '郓城县', '宋家村宋太公府', 1, NOW()),
                                                                                                                                                           (4, '卢俊义', '13800000002', '河北省', '邯郸市', '大名县', '玉麒麟府东大街1号', 1, NOW()),
                                                                                                                                                           (5, '吴用', '13800000003', '山东省', '菏泽市', '郓城县', '东溪村智多星书院', 1, NOW()),
                                                                                                                                                           (6, '公孙胜', '13800000004', '河北省', '沧州市', '河间市', '九宫县二仙山紫虚观', 1, NOW()),
                                                                                                                                                           (7, '关胜', '13800000005', '山西省', '运城市', '盐湖区', '解州镇关帝庙旁', 1, NOW()),
                                                                                                                                                           (8, '林冲', '13800000006', '河南省', '开封市', '鼓楼区', '东京八十万禁军教头府', 1, NOW()),
                                                                                                                                                           (9, '秦明', '13800000007', '山东省', '潍坊市', '青州市', '青州府兵马总管府', 1, NOW()),
                                                                                                                                                           (10, '呼延灼', '13800000008', '山西省', '太原市', '杏花岭区', '并州府呼延将军府', 1, NOW()),
                                                                                                                                                           (11, '花荣', '13800000009', '山东省', '潍坊市', '青州市', '清风寨知寨府', 1, NOW()),
                                                                                                                                                           (12, '柴进', '13800000010', '河北省', '沧州市', '运河区', '横海郡柴家庄小旋风府', 1, NOW()),
                                                                                                                                                           (13, '李逵', '13800000011', '山东省', '临沂市', '沂水县', '百丈村黑旋风家', 1, NOW()),
                                                                                                                                                           (14, '武松', '13800000012', '山东省', '聊城市', '阳谷县', '紫石街武大郎家', 1, NOW()),
                                                                                                                                                           (15, '鲁智深', '13800000013', '山西省', '忻州市', '五台县', '五台山文殊院', 1, NOW()),
                                                                                                                                                           (16, '燕青', '13800000014', '河北省', '邯郸市', '大名县', '玉麒麟府西院', 1, NOW()),
                                                                                                                                                           (17, '扈三娘', '13800000015', '山东省', '菏泽市', '郓城县', '扈家庄一丈青府', 1, NOW());

-- 红楼梦人物地址
INSERT INTO `tb_address` (`user_id`, `receiver_name`, `receiver_phone`, `province`, `city`, `district`, `detail_address`, `is_default`, `create_time`) VALUES
                                                                                                                                                           (18, '贾宝玉', '13800000016', '北京市', '北京市', '西城区', '大观园怡红院', 1, NOW()),
                                                                                                                                                           (19, '林黛玉', '13800000017', '北京市', '北京市', '西城区', '大观园潇湘馆', 1, NOW()),
                                                                                                                                                           (20, '薛宝钗', '13800000018', '北京市', '北京市', '西城区', '大观园蘅芜苑', 1, NOW()),
                                                                                                                                                           (21, '王熙凤', '13800000019', '北京市', '北京市', '西城区', '大观园荣国府王熙凤院', 1, NOW()),
                                                                                                                                                           (22, '史湘云', '13800000020', '北京市', '北京市', '西城区', '大观园藕香榭', 1, NOW()),
                                                                                                                                                           (23, '贾探春', '13800000021', '北京市', '北京市', '西城区', '大观园秋爽斋', 1, NOW()),
                                                                                                                                                           (24, '贾惜春', '13800000022', '北京市', '北京市', '西城区', '大观园蓼风轩', 1, NOW()),
                                                                                                                                                           (25, '妙玉', '13800000023', '北京市', '北京市', '西城区', '大观园栊翠庵', 1, NOW()),
                                                                                                                                                           (26, '李纨', '13800000024', '北京市', '北京市', '西城区', '大观园稻香村', 1, NOW()),
                                                                                                                                                           (27, '晴雯', '13800000025', '北京市', '北京市', '西城区', '大观园怡红院晴雯房', 1, NOW());




-- 如果表已存在，建议先备份（可选）
-- CREATE TABLE tb_category_backup AS SELECT * FROM tb_category;

TRUNCATE TABLE tb_category;

SET NAMES utf8mb4;

-- ==================== 一级分类 ====================
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (1, 0, '手机通讯', 1, 1, NOW()),
                                                                                                 (2, 0, '电脑办公', 2, 1, NOW()),
                                                                                                 (3, 0, '数码影音', 3, 1, NOW()),
                                                                                                 (4, 0, '家用电器', 4, 1, NOW()),
                                                                                                 (5, 0, '家居生活', 5, 1, NOW()),
                                                                                                 (6, 0, '个护美妆', 6, 1, NOW()),
                                                                                                 (7, 0, '母婴玩具', 7, 1, NOW()),
                                                                                                 (8, 0, '食品生鲜', 8, 1, NOW()),
                                                                                                 (9, 0, '图书文娱', 9, 1, NOW()),
                                                                                                 (10, 0, '运动户外', 10, 1, NOW()),
                                                                                                 (11, 0, '汽车用品', 11, 1, NOW());

-- ==================== 二级分类 ====================
-- 1. 手机通讯
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (101, 1, '手机', 1, 1, NOW()),
                                                                                                 (102, 1, '手机配件', 2, 1, NOW()),
                                                                                                 (103, 1, '对讲机', 3, 1, NOW());

-- 2. 电脑办公
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (201, 2, '电脑整机', 1, 1, NOW()),
                                                                                                 (202, 2, '电脑外设', 2, 1, NOW()),
                                                                                                 (203, 2, '办公设备', 3, 1, NOW()),
                                                                                                 (204, 2, '网络设备', 4, 1, NOW());

-- 3. 数码影音
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (301, 3, '音频设备', 1, 1, NOW()),
                                                                                                 (302, 3, '智能穿戴', 2, 1, NOW()),
                                                                                                 (303, 3, '摄影器材', 3, 1, NOW()),
                                                                                                 (304, 3, '影音播放', 4, 1, NOW());

-- 4. 家用电器
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (401, 4, '大家电', 1, 1, NOW()),
                                                                                                 (402, 4, '厨房电器', 2, 1, NOW()),
                                                                                                 (403, 4, '生活电器', 3, 1, NOW()),
                                                                                                 (404, 4, '个护电器', 4, 1, NOW());

-- 5. 家居生活
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (501, 5, '家具', 1, 1, NOW()),
                                                                                                 (502, 5, '家纺', 2, 1, NOW()),
                                                                                                 (503, 5, '餐厨具', 3, 1, NOW()),
                                                                                                 (504, 5, '日用杂货', 4, 1, NOW()),
                                                                                                 (505, 5, '家装建材', 5, 1, NOW());

-- 6. 个护美妆
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (601, 6, '面部护肤', 1, 1, NOW()),
                                                                                                 (602, 6, '彩妆', 2, 1, NOW()),
                                                                                                 (603, 6, '身体护理', 3, 1, NOW()),
                                                                                                 (604, 6, '口腔护理', 4, 1, NOW()),
                                                                                                 (605, 6, '香水', 5, 1, NOW());

-- 7. 母婴玩具
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (701, 7, '奶粉/营养品', 1, 1, NOW()),
                                                                                                 (702, 7, '尿裤/湿巾', 2, 1, NOW()),
                                                                                                 (703, 7, '喂养用品', 3, 1, NOW()),
                                                                                                 (704, 7, '洗护/安全', 4, 1, NOW()),
                                                                                                 (705, 7, '玩具', 5, 1, NOW());

-- 8. 食品生鲜
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (801, 8, '休闲食品', 1, 1, NOW()),
                                                                                                 (802, 8, '粮油调味', 2, 1, NOW()),
                                                                                                 (803, 8, '饮料冲调', 3, 1, NOW()),
                                                                                                 (804, 8, '生鲜', 4, 1, NOW()),
                                                                                                 (805, 8, '酒类', 5, 1, NOW());

-- 9. 图书文娱
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (901, 9, '文学/小说', 1, 1, NOW()),
                                                                                                 (902, 9, '动漫/漫画', 2, 1, NOW()),
                                                                                                 (903, 9, '艺术/设计', 3, 1, NOW()),
                                                                                                 (904, 9, '教育/考试', 4, 1, NOW()),
                                                                                                 (905, 9, '音像/数字', 5, 1, NOW());

-- 10. 运动户外
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (1001, 10, '运动鞋服', 1, 1, NOW()),
                                                                                                 (1002, 10, '健身器材', 2, 1, NOW()),
                                                                                                 (1003, 10, '户外装备', 3, 1, NOW()),
                                                                                                 (1004, 10, '球类/轮滑', 4, 1, NOW()),
                                                                                                 (1005, 10, '骑行', 5, 1, NOW());

-- 11. 汽车用品
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (1101, 11, '车载电器', 1, 1, NOW()),
                                                                                                 (1102, 11, '内饰装饰', 2, 1, NOW()),
                                                                                                 (1103, 11, '养护清洁', 3, 1, NOW()),
                                                                                                 (1104, 11, '安全应急', 4, 1, NOW());

-- ==================== 三级分类 ====================
-- 手机通讯 -> 手机
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (10101, 101, '智能手机', 1, 1, NOW()),
                                                                                                 (10102, 101, '老人机', 2, 1, NOW()),
                                                                                                 (10103, 101, '二手手机', 3, 1, NOW());

-- 手机通讯 -> 手机配件
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (10201, 102, '手机壳', 1, 1, NOW()),
                                                                                                 (10202, 102, '充电器', 2, 1, NOW()),
                                                                                                 (10203, 102, '数据线', 3, 1, NOW()),
                                                                                                 (10204, 102, '贴膜', 4, 1, NOW()),
                                                                                                 (10205, 102, '充电宝', 5, 1, NOW()),
                                                                                                 (10206, 102, '手机支架', 6, 1, NOW());

-- 手机通讯 -> 对讲机
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (10301, 103, '模拟对讲机', 1, 1, NOW()),
                                                                                                 (10302, 103, '数字对讲机', 2, 1, NOW());

-- 电脑办公 -> 电脑整机
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (20101, 201, '笔记本', 1, 1, NOW()),
                                                                                                 (20102, 201, '台式机', 2, 1, NOW()),
                                                                                                 (20103, 201, '一体机', 3, 1, NOW()),
                                                                                                 (20104, 201, '平板电脑', 4, 1, NOW());

-- 电脑办公 -> 电脑外设
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (20201, 202, '键盘', 1, 1, NOW()),
                                                                                                 (20202, 202, '鼠标', 2, 1, NOW()),
                                                                                                 (20203, 202, '显示器', 3, 1, NOW()),
                                                                                                 (20204, 202, '耳机', 4, 1, NOW()),
                                                                                                 (20205, 202, '摄像头', 5, 1, NOW()),
                                                                                                 (20206, 202, '扩展坞', 6, 1, NOW());

-- 电脑办公 -> 办公设备
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (20301, 203, '打印机', 1, 1, NOW()),
                                                                                                 (20302, 203, '扫描仪', 2, 1, NOW()),
                                                                                                 (20303, 203, '投影仪', 3, 1, NOW()),
                                                                                                 (20304, 203, '碎纸机', 4, 1, NOW()),
                                                                                                 (20305, 203, '考勤机', 5, 1, NOW());

-- 电脑办公 -> 网络设备
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (20401, 204, '路由器', 1, 1, NOW()),
                                                                                                 (20402, 204, '交换机', 2, 1, NOW()),
                                                                                                 (20403, 204, '网卡', 3, 1, NOW()),
                                                                                                 (20404, 204, '无线网卡', 4, 1, NOW());

-- 数码影音 -> 音频设备
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (30101, 301, '真无线耳机', 1, 1, NOW()),
                                                                                                 (30102, 301, '头戴耳机', 2, 1, NOW()),
                                                                                                 (30103, 301, '运动耳机', 3, 1, NOW()),
                                                                                                 (30104, 301, '蓝牙音箱', 4, 1, NOW()),
                                                                                                 (30105, 301, '有线耳机', 5, 1, NOW());

-- 数码影音 -> 智能穿戴
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (30201, 302, '智能手表', 1, 1, NOW()),
                                                                                                 (30202, 302, '智能手环', 2, 1, NOW()),
                                                                                                 (30203, 302, '智能眼镜', 3, 1, NOW());

-- 数码影音 -> 摄影器材
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (30301, 303, '微单/单反', 1, 1, NOW()),
                                                                                                 (30302, 303, '运动相机', 2, 1, NOW()),
                                                                                                 (30303, 303, '镜头', 3, 1, NOW()),
                                                                                                 (30304, 303, '稳定器', 4, 1, NOW()),
                                                                                                 (30305, 303, '三脚架', 5, 1, NOW());

-- 数码影音 -> 影音播放
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (30401, 304, '投影仪', 1, 1, NOW()),
                                                                                                 (30402, 304, '电视盒子', 2, 1, NOW()),
                                                                                                 (30403, 304, 'MP3/MP4', 3, 1, NOW()),
                                                                                                 (30404, 304, '录音笔', 4, 1, NOW());

-- 家用电器 -> 大家电
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (40101, 401, '电视', 1, 1, NOW()),
                                                                                                 (40102, 401, '冰箱', 2, 1, NOW()),
                                                                                                 (40103, 401, '洗衣机', 3, 1, NOW()),
                                                                                                 (40104, 401, '空调', 4, 1, NOW()),
                                                                                                 (40105, 401, '热水器', 5, 1, NOW());

-- 家用电器 -> 厨房电器
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (40201, 402, '电饭煲', 1, 1, NOW()),
                                                                                                 (40202, 402, '空气炸锅', 2, 1, NOW()),
                                                                                                 (40203, 402, '微波炉', 3, 1, NOW()),
                                                                                                 (40204, 402, '破壁机', 4, 1, NOW()),
                                                                                                 (40205, 402, '烤箱', 5, 1, NOW()),
                                                                                                 (40206, 402, '洗碗机', 6, 1, NOW());

-- 家用电器 -> 生活电器
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (40301, 403, '吸尘器', 1, 1, NOW()),
                                                                                                 (40302, 403, '扫地机器人', 2, 1, NOW()),
                                                                                                 (40303, 403, '电风扇', 3, 1, NOW()),
                                                                                                 (40304, 403, '加湿器', 4, 1, NOW()),
                                                                                                 (40305, 403, '空气净化器', 5, 1, NOW());

-- 家用电器 -> 个护电器
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (40401, 404, '吹风机', 1, 1, NOW()),
                                                                                                 (40402, 404, '电动牙刷', 2, 1, NOW()),
                                                                                                 (40403, 404, '剃须刀', 3, 1, NOW()),
                                                                                                 (40404, 404, '美容仪', 4, 1, NOW());

-- 家居生活 -> 家具
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (50101, 501, '沙发', 1, 1, NOW()),
                                                                                                 (50102, 501, '床', 2, 1, NOW()),
                                                                                                 (50103, 501, '桌椅', 3, 1, NOW()),
                                                                                                 (50104, 501, '柜子', 4, 1, NOW()),
                                                                                                 (50105, 501, '儿童家具', 5, 1, NOW());

-- 家居生活 -> 家纺
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (50201, 502, '四件套', 1, 1, NOW()),
                                                                                                 (50202, 502, '被子', 2, 1, NOW()),
                                                                                                 (50203, 502, '枕头', 3, 1, NOW()),
                                                                                                 (50204, 502, '毛毯', 4, 1, NOW()),
                                                                                                 (50205, 502, '凉席', 5, 1, NOW());

-- 家居生活 -> 餐厨具
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (50301, 503, '锅具', 1, 1, NOW()),
                                                                                                 (50302, 503, '刀具', 2, 1, NOW()),
                                                                                                 (50303, 503, '餐具', 3, 1, NOW()),
                                                                                                 (50304, 503, '水杯', 4, 1, NOW()),
                                                                                                 (50305, 503, '厨房收纳', 5, 1, NOW());

-- 家居生活 -> 日用杂货
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (50401, 504, '衣架', 1, 1, NOW()),
                                                                                                 (50402, 504, '挂钩', 2, 1, NOW()),
                                                                                                 (50403, 504, '收纳箱', 3, 1, NOW()),
                                                                                                 (50404, 504, '雨伞', 4, 1, NOW()),
                                                                                                 (50405, 504, '一次性用品', 5, 1, NOW());

-- 家居生活 -> 家装建材
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (50501, 505, '灯具', 1, 1, NOW()),
                                                                                                 (50502, 505, '开关', 2, 1, NOW()),
                                                                                                 (50503, 505, '五金', 3, 1, NOW()),
                                                                                                 (50504, 505, '墙漆', 4, 1, NOW()),
                                                                                                 (50505, 505, '地板', 5, 1, NOW());

-- 个护美妆 -> 面部护肤
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (60101, 601, '洁面', 1, 1, NOW()),
                                                                                                 (60102, 601, '水乳', 2, 1, NOW()),
                                                                                                 (60103, 601, '精华', 3, 1, NOW()),
                                                                                                 (60104, 601, '面霜', 4, 1, NOW()),
                                                                                                 (60105, 601, '面膜', 5, 1, NOW()),
                                                                                                 (60106, 601, '防晒', 6, 1, NOW());

-- 个护美妆 -> 彩妆
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (60201, 602, '粉底', 1, 1, NOW()),
                                                                                                 (60202, 602, '口红', 2, 1, NOW()),
                                                                                                 (60203, 602, '眼影', 3, 1, NOW()),
                                                                                                 (60204, 602, '眉笔', 4, 1, NOW()),
                                                                                                 (60205, 602, '卸妆', 5, 1, NOW());

-- 个护美妆 -> 身体护理
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (60301, 603, '沐浴露', 1, 1, NOW()),
                                                                                                 (60302, 603, '身体乳', 2, 1, NOW()),
                                                                                                 (60303, 603, '香皂', 3, 1, NOW()),
                                                                                                 (60304, 603, '脱毛', 4, 1, NOW());

-- 个护美妆 -> 口腔护理
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (60401, 604, '牙膏', 1, 1, NOW()),
                                                                                                 (60402, 604, '牙刷', 2, 1, NOW()),
                                                                                                 (60403, 604, '漱口水', 3, 1, NOW()),
                                                                                                 (60404, 604, '牙线', 4, 1, NOW());

-- 个护美妆 -> 香水
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (60501, 605, '男士香水', 1, 1, NOW()),
                                                                                                 (60502, 605, '女士香水', 2, 1, NOW()),
                                                                                                 (60503, 605, '中性香水', 3, 1, NOW());

-- 母婴玩具 -> 奶粉/营养品
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (70101, 701, '婴幼儿奶粉', 1, 1, NOW()),
                                                                                                 (70102, 701, '孕妇奶粉', 2, 1, NOW()),
                                                                                                 (70103, 701, 'DHA', 3, 1, NOW()),
                                                                                                 (70104, 701, '钙铁锌', 4, 1, NOW());

-- 母婴玩具 -> 尿裤/湿巾
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (70201, 702, '纸尿裤', 1, 1, NOW()),
                                                                                                 (70202, 702, '拉拉裤', 2, 1, NOW()),
                                                                                                 (70203, 702, '婴儿湿巾', 3, 1, NOW());

-- 母婴玩具 -> 喂养用品
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (70301, 703, '奶瓶', 1, 1, NOW()),
                                                                                                 (70302, 703, '吸奶器', 2, 1, NOW()),
                                                                                                 (70303, 703, '餐椅', 3, 1, NOW()),
                                                                                                 (70304, 703, '辅食机', 4, 1, NOW());

-- 母婴玩具 -> 洗护/安全
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (70401, 704, '婴儿洗发水', 1, 1, NOW()),
                                                                                                 (70402, 704, '护臀膏', 2, 1, NOW()),
                                                                                                 (70403, 704, '安全座椅', 3, 1, NOW()),
                                                                                                 (70404, 704, '围栏', 4, 1, NOW());

-- 母婴玩具 -> 玩具
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (70501, 705, '积木', 1, 1, NOW()),
                                                                                                 (70502, 705, '遥控车', 2, 1, NOW()),
                                                                                                 (70503, 705, '毛绒玩具', 3, 1, NOW()),
                                                                                                 (70504, 705, '益智玩具', 4, 1, NOW());

-- 食品生鲜 -> 休闲食品
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (80101, 801, '坚果', 1, 1, NOW()),
                                                                                                 (80102, 801, '饼干', 2, 1, NOW()),
                                                                                                 (80103, 801, '巧克力', 3, 1, NOW()),
                                                                                                 (80104, 801, '肉干', 4, 1, NOW()),
                                                                                                 (80105, 801, '海味', 5, 1, NOW());

-- 食品生鲜 -> 粮油调味
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (80201, 802, '米', 1, 1, NOW()),
                                                                                                 (80202, 802, '面', 2, 1, NOW()),
                                                                                                 (80203, 802, '油', 3, 1, NOW()),
                                                                                                 (80204, 802, '酱油', 4, 1, NOW()),
                                                                                                 (80205, 802, '醋', 5, 1, NOW()),
                                                                                                 (80206, 802, '火锅底料', 6, 1, NOW());

-- 食品生鲜 -> 饮料冲调
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (80301, 803, '牛奶', 1, 1, NOW()),
                                                                                                 (80302, 803, '咖啡', 2, 1, NOW()),
                                                                                                 (80303, 803, '茶', 3, 1, NOW()),
                                                                                                 (80304, 803, '果汁', 4, 1, NOW()),
                                                                                                 (80305, 803, '碳酸饮料', 5, 1, NOW());

-- 食品生鲜 -> 生鲜
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (80401, 804, '水果', 1, 1, NOW()),
                                                                                                 (80402, 804, '蔬菜', 2, 1, NOW()),
                                                                                                 (80403, 804, '肉类', 3, 1, NOW()),
                                                                                                 (80404, 804, '海鲜', 4, 1, NOW()),
                                                                                                 (80405, 804, '蛋品', 5, 1, NOW());

-- 食品生鲜 -> 酒类
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (80501, 805, '白酒', 1, 1, NOW()),
                                                                                                 (80502, 805, '啤酒', 2, 1, NOW()),
                                                                                                 (80503, 805, '红酒', 3, 1, NOW()),
                                                                                                 (80504, 805, '黄酒', 4, 1, NOW()),
                                                                                                 (80505, 805, '洋酒', 5, 1, NOW());

-- 图书文娱 -> 文学/小说
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (90101, 901, '科幻', 1, 1, NOW()),
                                                                                                 (90102, 901, '悬疑', 2, 1, NOW()),
                                                                                                 (90103, 901, '言情', 3, 1, NOW()),
                                                                                                 (90104, 901, '武侠', 4, 1, NOW()),
                                                                                                 (90105, 901, '经典', 5, 1, NOW());

-- 图书文娱 -> 动漫/漫画
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (90201, 902, '国产漫画', 1, 1, NOW()),
                                                                                                 (90202, 902, '日漫', 2, 1, NOW()),
                                                                                                 (90203, 902, '画集', 3, 1, NOW());

-- 图书文娱 -> 艺术/设计
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (90301, 903, '摄影', 1, 1, NOW()),
                                                                                                 (90302, 903, '绘画', 2, 1, NOW()),
                                                                                                 (90303, 903, '建筑', 3, 1, NOW()),
                                                                                                 (90304, 903, '设计', 4, 1, NOW());

-- 图书文娱 -> 教育/考试
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (90401, 904, '中小学教辅', 1, 1, NOW()),
                                                                                                 (90402, 904, '考研', 2, 1, NOW()),
                                                                                                 (90403, 904, '外语', 3, 1, NOW()),
                                                                                                 (90404, 904, '职业技能', 4, 1, NOW());

-- 图书文娱 -> 音像/数字
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (90501, 905, '音乐CD', 1, 1, NOW()),
                                                                                                 (90502, 905, '电子书', 2, 1, NOW()),
                                                                                                 (90503, 905, '有声书', 3, 1, NOW());

-- 运动户外 -> 运动鞋服
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (100101, 1001, '跑鞋', 1, 1, NOW()),
                                                                                                 (100102, 1001, '篮球鞋', 2, 1, NOW()),
                                                                                                 (100103, 1001, '运动T恤', 3, 1, NOW()),
                                                                                                 (100104, 1001, '运动裤', 4, 1, NOW());

-- 运动户外 -> 健身器材
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (100201, 1002, '瑜伽垫', 1, 1, NOW()),
                                                                                                 (100202, 1002, '哑铃', 2, 1, NOW()),
                                                                                                 (100203, 1002, '跑步机', 3, 1, NOW()),
                                                                                                 (100204, 1002, '拉力带', 4, 1, NOW());

-- 运动户外 -> 户外装备
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (100301, 1003, '帐篷', 1, 1, NOW()),
                                                                                                 (100302, 1003, '睡袋', 2, 1, NOW()),
                                                                                                 (100303, 1003, '登山杖', 3, 1, NOW()),
                                                                                                 (100304, 1003, '野餐垫', 4, 1, NOW());

-- 运动户外 -> 球类/轮滑
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (100401, 1004, '篮球', 1, 1, NOW()),
                                                                                                 (100402, 1004, '足球', 2, 1, NOW()),
                                                                                                 (100403, 1004, '羽毛球', 3, 1, NOW()),
                                                                                                 (100404, 1004, '滑板', 4, 1, NOW());

-- 运动户外 -> 骑行
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (100501, 1005, '自行车', 1, 1, NOW()),
                                                                                                 (100502, 1005, '头盔', 2, 1, NOW()),
                                                                                                 (100503, 1005, '骑行服', 3, 1, NOW()),
                                                                                                 (100504, 1005, '车灯', 4, 1, NOW());

-- 汽车用品 -> 车载电器
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (110101, 1101, '行车记录仪', 1, 1, NOW()),
                                                                                                 (110102, 1101, '车载充电器', 2, 1, NOW()),
                                                                                                 (110103, 1101, '净化器', 3, 1, NOW());

-- 汽车用品 -> 内饰装饰
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (110201, 1102, '脚垫', 1, 1, NOW()),
                                                                                                 (110202, 1102, '座套', 2, 1, NOW()),
                                                                                                 (110203, 1102, '挂件', 3, 1, NOW()),
                                                                                                 (110204, 1102, '方向盘套', 4, 1, NOW());

-- 汽车用品 -> 养护清洁
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (110301, 1103, '机油', 1, 1, NOW()),
                                                                                                 (110302, 1103, '玻璃水', 2, 1, NOW()),
                                                                                                 (110303, 1103, '洗车液', 3, 1, NOW()),
                                                                                                 (110304, 1103, '车蜡', 4, 1, NOW());

-- 汽车用品 -> 安全应急
INSERT INTO `tb_category` (`id`, `parent_id`, `name`, `sort_order`, `status`, `create_time`) VALUES
                                                                                                 (110401, 1104, '灭火器', 1, 1, NOW()),
                                                                                                 (110402, 1104, '安全锤', 2, 1, NOW()),
                                                                                                 (110403, 1104, '急救包', 3, 1, NOW()),
                                                                                                 (110404, 1104, '三角牌', 4, 1, NOW());

-- 所有三级分类插入完毕



-- ==================== 一级分类1：手机通讯 ====================
-- 10101 智能手机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Apple iPhone 15 Pro Max 256GB 钛金属', 'A17 Pro芯片，钛金属设计，4800万像素主摄，USB-C接口', 9999.00, 120, 10101, 'https://picsum.photos/id/1/600/600', 1, 356, NOW()),
                                                                                                                                           ('华为Mate 60 Pro 12GB+512GB 雅丹黑', '麒麟9000S芯片，卫星通话，超光变XMAGE影像', 6999.00, 85, 10101, 'https://picsum.photos/id/2/600/600', 1, 421, NOW()),
                                                                                                                                           ('小米14 Ultra 16GB+1TB 黑色', '徕卡光学Summilux镜头，骁龙8 Gen3，2K OLED屏幕', 6499.00, 98, 10101, 'https://picsum.photos/id/3/600/600', 1, 289, NOW()),
                                                                                                                                           ('vivo X100 Pro 12GB+256GB 蓝晶', '天玑9300芯片，蔡司APO超级长焦，蓝海电池', 4999.00, 156, 10101, 'https://picsum.photos/id/4/600/600', 1, 312, NOW()),
                                                                                                                                           ('三星Galaxy S24 Ultra 12GB+512GB 钛灰', '骁龙8 Gen3 for Galaxy，S Pen手写笔，2亿像素主摄', 8999.00, 67, 10101, 'https://picsum.photos/id/5/600/600', 1, 178, NOW());

-- 10102 老人机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('诺基亚105 4G 黑色', '超长待机，大按键大字体，FM收音机，双卡双待', 199.00, 500, 10102, 'https://picsum.photos/id/6/600/600', 1, 892, NOW()),
                                                                                                                                           ('飞利浦E169K 香槟金', '2.4英寸屏幕，大音量喇叭，一键拨号，手电筒功能', 299.00, 320, 10102, 'https://picsum.photos/id/7/600/600', 1, 567, NOW()),
                                                                                                                                           ('天语Q31 红色', '三防设计，超长待机，亲情号码，语音播报', 249.00, 410, 10102, 'https://picsum.photos/id/8/600/600', 1, 435, NOW()),
                                                                                                                                           ('中兴守护宝K580 黑色', '4G全网通，定位功能，SOS紧急呼叫，大字体', 399.00, 280, 10102, 'https://picsum.photos/id/9/600/600', 1, 321, NOW());

-- 10103 二手手机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('95新 Apple iPhone 14 128GB 星光色', '原装无拆修，电池健康85%以上，官方质检', 3299.00, 45, 10103, 'https://picsum.photos/id/10/600/600', 1, 123, NOW()),
                                                                                                                                           ('99新 华为Mate 50 Pro 256GB 曜金黑', '几乎全新，使用不到3个月，全套配件', 3999.00, 28, 10103, 'https://picsum.photos/id/11/600/600', 1, 87, NOW()),
                                                                                                                                           ('9成新 小米13 8GB+256GB 白色', '无明显划痕，功能完好，赠送充电器', 2199.00, 62, 10103, 'https://picsum.photos/id/12/600/600', 1, 156, NOW()),
                                                                                                                                           ('95新 vivo X90 12GB+256GB 华夏红', '原装屏幕，无维修记录，电池健康90%', 2799.00, 34, 10103, 'https://picsum.photos/id/13/600/600', 1, 98, NOW());

-- 10201 手机壳
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('iPhone 15 Pro Max 透明硅胶手机壳', '高透防黄，全包边防摔，精准孔位', 29.90, 1000, 10201, 'https://picsum.photos/id/14/600/600', 1, 1256, NOW()),
                                                                                                                                           ('华为Mate 60 Pro 素皮手机壳 青山黛', '真皮质感，磁吸无线充，镜头全包', 69.90, 500, 10201, 'https://picsum.photos/id/15/600/600', 1, 789, NOW()),
                                                                                                                                           ('小米14 液态硅胶手机壳 黑色', '亲肤手感，防指纹，防摔耐磨', 39.90, 800, 10201, 'https://picsum.photos/id/16/600/600', 1, 923, NOW()),
                                                                                                                                           ('vivo X100 卡通动漫手机壳', '个性图案，软壳材质，全包保护', 19.90, 1200, 10201, 'https://picsum.photos/id/17/600/600', 1, 1567, NOW());

-- 10202 充电器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Apple 20W USB-C充电器', '原装正品，快速充电，安全可靠', 149.00, 300, 10202, 'https://picsum.photos/id/18/600/600', 1, 892, NOW()),
                                                                                                                                           ('华为66W超级快充充电器', '支持华为全系列快充，氮化镓技术', 199.00, 250, 10202, 'https://picsum.photos/id/19/600/600', 1, 654, NOW()),
                                                                                                                                           ('小米120W氮化镓充电器', '体积小巧，多协议兼容，快充不发烫', 249.00, 200, 10202, 'https://picsum.photos/id/20/600/600', 1, 521, NOW()),
                                                                                                                                           ('绿联65W氮化镓三口充电器', '同时充3台设备，笔记本手机通用', 129.00, 400, 10202, 'https://picsum.photos/id/21/600/600', 1, 734, NOW());

-- 10203 数据线
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Apple USB-C to Lightning数据线 1米', '原装正品，MFi认证，耐用编织线', 149.00, 400, 10203, 'https://picsum.photos/id/22/600/600', 1, 987, NOW()),
                                                                                                                                           ('华为6A超级快充数据线 1.5米', '支持66W快充，加粗线芯，不易折断', 59.00, 500, 10203, 'https://picsum.photos/id/23/600/600', 1, 765, NOW()),
                                                                                                                                           ('绿联Type-C数据线 2米', '尼龙编织，5A快充，兼容安卓设备', 29.90, 800, 10203, 'https://picsum.photos/id/24/600/600', 1, 1234, NOW()),
                                                                                                                                           ('倍思磁吸数据线 Type-C', '强磁吸附，一吸即充，LED指示灯', 39.90, 600, 10203, 'https://picsum.photos/id/25/600/600', 1, 876, NOW());

-- 10204 贴膜
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('iPhone 15 Pro Max 钢化膜 2片装', '高清高透，防指纹，全屏覆盖', 29.90, 1500, 10204, 'https://picsum.photos/id/26/600/600', 1, 2345, NOW()),
                                                                                                                                           ('华为Mate 60 Pro 曲屏钢化膜', '曲面贴合，防爆防刮，触控灵敏', 49.90, 800, 10204, 'https://picsum.photos/id/27/600/600', 1, 1567, NOW()),
                                                                                                                                           ('小米14 磨砂钢化膜', '防眩光，防指纹，游戏专用', 39.90, 600, 10204, 'https://picsum.photos/id/28/600/600', 1, 987, NOW()),
                                                                                                                                           ('闪魔 防窥钢化膜 通用款', '28度防窥，保护隐私，高清显示', 35.90, 1000, 10204, 'https://picsum.photos/id/29/600/600', 1, 1876, NOW());

-- 10205 充电宝
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('小米移动电源3 20000mAh', '双向快充，大容量，可上飞机', 129.00, 500, 10205, 'https://picsum.photos/id/30/600/600', 1, 1567, NOW()),
                                                                                                                                           ('Anker 10000mAh磁吸充电宝', 'MagSafe磁吸，无线快充，小巧便携', 249.00, 300, 10205, 'https://picsum.photos/id/31/600/600', 1, 892, NOW()),
                                                                                                                                           ('罗马仕30000mAh大容量充电宝', '22.5W快充，多口输出，超长续航', 99.00, 600, 10205, 'https://picsum.photos/id/32/600/600', 1, 2134, NOW()),
                                                                                                                                           ('倍思65W氮化镓充电宝', '笔记本手机通用，快充不发烫，20000mAh', 199.00, 250, 10205, 'https://picsum.photos/id/33/600/600', 1, 654, NOW());

-- 10206 手机支架
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('绿联 桌面手机支架', '可调节角度，铝合金材质，稳固不晃', 29.90, 800, 10206, 'https://picsum.photos/id/34/600/600', 1, 1234, NOW()),
                                                                                                                                           ('倍思 车载手机支架 出风口款', '重力感应，一放即夹，稳固不掉', 39.90, 600, 10206, 'https://picsum.photos/id/35/600/600', 1, 987, NOW()),
                                                                                                                                           ('懒人手机支架 床头款', '360度旋转，可弯曲，解放双手', 19.90, 1000, 10206, 'https://picsum.photos/id/36/600/600', 1, 1567, NOW()),
                                                                                                                                           ('磁吸手机支架 车载仪表台款', '强磁吸附，角度可调，不挡视线', 25.90, 700, 10206, 'https://picsum.photos/id/37/600/600', 1, 876, NOW());

-- 10301 模拟对讲机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('宝锋BF-888S对讲机', '大功率，远距离通话，酒店工地专用', 99.00, 300, 10301, 'https://picsum.photos/id/38/600/600', 1, 567, NOW()),
                                                                                                                                           ('摩托罗拉T40对讲机', '民用对讲机，3公里通话，清晰音质', 199.00, 200, 10301, 'https://picsum.photos/id/39/600/600', 1, 321, NOW()),
                                                                                                                                           ('小米对讲机Lite', '轻薄便携，APP写频，超长待机', 149.00, 250, 10301, 'https://picsum.photos/id/40/600/600', 1, 435, NOW()),
                                                                                                                                           ('海能达TC-500S对讲机', '专业商用，坚固耐用，防水防尘', 399.00, 150, 10301, 'https://picsum.photos/id/41/600/600', 1, 213, NOW());

-- 10302 数字对讲机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('海能达BD500数字对讲机', 'DMR数字制式，音质清晰，加密通话', 699.00, 100, 10302, 'https://picsum.photos/id/42/600/600', 1, 123, NOW()),
                                                                                                                                           ('摩托罗拉XIR P3688数字对讲机', '专业数字对讲机，防水防尘，长续航', 899.00, 80, 10302, 'https://picsum.photos/id/43/600/600', 1, 87, NOW()),
                                                                                                                                           ('小米对讲机2S', '数字模拟双模，全国对讲，GPS定位', 299.00, 150, 10302, 'https://picsum.photos/id/44/600/600', 1, 156, NOW()),
                                                                                                                                           ('建伍TK-3207GD数字对讲机', '专业商用，坚固耐用，音质清晰', 799.00, 60, 10302, 'https://picsum.photos/id/45/600/600', 1, 98, NOW());

-- ==================== 一级分类2：电脑办公 ====================
-- 20101 笔记本
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Apple MacBook Pro 14英寸 M3 Pro', 'M3 Pro芯片，18GB内存，512GB SSD，Liquid Retina XDR屏幕', 14999.00, 50, 20101, 'https://picsum.photos/id/46/600/600', 1, 234, NOW()),
                                                                                                                                           ('联想拯救者Y9000P 2024款', 'i9-14900HX，RTX4060，16GB+1TB，2.5K 240Hz屏幕', 9999.00, 80, 20101, 'https://picsum.photos/id/47/600/600', 1, 356, NOW()),
                                                                                                                                           ('华为MateBook X Pro 2024', '酷睿Ultra 7，32GB+1TB，3.1K触控屏，轻薄便携', 11999.00, 60, 20101, 'https://picsum.photos/id/48/600/600', 1, 189, NOW()),
                                                                                                                                           ('小米笔记本Pro 16 2024', '锐龙7 8845H，16GB+1TB，3.2K 120Hz屏幕', 5999.00, 120, 20101, 'https://picsum.photos/id/49/600/600', 1, 278, NOW());

-- 20102 台式机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('联想拯救者刃9000K 2024', 'i9-14900KF，RTX4090，32GB+2TB，水冷散热', 29999.00, 30, 20102, 'https://picsum.photos/id/50/600/600', 1, 87, NOW()),
                                                                                                                                           ('戴尔XPS 8960台式机', 'i7-14700K，RTX4070Ti，16GB+1TB，专业设计', 15999.00, 40, 20102, 'https://picsum.photos/id/51/600/600', 1, 65, NOW()),
                                                                                                                                           ('惠普暗影精灵10台式机', 'i5-14400F，RTX4060，16GB+512GB，游戏主机', 6999.00, 80, 20102, 'https://picsum.photos/id/52/600/600', 1, 123, NOW()),
                                                                                                                                           ('攀升战境S5游戏主机', '锐龙5 7600X，RTX4060，16GB+1TB，高性价比', 4999.00, 100, 20102, 'https://picsum.photos/id/53/600/600', 1, 156, NOW());

-- 20103 一体机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Apple iMac 24英寸 M3', 'M3芯片，8GB+256GB，4.5K Retina屏幕，粉色', 9999.00, 40, 20103, 'https://picsum.photos/id/54/600/600', 1, 123, NOW()),
                                                                                                                                           ('联想小新Pro 27一体机', 'i7-14700H，32GB+1TB，2.5K 100Hz屏幕', 7999.00, 50, 20103, 'https://picsum.photos/id/55/600/600', 1, 98, NOW()),
                                                                                                                                           ('华为MateStation X 2024', '酷睿Ultra 5，16GB+1TB，28.2英寸4K触控屏', 10999.00, 30, 20103, 'https://picsum.photos/id/56/600/600', 1, 67, NOW()),
                                                                                                                                           ('戴尔灵越5420一体机', 'i5-13400T，16GB+512GB，23.8英寸全高清屏幕', 4999.00, 80, 20103, 'https://picsum.photos/id/57/600/600', 1, 134, NOW());

-- 20104 平板电脑
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Apple iPad Pro 12.9英寸 M4', 'M4芯片，256GB，Liquid Retina XDR屏幕，支持Apple Pencil', 9999.00, 60, 20104, 'https://picsum.photos/id/58/600/600', 1, 234, NOW()),
                                                                                                                                           ('华为MatePad Pro 13.2英寸', '麒麟9000S芯片，12GB+256GB，OLED屏幕，星闪技术', 5999.00, 80, 20104, 'https://picsum.photos/id/59/600/600', 1, 189, NOW()),
                                                                                                                                           ('小米平板6 Pro', '骁龙8+ Gen1，12GB+256GB，11.2英寸2.8K 144Hz屏幕', 2499.00, 150, 20104, 'https://picsum.photos/id/60/600/600', 1, 321, NOW()),
                                                                                                                                           ('vivo Pad3 Pro', '天玑9300芯片，12GB+256GB，13英寸3K 144Hz屏幕', 2999.00, 100, 20104, 'https://picsum.photos/id/61/600/600', 1, 213, NOW());

-- 20201 键盘
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('樱桃MX3.0S机械键盘 红轴', '德国原厂轴体，铝合金外壳，全键无冲', 399.00, 200, 20201, 'https://picsum.photos/id/62/600/600', 1, 456, NOW()),
                                                                                                                                           ('罗技G913 TKL无线机械键盘', '超薄设计，LIGHTSPEED无线，RGB背光', 1299.00, 100, 20201, 'https://picsum.photos/id/63/600/600', 1, 234, NOW()),
                                                                                                                                           ('小米机械键盘 青轴', '高性价比，全键无冲，白色背光', 129.00, 500, 20201, 'https://picsum.photos/id/64/600/600', 1, 789, NOW()),
                                                                                                                                           ('雷蛇黑寡妇蜘蛛V3机械键盘', '绿轴，RGB幻彩，人体工学设计', 499.00, 150, 20201, 'https://picsum.photos/id/65/600/600', 1, 321, NOW());

-- 20202 鼠标
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('罗技G502 HERO游戏鼠标', 'HERO 25K传感器，11个可编程按键，RGB灯光', 299.00, 300, 20202, 'https://picsum.photos/id/66/600/600', 1, 567, NOW()),
                                                                                                                                           ('雷蛇炼狱蝰蛇V3 Pro无线鼠标', '轻量化设计，30000DPI，长续航', 799.00, 100, 20202, 'https://picsum.photos/id/67/600/600', 1, 234, NOW()),
                                                                                                                                           ('苹果Magic Mouse 2', '多点触控，无线充电，超薄设计', 549.00, 150, 20202, 'https://picsum.photos/id/68/600/600', 1, 321, NOW()),
                                                                                                                                           ('小米无线鼠标 静音版', '静音按键，2.4G无线，人体工学', 49.90, 800, 20202, 'https://picsum.photos/id/69/600/600', 1, 1234, NOW());

-- 20203 显示器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('戴尔U2723QE 27英寸4K显示器', 'IPS面板，Type-C 90W供电，专业色彩', 3999.00, 80, 20203, 'https://picsum.photos/id/70/600/600', 1, 189, NOW()),
                                                                                                                                           ('华硕ROG PG27AQDM 27英寸2K显示器', 'OLED面板，240Hz刷新率，0.03ms响应', 4999.00, 50, 20203, 'https://picsum.photos/id/71/600/600', 1, 123, NOW()),
                                                                                                                                           ('小米显示器27英寸 2K 165Hz', 'IPS面板，高刷新率，低蓝光', 1299.00, 200, 20203, 'https://picsum.photos/id/72/600/600', 1, 356, NOW()),
                                                                                                                                           ('AOC C27G2Z 27英寸曲面显示器', 'VA面板，240Hz刷新率，1500R曲率', 1499.00, 150, 20203, 'https://picsum.photos/id/73/600/600', 1, 278, NOW());

-- 20204 耳机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('索尼WH-1000XM5头戴式降噪耳机', '业界领先降噪，30小时续航，Hi-Res音质', 2499.00, 100, 20204, 'https://picsum.photos/id/74/600/600', 1, 234, NOW()),
                                                                                                                                           ('Bose QuietComfort 45降噪耳机', '舒适佩戴，出色降噪，24小时续航', 1999.00, 80, 20204, 'https://picsum.photos/id/75/600/600', 1, 189, NOW()),
                                                                                                                                           ('雷蛇北海巨妖V3游戏耳机', '7.1环绕声，RGB灯光，舒适耳罩', 399.00, 200, 20204, 'https://picsum.photos/id/76/600/600', 1, 321, NOW()),
                                                                                                                                           ('漫步者W820NB双金标版', '主动降噪，Hi-Res音质，49小时续航', 299.00, 300, 20204, 'https://picsum.photos/id/77/600/600', 1, 567, NOW());

-- 20205 摄像头
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('罗技C920e高清摄像头', '1080P 30fps，自动对焦，内置麦克风', 299.00, 200, 20205, 'https://picsum.photos/id/78/600/600', 1, 456, NOW()),
                                                                                                                                           ('雷蛇清姬专业版摄像头', '1080P 60fps，自动曝光，可调节支架', 499.00, 100, 20205, 'https://picsum.photos/id/79/600/600', 1, 234, NOW()),
                                                                                                                                           ('奥尼A31高清摄像头', '1080P，内置降噪麦克风，即插即用', 99.00, 500, 20205, 'https://picsum.photos/id/80/600/600', 1, 789, NOW()),
                                                                                                                                           ('罗技Brio 4K超高清摄像头', '4K HDR，90度广角，Windows Hello支持', 1299.00, 50, 20205, 'https://picsum.photos/id/81/600/600', 1, 123, NOW());

-- 20206 扩展坞
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('贝尔金雷电4扩展坞', '12合1接口，8K视频输出，100W PD供电', 1299.00, 50, 20206, 'https://picsum.photos/id/82/600/600', 1, 87, NOW()),
                                                                                                                                           ('绿联Type-C扩展坞 9合1', 'HDMI 4K 60Hz，千兆网口，PD 100W供电', 199.00, 200, 20206, 'https://picsum.photos/id/83/600/600', 1, 321, NOW()),
                                                                                                                                           ('倍思Type-C扩展坞 7合1', '小巧便携，HDMI+VGA双输出，SD/TF读卡', 129.00, 300, 20206, 'https://picsum.photos/id/84/600/600', 1, 456, NOW()),
                                                                                                                                           ('戴尔WD19S扩展坞', '130W供电，双4K输出，千兆网口', 799.00, 80, 20206, 'https://picsum.photos/id/85/600/600', 1, 123, NOW());

-- 20301 打印机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('惠普LaserJet Pro MFP M126a激光一体机', '打印复印扫描，黑白激光，家用办公', 1299.00, 100, 20301, 'https://picsum.photos/id/86/600/600', 1, 234, NOW()),
                                                                                                                                           ('佳能TS3480彩色喷墨一体机', '无线打印，复印扫描，家用照片打印', 499.00, 200, 20301, 'https://picsum.photos/id/87/600/600', 1, 356, NOW()),
                                                                                                                                           ('兄弟DCP-L2550DW激光一体机', '无线打印，自动双面，打印复印扫描', 1599.00, 80, 20301, 'https://picsum.photos/id/88/600/600', 1, 189, NOW()),
                                                                                                                                           ('爱普生L3253墨仓式打印机', '连供设计，低成本打印，无线连接', 899.00, 150, 20301, 'https://picsum.photos/id/89/600/600', 1, 278, NOW());

-- 20302 扫描仪
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('佳能LiDE 300平板扫描仪', '2400dpi分辨率，USB供电，轻薄便携', 399.00, 100, 20302, 'https://picsum.photos/id/90/600/600', 1, 123, NOW()),
                                                                                                                                           ('爱普生V39平板扫描仪', '4800dpi分辨率，OCR文字识别，快速扫描', 599.00, 80, 20302, 'https://picsum.photos/id/91/600/600', 1, 87, NOW()),
                                                                                                                                           ('惠普ScanJet Pro 2500 f1平板+馈纸式扫描仪', '自动进纸，双面扫描，办公专用', 1999.00, 50, 20302, 'https://picsum.photos/id/92/600/600', 1, 65, NOW()),
                                                                                                                                           ('富士通iX1500高速扫描仪', '双面高速扫描，无线连接，自动进纸', 3999.00, 30, 20302, 'https://picsum.photos/id/93/600/600', 1, 43, NOW());

-- 20303 投影仪
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('极米H6 4K投影仪', '4K超高清，2200ANSI流明，自动对焦梯形校正', 4999.00, 80, 20303, 'https://picsum.photos/id/94/600/600', 1, 189, NOW()),
                                                                                                                                           ('坚果J10S投影仪', '徕卡色彩调校，2400ANSI流明，丹拿音响', 4299.00, 100, 20303, 'https://picsum.photos/id/95/600/600', 1, 234, NOW()),
                                                                                                                                           ('当贝F6投影仪', '4K分辨率，2800ANSI流明，MTK9669芯片', 5499.00, 60, 20303, 'https://picsum.photos/id/96/600/600', 1, 156, NOW()),
                                                                                                                                           ('小米投影仪2S', '1080P分辨率，850ANSI流明，自动对焦', 1999.00, 150, 20303, 'https://picsum.photos/id/97/600/600', 1, 321, NOW());

-- 20304 碎纸机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('得力9912碎纸机', '5级保密，单次碎纸10张，20L纸屑桶', 399.00, 100, 20304, 'https://picsum.photos/id/98/600/600', 1, 123, NOW()),
                                                                                                                                           ('科密C-838碎纸机', '4级保密，单次碎纸8张，15L纸屑桶', 299.00, 150, 20304, 'https://picsum.photos/id/99/600/600', 1, 189, NOW()),
                                                                                                                                           ('盆景4S23碎纸机', '5级保密，单次碎纸12张，23L纸屑桶', 499.00, 80, 20304, 'https://picsum.photos/id/100/600/600', 1, 87, NOW()),
                                                                                                                                           ('震旦AS068CD碎纸机', '4级保密，可碎光盘，16L纸屑桶', 349.00, 120, 20304, 'https://picsum.photos/id/101/600/600', 1, 156, NOW());

-- 20305 考勤机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('得力3765指纹考勤机', '指纹识别，U盘下载，免软件安装', 199.00, 200, 20305, 'https://picsum.photos/id/102/600/600', 1, 321, NOW()),
                                                                                                                                           ('中控智慧ZKTeco XFace100人脸识别考勤机', '人脸+指纹识别，快速识别，WiFi连接', 599.00, 100, 20305, 'https://picsum.photos/id/103/600/600', 1, 189, NOW()),
                                                                                                                                           ('钉钉M1X人脸识别考勤机', '钉钉生态，多人同时识别，云端管理', 399.00, 150, 20305, 'https://picsum.photos/id/104/600/600', 1, 234, NOW()),
                                                                                                                                           ('汉王C330E人脸识别考勤机', '汉王人脸识别技术，大容量存储，脱机使用', 499.00, 80, 20305, 'https://picsum.photos/id/105/600/600', 1, 123, NOW());

-- 20401 路由器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('华硕RT-AX86U Pro路由器', 'WiFi6，AX5700速率，2.5G网口，游戏加速', 1299.00, 80, 20401, 'https://picsum.photos/id/106/600/600', 1, 189, NOW()),
                                                                                                                                           ('小米AX6000路由器', 'WiFi6，6000Mbps速率，6路独立信号放大器', 599.00, 200, 20401, 'https://picsum.photos/id/107/600/600', 1, 321, NOW()),
                                                                                                                                           ('TP-LINK XDR6088路由器', 'WiFi6，AX6000速率，双2.5G网口，易展Mesh', 799.00, 150, 20401, 'https://picsum.photos/id/108/600/600', 1, 234, NOW()),
                                                                                                                                           ('华为AX3 Pro路由器', 'WiFi6+，3000Mbps速率，HarmonyOS一碰连', 349.00, 300, 20401, 'https://picsum.photos/id/109/600/600', 1, 456, NOW());

-- 20402 交换机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('TP-LINK TL-SG1008D 8口千兆交换机', '即插即用，金属外壳，稳定可靠', 129.00, 300, 20402, 'https://picsum.photos/id/110/600/600', 1, 456, NOW()),
                                                                                                                                           ('华为S1730S-L8T-A 8口千兆交换机', '企业级品质，即插即用，静音设计', 199.00, 150, 20402, 'https://picsum.photos/id/111/600/600', 1, 234, NOW()),
                                                                                                                                           ('水星SG105 5口千兆交换机', '小巧便携，即插即用，低功耗', 59.90, 500, 20402, 'https://picsum.photos/id/112/600/600', 1, 789, NOW()),
                                                                                                                                           ('华三H3C S1224 24口千兆交换机', '机架式设计，即插即用，企业级稳定', 499.00, 80, 20402, 'https://picsum.photos/id/113/600/600', 1, 123, NOW());

-- 20403 网卡
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('TP-LINK TG-3269E千兆PCI-E网卡', '千兆有线，PCI-E接口，稳定高速', 49.90, 300, 20403, 'https://picsum.photos/id/114/600/600', 1, 321, NOW()),
                                                                                                                                           ('英特尔I225-V 2.5G PCI-E网卡', '2.5Gbps速率，低延迟，游戏专用', 129.00, 150, 20403, 'https://picsum.photos/id/115/600/600', 1, 189, NOW()),
                                                                                                                                           ('绿联USB3.0千兆网卡', 'USB接口，即插即用，笔记本台式机通用', 39.90, 400, 20403, 'https://picsum.photos/id/116/600/600', 1, 456, NOW()),
                                                                                                                                           ('华硕PCE-AX58BT WiFi6无线网卡', 'AX3000速率，蓝牙5.0，PCI-E接口', 299.00, 100, 20403, 'https://picsum.photos/id/117/600/600', 1, 123, NOW());

-- 20404 无线网卡
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('TP-LINK TL-WN725N USB无线网卡', '150Mbps速率，小巧便携，即插即用', 29.90, 500, 20404, 'https://picsum.photos/id/118/600/600', 1, 789, NOW()),
                                                                                                                                           ('小米WiFi6 USB无线网卡', 'AX3000速率，双频并发，高速稳定', 99.00, 200, 20404, 'https://picsum.photos/id/119/600/600', 1, 321, NOW()),
                                                                                                                                           ('绿联WiFi6 USB无线网卡', 'AX1800速率，双频，免驱安装', 69.90, 300, 20404, 'https://picsum.photos/id/120/600/600', 1, 456, NOW()),
                                                                                                                                           ('华硕USB-AX56 Nano WiFi6无线网卡', 'AX1800速率，小巧设计，低延迟', 129.00, 100, 20404, 'https://picsum.photos/id/121/600/600', 1, 189, NOW());

-- ==================== 一级分类3：数码影音 ====================
-- 30101 真无线耳机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Apple AirPods Pro (第二代)', '主动降噪，空间音频，MagSafe充电', 1899.00, 200, 30101, 'https://picsum.photos/id/122/600/600', 1, 567, NOW()),
                                                                                                                                           ('索尼WF-1000XM5真无线降噪耳机', '业界领先降噪，Hi-Res音质，36小时续航', 1999.00, 150, 30101, 'https://picsum.photos/id/123/600/600', 1, 456, NOW()),
                                                                                                                                           ('华为FreeBuds Pro 3', '星闪连接，超感知原声，主动降噪', 1299.00, 200, 30101, 'https://picsum.photos/id/124/600/600', 1, 321, NOW()),
                                                                                                                                           ('小米Buds 4 Pro', '48dB深度降噪，Hi-Res音质，38小时续航', 699.00, 300, 30101, 'https://picsum.photos/id/125/600/600', 1, 567, NOW());

-- 30102 头戴耳机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('索尼WH-1000XM5头戴式降噪耳机', '业界领先降噪，30小时续航，Hi-Res音质', 2499.00, 100, 30102, 'https://picsum.photos/id/126/600/600', 1, 234, NOW()),
                                                                                                                                           ('Bose QuietComfort 45降噪耳机', '舒适佩戴，出色降噪，24小时续航', 1999.00, 80, 30102, 'https://picsum.photos/id/127/600/600', 1, 189, NOW()),
                                                                                                                                           ('苹果AirPods Max', '主动降噪，空间音频，铝合金外壳', 3999.00, 50, 30102, 'https://picsum.photos/id/128/600/600', 1, 123, NOW()),
                                                                                                                                           ('漫步者W820NB双金标版', '主动降噪，Hi-Res音质，49小时续航', 299.00, 300, 30102, 'https://picsum.photos/id/129/600/600', 1, 567, NOW());

-- 30103 运动耳机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Powerbeats Pro真无线运动耳机', '挂耳式设计，IPX4防水，9小时续航', 1499.00, 100, 30103, 'https://picsum.photos/id/130/600/600', 1, 189, NOW()),
                                                                                                                                           ('JBL Endurance Peak 3运动耳机', 'IP68防水防尘，挂耳式，30小时续航', 799.00, 150, 30103, 'https://picsum.photos/id/131/600/600', 1, 234, NOW()),
                                                                                                                                           ('韶音OpenRun Pro骨传导耳机', '骨传导技术，IP55防水，10小时续航', 1099.00, 120, 30103, 'https://picsum.photos/id/132/600/600', 1, 156, NOW()),
                                                                                                                                           ('小米运动蓝牙耳机Mini', '入耳式，IPX4防水，12小时续航', 99.00, 500, 30103, 'https://picsum.photos/id/133/600/600', 1, 789, NOW());

-- 30104 蓝牙音箱
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('马歇尔Marshall Emberton II蓝牙音箱', '经典设计，立体声，20小时续航', 1299.00, 100, 30104, 'https://picsum.photos/id/134/600/600', 1, 234, NOW()),
                                                                                                                                           ('索尼SRS-XB43蓝牙音箱', '重低音，IP67防水，24小时续航', 899.00, 150, 30104, 'https://picsum.photos/id/135/600/600', 1, 189, NOW()),
                                                                                                                                           ('JBL Flip 6蓝牙音箱', 'IP67防水，便携设计，12小时续航', 599.00, 200, 30104, 'https://picsum.photos/id/136/600/600', 1, 321, NOW()),
                                                                                                                                           ('小米Sound Move蓝牙音箱', '便携设计，Hi-Res音质，21小时续航', 399.00, 300, 30104, 'https://picsum.photos/id/137/600/600', 1, 456, NOW());

-- 30105 有线耳机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('森海塞尔IE 40 PRO入耳式耳机', '专业监听，清晰音质，耐用设计', 499.00, 100, 30105, 'https://picsum.photos/id/138/600/600', 1, 123, NOW()),
                                                                                                                                           ('铁三角ATH-M50x头戴式监听耳机', '专业监听，折叠设计，舒适佩戴', 999.00, 80, 30105, 'https://picsum.photos/id/139/600/600', 1, 87, NOW()),
                                                                                                                                           ('苹果EarPods有线耳机', 'Lightning接口，线控麦克风，原装正品', 149.00, 500, 30105, 'https://picsum.photos/id/140/600/600', 1, 789, NOW()),
                                                                                                                                           ('漫步者H230P入耳式耳机', '高性价比，线控麦克风，清晰音质', 59.90, 800, 30105, 'https://picsum.photos/id/141/600/600', 1, 1234, NOW());

-- 30201 智能手表
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Apple Watch Series 9 GPS 45mm', 'S9芯片，双击手势，健康监测', 3199.00, 150, 30201, 'https://picsum.photos/id/142/600/600', 1, 321, NOW()),
                                                                                                                                           ('华为Watch GT 4 46mm', '14天续航，健康监测，GPS定位', 1488.00, 200, 30201, 'https://picsum.photos/id/143/600/600', 1, 234, NOW()),
                                                                                                                                           ('小米Watch S3', 'eSIM独立通话，健康监测，15天续航', 999.00, 250, 30201, 'https://picsum.photos/id/144/600/600', 1, 456, NOW()),
                                                                                                                                           ('三星Galaxy Watch 6 Classic 47mm', '旋转表圈，健康监测，GPS定位', 2599.00, 100, 30201, 'https://picsum.photos/id/145/600/600', 1, 189, NOW());

-- 30202 智能手环
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('小米手环8', '1.62英寸AMOLED屏幕，14天续航，健康监测', 249.00, 500, 30202, 'https://picsum.photos/id/146/600/600', 1, 789, NOW()),
                                                                                                                                           ('华为手环8', '1.47英寸屏幕，14天续航，心率监测', 229.00, 400, 30202, 'https://picsum.photos/id/147/600/600', 1, 654, NOW()),
                                                                                                                                           ('荣耀手环7', '1.47英寸屏幕，14天续航，血氧监测', 199.00, 300, 30202, 'https://picsum.photos/id/148/600/600', 1, 567, NOW()),
                                                                                                                                           ('Keep手环B4', '运动监测，14天续航，智能提醒', 169.00, 350, 30202, 'https://picsum.photos/id/149/600/600', 1, 432, NOW());

-- 30203 智能眼镜
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('华为Vision Glass智能眼镜', '120英寸虚拟巨幕，高清观影，轻量设计', 1699.00, 80, 30203, 'https://picsum.photos/id/150/600/600', 1, 87, NOW()),
                                                                                                                                           ('雷鸟Air 2 AR智能眼镜', '1080P高清显示，120Hz刷新率，便携设计', 2299.00, 60, 30203, 'https://picsum.photos/id/151/600/600', 1, 65, NOW()),
                                                                                                                                           ('OPPO Air Glass 2智能眼镜', '轻量设计，语音助手，导航提醒', 2999.00, 40, 30203, 'https://picsum.photos/id/152/600/600', 1, 43, NOW()),
                                                                                                                                           ('小米智能眼镜探索版', 'MicroLED显示屏，语音助手，导航翻译', 2499.00, 50, 30203, 'https://picsum.photos/id/153/600/600', 1, 56, NOW());

-- 30301 微单/单反
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('索尼A7M4全画幅微单相机', '3300万像素，4K视频，5轴防抖', 15999.00, 50, 30301, 'https://picsum.photos/id/154/600/600', 1, 123, NOW()),
                                                                                                                                           ('佳能EOS R6 Mark II全画幅微单', '2420万像素，4K 60fps视频，高速连拍', 14999.00, 40, 30301, 'https://picsum.photos/id/155/600/600', 1, 98, NOW()),
                                                                                                                                           ('尼康Z6 II全画幅微单', '2450万像素，4K视频，5轴防抖', 11999.00, 60, 30301, 'https://picsum.photos/id/156/600/600', 1, 87, NOW()),
                                                                                                                                           ('富士X-T5 APS-C微单', '4020万像素，复古设计，胶片模拟', 11999.00, 45, 30301, 'https://picsum.photos/id/157/600/600', 1, 76, NOW());

-- 30302 运动相机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('GoPro HERO12 Black运动相机', '5.3K视频，HyperSmooth 6.0防抖，防水', 3198.00, 80, 30302, 'https://picsum.photos/id/158/600/600', 1, 189, NOW()),
                                                                                                                                           ('大疆Osmo Action 4运动相机', '4K/120fps视频，RockSteady 3.0防抖，防水', 2598.00, 100, 30302, 'https://picsum.photos/id/159/600/600', 1, 234, NOW()),
                                                                                                                                           ('Insta360 X3全景运动相机', '360度全景拍摄，5.7K视频，防水', 2798.00, 90, 30302, 'https://picsum.photos/id/160/600/600', 1, 156, NOW()),
                                                                                                                                           ('索尼FDR-X3000运动相机', '4K视频，光学防抖，防水', 2499.00, 60, 30302, 'https://picsum.photos/id/161/600/600', 1, 123, NOW());

-- 30303 镜头
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('索尼FE 24-70mm F2.8 GM II镜头', '标准变焦，大光圈，G大师镜头', 14999.00, 30, 30303, 'https://picsum.photos/id/162/600/600', 1, 65, NOW()),
                                                                                                                                           ('佳能RF 50mm F1.8 STM镜头', '标准定焦，大光圈，性价比高', 1199.00, 100, 30303, 'https://picsum.photos/id/163/600/600', 1, 189, NOW()),
                                                                                                                                           ('尼康Z 24-200mm F4-6.3 VR镜头', '大变焦，防抖，便携设计', 4999.00, 50, 30303, 'https://picsum.photos/id/164/600/600', 1, 87, NOW()),
                                                                                                                                           ('富士XF 35mm F1.4 R镜头', '定焦大光圈，复古设计，胶片模拟', 3499.00, 60, 30303, 'https://picsum.photos/id/165/600/600', 1, 76, NOW());

-- 30304 稳定器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('大疆DJI Ronin-SC 2微单稳定器', '轻量设计，三轴防抖，智能跟随', 2299.00, 80, 30304, 'https://picsum.photos/id/166/600/600', 1, 123, NOW()),
                                                                                                                                           ('智云Weebill 3S微单稳定器', '轻量设计，三轴防抖，补光灯', 1999.00, 100, 30304, 'https://picsum.photos/id/167/600/600', 1, 156, NOW()),
                                                                                                                                           ('大疆DJI Osmo Mobile 6手机稳定器', '手机专用，三轴防抖，智能跟随', 799.00, 200, 30304, 'https://picsum.photos/id/168/600/600', 1, 321, NOW()),
                                                                                                                                           ('智云Smooth 5S手机稳定器', '手机专用，三轴防抖，补光灯', 599.00, 250, 30304, 'https://picsum.photos/id/169/600/600', 1, 289, NOW());

-- 30305 三脚架
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('曼富图MT055CXPRO3碳纤维三脚架', '专业级，碳纤维材质，承重8kg', 2999.00, 30, 30305, 'https://picsum.photos/id/170/600/600', 1, 43, NOW()),
                                                                                                                                           ('百诺C2690TB1碳纤维三脚架', '碳纤维材质，便携设计，承重8kg', 1299.00, 60, 30305, 'https://picsum.photos/id/171/600/600', 1, 87, NOW()),
                                                                                                                                           ('思锐E1005A+G12三脚架', '铝合金材质，便携设计，承重6kg', 599.00, 150, 30305, 'https://picsum.photos/id/172/600/600', 1, 156, NOW()),
                                                                                                                                           ('富图宝X4I三脚架', '铝合金材质，便携设计，承重5kg', 399.00, 200, 30305, 'https://picsum.photos/id/173/600/600', 1, 234, NOW());

-- 30401 投影仪
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('极米H6 4K投影仪', '4K超高清，2200ANSI流明，自动对焦梯形校正', 4999.00, 80, 30401, 'https://picsum.photos/id/174/600/600', 1, 189, NOW()),
                                                                                                                                           ('坚果J10S投影仪', '徕卡色彩调校，2400ANSI流明，丹拿音响', 4299.00, 100, 30401, 'https://picsum.photos/id/175/600/600', 1, 234, NOW()),
                                                                                                                                           ('当贝F6投影仪', '4K分辨率，2800ANSI流明，MTK9669芯片', 5499.00, 60, 30401, 'https://picsum.photos/id/176/600/600', 1, 156, NOW()),
                                                                                                                                           ('小米投影仪2S', '1080P分辨率，850ANSI流明，自动对焦', 1999.00, 150, 30401, 'https://picsum.photos/id/177/600/600', 1, 321, NOW());

-- 30402 电视盒子
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Apple TV 4K (第三代)', 'A15芯片，4K HDR，杜比视界', 1299.00, 100, 30402, 'https://picsum.photos/id/178/600/600', 1, 123, NOW()),
                                                                                                                                           ('小米盒子4S MAX', '4K HDR，2GB+16GB，蓝牙语音遥控', 399.00, 300, 30402, 'https://picsum.photos/id/179/600/600', 1, 321, NOW()),
                                                                                                                                           ('当贝盒子B3 Pro', 'S922X芯片，4GB+64GB，8K解码', 599.00, 150, 30402, 'https://picsum.photos/id/180/600/600', 1, 234, NOW()),
                                                                                                                                           ('腾讯极光盒子5 Pro', 'S928X芯片，4GB+64GB，8K解码', 699.00, 120, 30402, 'https://picsum.photos/id/181/600/600', 1, 189, NOW());

-- 30403 MP3/MP4
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('索尼NW-A306 MP3播放器', '安卓系统，Hi-Res音质，32GB存储', 1999.00, 80, 30403, 'https://picsum.photos/id/182/600/600', 1, 87, NOW()),
                                                                                                                                           ('苹果iPod touch (第七代)', 'iOS系统，4英寸屏幕，128GB存储', 1599.00, 60, 30403, 'https://picsum.photos/id/183/600/600', 1, 65, NOW()),
                                                                                                                                           ('山灵M3 Ultra MP3播放器', '双DAC，Hi-Res音质，32GB存储', 1299.00, 100, 30403, 'https://picsum.photos/id/184/600/600', 1, 123, NOW()),
                                                                                                                                           ('爱国者MP3-801播放器', '便携设计，16GB存储，FM收音机', 99.00, 500, 30403, 'https://picsum.photos/id/185/600/600', 1, 456, NOW());

-- 30404 录音笔
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('索尼ICD-UX570F录音笔', '4GB存储，高清录音，降噪功能', 599.00, 150, 30404, 'https://picsum.photos/id/186/600/600', 1, 189, NOW()),
                                                                                                                                           ('科大讯飞SR502智能录音笔', '16GB存储，实时转写，多语言翻译', 1299.00, 100, 30404, 'https://picsum.photos/id/187/600/600', 1, 234, NOW()),
                                                                                                                                           ('爱国者R5511录音笔', '8GB存储，高清录音，长续航', 199.00, 300, 30404, 'https://picsum.photos/id/188/600/600', 1, 321, NOW()),
                                                                                                                                           ('飞利浦VTR5000录音笔', '4GB存储，高清录音，一键录音', 299.00, 200, 30404, 'https://picsum.photos/id/189/600/600', 1, 278, NOW());
-- 40104 空调
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('格力KFR-35GW/(35592)FNhAa-B1 1.5匹空调', '新一级能效，变频冷暖，自清洁', 3299.00, 150, 40104, 'https://picsum.photos/id/203/600/600', 1, 321, NOW()),
                                                                                                                                           ('美的KFR-35GW/N8MXA1 1.5匹空调', '新一级能效，无风感，智能联网', 3599.00, 120, 40104, 'https://picsum.photos/id/204/600/600', 1, 278, NOW()),
                                                                                                                                           ('海尔KFR-35GW/81@U1-Gc 1.5匹空调', '新一级能效，自清洁，静音设计', 2999.00, 180, 40104, 'https://picsum.photos/id/205/600/600', 1, 356, NOW()),
                                                                                                                                           ('海信KFR-35GW/E500-A1 1.5匹空调', '新一级能效，变频冷暖，高温自清洁', 2799.00, 200, 40104, 'https://picsum.photos/id/206/600/600', 1, 421, NOW());

-- 40105 热水器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('海尔JSQ31-16KL3U1 16L燃气热水器', '零冷水，恒温出水，智能联网', 3999.00, 80, 40105, 'https://picsum.photos/id/207/600/600', 1, 189, NOW()),
                                                                                                                                           ('美的JSQ30-RX7 16L燃气热水器', '零冷水，增压水伺服，一级能效', 3599.00, 100, 40105, 'https://picsum.photos/id/208/600/600', 1, 234, NOW()),
                                                                                                                                           ('史密斯E60VDP 60L电热水器', '金圭内胆，双棒速热，一级能效', 2999.00, 120, 40105, 'https://picsum.photos/id/209/600/600', 1, 278, NOW()),
                                                                                                                                           ('林内JSQ31-D06 16L燃气热水器', '恒温出水，微火苗技术，进口CPU', 4299.00, 60, 40105, 'https://picsum.photos/id/210/600/600', 1, 156, NOW());

-- 40201 电饭煲
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('美的MB-FZ4086 4L电饭煲', 'IH电磁加热，钛金鼎釜，智能预约', 1299.00, 150, 40201, 'https://picsum.photos/id/211/600/600', 1, 321, NOW()),
                                                                                                                                           ('苏泊尔SF40HC88 4L电饭煲', '远红外加热，本釜内胆，多功能菜单', 1599.00, 120, 40201, 'https://picsum.photos/id/212/600/600', 1, 278, NOW()),
                                                                                                                                           ('九阳F-40T50A 4L电饭煲', 'IH电磁加热，土灶铁釜，智能预约', 899.00, 200, 40201, 'https://picsum.photos/id/213/600/600', 1, 356, NOW()),
                                                                                                                                           ('松下SR-L10H8 3L电饭煲', 'IH电磁加热，备长炭内胆，日本进口', 1999.00, 80, 40201, 'https://picsum.photos/id/214/600/600', 1, 189, NOW());

-- 40202 空气炸锅
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('美的MF-KZ50E101 5L空气炸锅', '大容量，可视窗口，智能触控', 399.00, 300, 40202, 'https://picsum.photos/id/215/600/600', 1, 567, NOW()),
                                                                                                                                           ('苏泊尔KD50D818 5L空气炸锅', '蒸汽嫩炸，可视窗口，不粘内胆', 499.00, 250, 40202, 'https://picsum.photos/id/216/600/600', 1, 456, NOW()),
                                                                                                                                           ('九阳VF516 4.5L空气炸锅', '无油煎炸，智能预约，易清洗', 299.00, 400, 40202, 'https://picsum.photos/id/217/600/600', 1, 654, NOW()),
                                                                                                                                           ('飞利浦HD9270/91 6.2L空气炸锅', '海星底盘，快速加热，大容量', 899.00, 150, 40202, 'https://picsum.photos/id/218/600/600', 1, 321, NOW());

-- 40203 微波炉
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('美的M3-L233B 23L微波炉', '平板加热，智能菜单，快速解冻', 499.00, 200, 40203, 'https://picsum.photos/id/219/600/600', 1, 456, NOW()),
                                                                                                                                           ('格兰仕G80F23CN3L-Q6(P0) 23L微波炉', '光波烧烤，智能菜单，平板加热', 599.00, 180, 40203, 'https://picsum.photos/id/220/600/600', 1, 421, NOW()),
                                                                                                                                           ('松下NN-GT35HB 23L微波炉', '变频加热，智能菜单，平板设计', 799.00, 120, 40203, 'https://picsum.photos/id/221/600/600', 1, 321, NOW()),
                                                                                                                                           ('海尔MZ-2017 20L微波炉', '机械旋钮，简单操作，快速加热', 299.00, 300, 40203, 'https://picsum.photos/id/222/600/600', 1, 567, NOW());

-- 40204 破壁机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('美的MJ-PB12Power304 破壁机', '1200W大功率，静音设计，多功能', 699.00, 200, 40204, 'https://picsum.photos/id/223/600/600', 1, 321, NOW()),
                                                                                                                                           ('苏泊尔SP902S 破壁机', '可拆洗刀座，静音破壁，智能预约', 899.00, 150, 40204, 'https://picsum.photos/id/224/600/600', 1, 278, NOW()),
                                                                                                                                           ('九阳Y912C 破壁机', '高速破壁，冷热双打，智能预约', 599.00, 250, 40204, 'https://picsum.photos/id/225/600/600', 1, 356, NOW()),
                                                                                                                                           ('飞利浦HR2099/90 破壁机', '高速破壁，多档调节，易清洗', 1299.00, 100, 40204, 'https://picsum.photos/id/226/600/600', 1, 189, NOW());

-- 40205 烤箱
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('海氏C40 40L电烤箱', '上下独立控温，热风循环，搪瓷内胆', 599.00, 150, 40205, 'https://picsum.photos/id/227/600/600', 1, 234, NOW()),
                                                                                                                                           ('长帝CRTF32PD 32L电烤箱', '上下独立控温，热风循环，不粘内胆', 499.00, 200, 40205, 'https://picsum.photos/id/228/600/600', 1, 278, NOW()),
                                                                                                                                           ('美的PT40C1 40L电烤箱', '上下独立控温，热风循环，智能菜单', 399.00, 250, 40205, 'https://picsum.photos/id/229/600/600', 1, 321, NOW()),
                                                                                                                                           ('苏泊尔K42FK619 42L电烤箱', '大容量，上下独立控温，热风循环', 459.00, 180, 40205, 'https://picsum.photos/id/230/600/600', 1, 298, NOW());

-- 40206 洗碗机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('美的RX600 15套嵌入式洗碗机', '双驱变频，热风烘干，一级水效', 4999.00, 80, 40206, 'https://picsum.photos/id/231/600/600', 1, 123, NOW()),
                                                                                                                                           ('西门子SJ636X04JC 12套嵌入式洗碗机', '5D喷淋，冷凝烘干，除菌消毒', 6999.00, 60, 40206, 'https://picsum.photos/id/232/600/600', 1, 87, NOW()),
                                                                                                                                           ('海尔EW139186BK 13套嵌入式洗碗机', '80℃高温煮洗，热风烘干，智能联网', 4299.00, 100, 40206, 'https://picsum.photos/id/233/600/600', 1, 156, NOW()),
                                                                                                                                           ('老板WB793D 13套嵌入式洗碗机', '三叉喷淋，热风烘干，一级水效', 5299.00, 70, 40206, 'https://picsum.photos/id/234/600/600', 1, 109, NOW());

-- 40301 吸尘器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('戴森V15 Detect无线吸尘器', '激光探测灰尘，智能感应，强劲吸力', 4999.00, 50, 40301, 'https://picsum.photos/id/235/600/600', 1, 123, NOW()),
                                                                                                                                           ('美的P6 Pro无线吸尘器', '120AW吸力，60分钟续航，除螨功能', 1299.00, 150, 40301, 'https://picsum.photos/id/236/600/600', 1, 234, NOW()),
                                                                                                                                           ('小狗T12 Pro无线吸尘器', '185AW吸力，60分钟续航，智能感应', 1999.00, 100, 40301, 'https://picsum.photos/id/237/600/600', 1, 189, NOW()),
                                                                                                                                           ('追觅V12无线吸尘器', '185AW吸力，90分钟续航，除螨功能', 1799.00, 120, 40301, 'https://picsum.photos/id/238/600/600', 1, 156, NOW());

-- 40302 扫地机器人
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('科沃斯T20 Pro扫地机器人', '热水洗拖布，自动集尘，智能导航', 4999.00, 80, 40302, 'https://picsum.photos/id/239/600/600', 1, 189, NOW()),
                                                                                                                                           ('石头G20扫地机器人', '双螺旋胶刷，自动集尘，声波震动拖地', 4599.00, 90, 40302, 'https://picsum.photos/id/240/600/600', 1, 176, NOW()),
                                                                                                                                           ('小米全能扫地机器人2', '自动集尘，自动洗拖布，热风烘干', 3299.00, 150, 40302, 'https://picsum.photos/id/241/600/600', 1, 278, NOW()),
                                                                                                                                           ('追觅S10 Pro扫地机器人', '自动集尘，自动洗拖布，热风烘干', 3999.00, 100, 40302, 'https://picsum.photos/id/242/600/600', 1, 234, NOW());

-- 40303 电风扇
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('美的FS40-18AR 落地扇', '遥控控制，9档风速，定时功能', 299.00, 300, 40303, 'https://picsum.photos/id/243/600/600', 1, 456, NOW()),
                                                                                                                                           ('戴森AM07无叶风扇', '无叶设计，10档风速，遥控控制', 2999.00, 80, 40303, 'https://picsum.photos/id/244/600/600', 1, 123, NOW()),
                                                                                                                                           ('小米米家直流变频落地扇1X', '直流变频，静音设计，APP控制', 199.00, 400, 40303, 'https://picsum.photos/id/245/600/600', 1, 567, NOW()),
                                                                                                                                           ('格力FD-40X62Bh5 落地扇', '遥控控制，7档风速，定时功能', 259.00, 350, 40303, 'https://picsum.photos/id/246/600/600', 1, 498, NOW());

-- 40304 加湿器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('美的SC-3G40A 4L加湿器', '大容量，静音设计，雾量可调', 129.00, 400, 40304, 'https://picsum.photos/id/247/600/600', 1, 567, NOW()),
                                                                                                                                           ('飞利浦HU4803/00 2L加湿器', '纳米无雾，恒湿功能，静音设计', 399.00, 200, 40304, 'https://picsum.photos/id/248/600/600', 1, 321, NOW()),
                                                                                                                                           ('小米米家智能加湿器', '4L大容量，APP控制，恒湿功能', 199.00, 350, 40304, 'https://picsum.photos/id/249/600/600', 1, 456, NOW()),
                                                                                                                                           ('亚都SZK-J030 3L加湿器', '无雾加湿，恒湿功能，静音设计', 299.00, 250, 40304, 'https://picsum.photos/id/250/600/600', 1, 389, NOW());

-- 40305 空气净化器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('飞利浦AC6678/00空气净化器', 'CADR值710m³/h，除甲醛，智能感应', 3999.00, 80, 40305, 'https://picsum.photos/id/251/600/600', 1, 123, NOW()),
                                                                                                                                           ('小米米家空气净化器4 Pro', 'CADR值500m³/h，除甲醛，APP控制', 1299.00, 200, 40305, 'https://picsum.photos/id/252/600/600', 1, 278, NOW()),
                                                                                                                                           ('戴森TP09空气净化风扇', '空气净化+风扇，除甲醛，智能感应', 5999.00, 50, 40305, 'https://picsum.photos/id/253/600/600', 1, 87, NOW()),
                                                                                                                                           ('霍尼韦尔HPA300空气净化器', 'CADR值465m³/h，除雾霾，HEPA滤网', 2499.00, 100, 40305, 'https://picsum.photos/id/254/600/600', 1, 156, NOW());

-- 40401 吹风机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('戴森HD15吹风机', '高速马达，负离子护发，多档调节', 3199.00, 100, 40401, 'https://picsum.photos/id/255/600/600', 1, 234, NOW()),
                                                                                                                                           ('飞利浦HP8235/00吹风机', '2200W大功率，负离子护发，冷热风', 299.00, 300, 40401, 'https://picsum.photos/id/256/600/600', 1, 456, NOW()),
                                                                                                                                           ('小米米家水离子吹风机', '水离子护发，1800W功率，冷热风', 199.00, 400, 40401, 'https://picsum.photos/id/257/600/600', 1, 567, NOW()),
                                                                                                                                           ('松下EH-WNA3B吹风机', '纳米水离子，1800W功率，冷热风', 399.00, 250, 40401, 'https://picsum.photos/id/258/600/600', 1, 389, NOW());

-- 40402 电动牙刷
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('飞利浦HX6730/02电动牙刷', '声波震动，3种模式，2分钟计时', 399.00, 200, 40402, 'https://picsum.photos/id/259/600/600', 1, 321, NOW()),
                                                                                                                                           ('欧乐B iO7电动牙刷', '磁波震动，5种模式，压力感应', 799.00, 150, 40402, 'https://picsum.photos/id/260/600/600', 1, 234, NOW()),
                                                                                                                                           ('小米T700电动牙刷', '声波震动，4种模式，APP连接', 299.00, 300, 40402, 'https://picsum.photos/id/261/600/600', 1, 456, NOW()),
                                                                                                                                           ('松下EW-DM71电动牙刷', '声波震动，2种模式，便携设计', 199.00, 350, 40402, 'https://picsum.photos/id/262/600/600', 1, 389, NOW());

-- 40403 剃须刀
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('飞利浦S9041/12电动剃须刀', '9系刀头，干湿两用，全身水洗', 1299.00, 100, 40403, 'https://picsum.photos/id/263/600/600', 1, 189, NOW()),
                                                                                                                                           ('博朗9系9350s电动剃须刀', '往复式刀头，干湿两用，智能感应', 1999.00, 80, 40403, 'https://picsum.photos/id/264/600/600', 1, 156, NOW()),
                                                                                                                                           ('飞科FS903电动剃须刀', '浮动刀头，全身水洗，快充功能', 299.00, 400, 40403, 'https://picsum.photos/id/265/600/600', 1, 567, NOW()),
                                                                                                                                           ('小米米家电动剃须刀S500', '浮动刀头，全身水洗，Type-C充电', 199.00, 350, 40403, 'https://picsum.photos/id/266/600/600', 1, 456, NOW());

-- 40404 美容仪
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('雅萌ACE Pro射频美容仪', '射频技术，提拉紧致，淡化皱纹', 4299.00, 60, 40404, 'https://picsum.photos/id/267/600/600', 1, 87, NOW()),
                                                                                                                                           ('初普Tripollar Stop Vx Gold美容仪', '射频技术，提拉紧致，胶原再生', 5299.00, 50, 40404, 'https://picsum.photos/id/268/600/600', 1, 65, NOW()),
                                                                                                                                           ('宙斯二代美容仪', 'EMS微电流，提拉紧致，导入导出', 12999.00, 20, 40404, 'https://picsum.photos/id/269/600/600', 1, 32, NOW()),
                                                                                                                                           ('小米有品inFace射频美容仪', '射频技术，提拉紧致，性价比高', 699.00, 150, 40404, 'https://picsum.photos/id/270/600/600', 1, 189, NOW());

-- ==================== 一级分类5：家居生活 ====================
-- 50101 沙发
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('林氏木业现代简约布艺沙发', '科技布面料，可拆洗，三人位+贵妃', 3999.00, 50, 50101, 'https://picsum.photos/id/271/600/600', 1, 123, NOW()),
                                                                                                                                           ('全友家居北欧风格布艺沙发', '棉麻面料，实木框架，小户型适用', 2999.00, 60, 50101, 'https://picsum.photos/id/272/600/600', 1, 156, NOW()),
                                                                                                                                           ('顾家家居真皮沙发', '头层牛皮，实木框架，三人位', 7999.00, 30, 50101, 'https://picsum.photos/id/273/600/600', 1, 87, NOW()),
                                                                                                                                           ('芝华仕头等舱功能沙发', '电动可躺，科技布面料，单人位', 1999.00, 80, 50101, 'https://picsum.photos/id/274/600/600', 1, 189, NOW());

-- 50102 床
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('林氏木业现代简约板式床', '1.8米双人床，储物功能，北欧风格', 1999.00, 80, 50102, 'https://picsum.photos/id/275/600/600', 1, 234, NOW()),
                                                                                                                                           ('全友家居实木床', '1.8米双人床，橡木材质，中式风格', 3999.00, 50, 50102, 'https://picsum.photos/id/276/600/600', 1, 156, NOW()),
                                                                                                                                           ('顾家家居真皮软包床', '1.8米双人床，头层牛皮，现代风格', 4999.00, 40, 50102, 'https://picsum.photos/id/277/600/600', 1, 123, NOW()),
                                                                                                                                           ('喜临门乳胶床垫+床架套餐', '1.8米，独立弹簧床垫，简约风格', 3299.00, 60, 50102, 'https://picsum.photos/id/278/600/600', 1, 189, NOW());

-- 50103 桌椅
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('林氏木业实木餐桌椅组合', '1.4米餐桌+4椅，橡木材质，北欧风格', 2999.00, 60, 50103, 'https://picsum.photos/id/279/600/600', 1, 156, NOW()),
                                                                                                                                           ('全友家居岩板餐桌椅组合', '1.6米餐桌+6椅，岩板台面，现代风格', 3999.00, 50, 50103, 'https://picsum.photos/id/280/600/600', 1, 123, NOW()),
                                                                                                                                           ('西昊M57人体工学电脑椅', '可调节腰靠，网布材质，办公椅', 899.00, 150, 50103, 'https://picsum.photos/id/281/600/600', 1, 321, NOW()),
                                                                                                                                           ('小米米家升降电脑桌', '电动升降，实木桌面，站立办公', 1999.00, 100, 50103, 'https://picsum.photos/id/282/600/600', 1, 234, NOW());

-- 50104 柜子
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('林氏木业现代简约衣柜', '四门衣柜，推拉门，储物空间大', 2999.00, 50, 50104, 'https://picsum.photos/id/283/600/600', 1, 123, NOW()),
                                                                                                                                           ('全友家居板式衣柜', '五门衣柜，平开门，北欧风格', 3999.00, 40, 50104, 'https://picsum.photos/id/284/600/600', 1, 98, NOW()),
                                                                                                                                           ('顾家家居实木鞋柜', '多层鞋柜，大容量，玄关柜', 1299.00, 80, 50104, 'https://picsum.photos/id/285/600/600', 1, 189, NOW()),
                                                                                                                                           ('宜家毕利书架', '多层书架，可调节隔板，简约风格', 599.00, 200, 50104, 'https://picsum.photos/id/286/600/600', 1, 321, NOW());

-- 50105 儿童家具
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('林氏木业儿童上下床', '高低床，实木材质，带护栏', 3999.00, 40, 50105, 'https://picsum.photos/id/287/600/600', 1, 87, NOW()),
                                                                                                                                           ('全友家居儿童书桌', '可升降书桌，带书架，学习桌', 1299.00, 80, 50105, 'https://picsum.photos/id/288/600/600', 1, 156, NOW()),
                                                                                                                                           ('顾家家居儿童衣柜', '三门衣柜，卡通设计，环保材质', 1999.00, 50, 50105, 'https://picsum.photos/id/289/600/600', 1, 123, NOW()),
                                                                                                                                           ('喜临门儿童床垫', '1.2米，椰棕床垫，护脊设计', 899.00, 100, 50105, 'https://picsum.photos/id/290/600/600', 1, 189, NOW());

-- 50201 四件套
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('水星家纺纯棉四件套', '100%纯棉，简约条纹，1.8米床适用', 299.00, 300, 50201, 'https://picsum.photos/id/291/600/600', 1, 456, NOW()),
                                                                                                                                           ('罗莱家纺磨毛四件套', '全棉磨毛，保暖舒适，1.8米床适用', 499.00, 200, 50201, 'https://picsum.photos/id/292/600/600', 1, 321, NOW()),
                                                                                                                                           ('富安娜家纺真丝四件套', '桑蚕丝材质，奢华质感，1.8米床适用', 1999.00, 50, 50201, 'https://picsum.photos/id/293/600/600', 1, 87, NOW()),
                                                                                                                                           ('网易严选纯棉四件套', '新疆长绒棉，简约纯色，1.5米床适用', 199.00, 400, 50201, 'https://picsum.photos/id/294/600/600', 1, 567, NOW());

-- 50202 被子
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('水星家纺羽绒被', '95%白鹅绒，保暖轻盈，200*230cm', 1299.00, 100, 50202, 'https://picsum.photos/id/295/600/600', 1, 189, NOW()),
                                                                                                                                           ('罗莱家纺蚕丝被', '100%桑蚕丝，春秋被，200*230cm', 899.00, 120, 50202, 'https://picsum.photos/id/296/600/600', 1, 156, NOW()),
                                                                                                                                           ('富安娜家纺羊毛被', '澳洲羊毛，保暖舒适，200*230cm', 599.00, 150, 50202, 'https://picsum.photos/id/297/600/600', 1, 234, NOW()),
                                                                                                                                           ('网易严选大豆纤维被', '大豆纤维，四季通用，200*230cm', 299.00, 300, 50202, 'https://picsum.photos/id/298/600/600', 1, 389, NOW());

-- 50203 枕头
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('水星家纺乳胶枕头', '泰国天然乳胶，护颈设计，高低枕', 299.00, 200, 50203, 'https://picsum.photos/id/299/600/600', 1, 321, NOW()),
                                                                                                                                           ('罗莱家纺记忆棉枕头', '慢回弹记忆棉，护颈设计，人体工学', 199.00, 250, 50203, 'https://picsum.photos/id/300/600/600', 1, 389, NOW()),
                                                                                                                                           ('富安娜家纺羽绒枕头', '95%白鹅绒，柔软舒适，高回弹', 499.00, 100, 50203, 'https://picsum.photos/id/301/600/600', 1, 189, NOW()),
                                                                                                                                           ('网易严选荞麦枕头', '荞麦壳填充，可调节高度，护颈', 99.00, 400, 50203, 'https://picsum.photos/id/302/600/600', 1, 567, NOW());

-- 50204 毛毯
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('水星家纺法兰绒毛毯', '加厚法兰绒，柔软保暖，150*200cm', 99.00, 400, 50204, 'https://picsum.photos/id/303/600/600', 1, 567, NOW()),
                                                                                                                                           ('罗莱家纺珊瑚绒毛毯', '加厚珊瑚绒，亲肤舒适，180*200cm', 129.00, 350, 50204, 'https://picsum.photos/id/304/600/600', 1, 498, NOW()),
                                                                                                                                           ('富安娜家纺羊毛毛毯', '澳洲羊毛，保暖舒适，200*230cm', 399.00, 150, 50204, 'https://picsum.photos/id/305/600/600', 1, 234, NOW()),
                                                                                                                                           ('网易严选羊羔绒毛毯', '双面羊羔绒，加厚保暖，150*200cm', 159.00, 300, 50204, 'https://picsum.photos/id/306/600/600', 1, 389, NOW());

-- 50205 凉席
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('水星家纺竹凉席', '天然竹材，双面可用，1.8米床适用', 199.00, 200, 50205, 'https://picsum.photos/id/307/600/600', 1, 321, NOW()),
                                                                                                                                           ('罗莱家纺藤凉席', '天然藤条，透气凉爽，1.8米床适用', 299.00, 150, 50205, 'https://picsum.photos/id/308/600/600', 1, 278, NOW()),
                                                                                                                                           ('富安娜家纺冰丝凉席', '冰丝材质，柔软舒适，1.5米床适用', 159.00, 250, 50205, 'https://picsum.photos/id/309/600/600', 1, 389, NOW()),
                                                                                                                                           ('网易严选乳胶凉席', '天然乳胶，透气凉爽，1.8米床适用', 259.00, 180, 50205, 'https://picsum.photos/id/310/600/600', 1, 321, NOW());

-- 50301 锅具
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('双立人Nova Plus不粘炒锅', '30cm，不粘涂层，电磁炉通用', 599.00, 150, 50301, 'https://picsum.photos/id/311/600/600', 1, 234, NOW()),
                                                                                                                                           ('苏泊尔火红点炒锅', '32cm，不粘涂层，火红点控温', 299.00, 300, 50301, 'https://picsum.photos/id/312/600/600', 1, 456, NOW()),
                                                                                                                                           ('九阳铸铁炒锅', '32cm，无涂层，铸铁材质', 199.00, 250, 50301, 'https://picsum.photos/id/313/600/600', 1, 389, NOW()),
                                                                                                                                           ('菲仕乐Fissler高压锅', '6L，不锈钢材质，安全防爆', 1299.00, 80, 50301, 'https://picsum.photos/id/314/600/600', 1, 123, NOW());

-- 50302 刀具
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('双立人Point S刀具套装', '6件套，不锈钢材质，锋利耐用', 699.00, 100, 50302, 'https://picsum.photos/id/315/600/600', 1, 189, NOW()),
                                                                                                                                           ('张小泉刀具套装', '7件套，不锈钢材质，中式菜刀', 299.00, 200, 50302, 'https://picsum.photos/id/316/600/600', 1, 321, NOW()),
                                                                                                                                           ('十八子作刀具套装', '8件套，不锈钢材质，锋利耐用', 399.00, 150, 50302, 'https://picsum.photos/id/317/600/600', 1, 278, NOW()),
                                                                                                                                           ('德国WMF刀具套装', '5件套，不锈钢材质，人体工学设计', 899.00, 80, 50302, 'https://picsum.photos/id/318/600/600', 1, 123, NOW());

-- 50303 餐具
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('景德镇骨瓷餐具套装', '56头，骨瓷材质，中式风格', 599.00, 100, 50303, 'https://picsum.photos/id/319/600/600', 1, 189, NOW()),
                                                                                                                                           ('苏泊尔不锈钢餐具套装', '24头，304不锈钢，欧式风格', 199.00, 250, 50303, 'https://picsum.photos/id/320/600/600', 1, 321, NOW()),
                                                                                                                                           ('宜家IKEA餐具套装', '18头，陶瓷材质，简约风格', 99.00, 400, 50303, 'https://picsum.photos/id/321/600/600', 1, 567, NOW()),
                                                                                                                                           ('双立人西餐餐具套装', '16头，304不锈钢，欧式风格', 399.00, 150, 50303, 'https://picsum.photos/id/322/600/600', 1, 234, NOW());

-- 50304 水杯
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('膳魔师保温杯', '500ml，304不锈钢，真空保温', 299.00, 200, 50304, 'https://picsum.photos/id/323/600/600', 1, 321, NOW()),
                                                                                                                                           ('象印保温杯', '480ml，304不锈钢，一键开启', 399.00, 150, 50304, 'https://picsum.photos/id/324/600/600', 1, 278, NOW()),
                                                                                                                                           ('小米米家保温杯', '500ml，316不锈钢，简约设计', 99.00, 400, 50304, 'https://picsum.photos/id/325/600/600', 1, 567, NOW()),
                                                                                                                                           ('富光玻璃杯', '400ml，高硼硅玻璃，双层隔热', 59.90, 500, 50304, 'https://picsum.photos/id/326/600/600', 1, 654, NOW());

-- 50305 厨房收纳
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('太力厨房置物架', '多层置物架，不锈钢材质，落地式', 199.00, 200, 50305, 'https://picsum.photos/id/327/600/600', 1, 321, NOW()),
                                                                                                                                           ('茶花调料盒套装', '4件套，食品级塑料，带勺', 29.90, 500, 50305, 'https://picsum.photos/id/328/600/600', 1, 654, NOW()),
                                                                                                                                           ('宜家IKEA厨房收纳盒', '6件套，食品级塑料，可叠放', 59.90, 400, 50305, 'https://picsum.photos/id/329/600/600', 1, 567, NOW()),
                                                                                                                                           ('炊大皇碗碟架', '不锈钢材质，沥水设计，壁挂式', 89.90, 300, 50305, 'https://picsum.photos/id/330/600/600', 1, 456, NOW());

-- 50401 衣架
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('茶花塑料衣架', '20个装，防滑设计，干湿两用', 29.90, 500, 50401, 'https://picsum.photos/id/331/600/600', 1, 789, NOW()),
                                                                                                                                           ('好太太铝合金衣架', '10个装，铝合金材质，防滑无痕', 59.90, 300, 50401, 'https://picsum.photos/id/332/600/600', 1, 567, NOW()),
                                                                                                                                           ('宜家IKEA木质衣架', '10个装，实木材质，简约设计', 49.90, 350, 50401, 'https://picsum.photos/id/333/600/600', 1, 498, NOW()),
                                                                                                                                           ('植绒衣架', '30个装，植绒材质，防滑无痕', 39.90, 400, 50401, 'https://picsum.photos/id/334/600/600', 1, 654, NOW());

-- 50402 挂钩
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('3M强力粘钩', '10个装，免打孔，承重5kg', 19.90, 600, 50402, 'https://picsum.photos/id/335/600/600', 1, 892, NOW()),
                                                                                                                                           ('太力吸盘挂钩', '6个装，免打孔，可重复使用', 29.90, 400, 50402, 'https://picsum.photos/id/336/600/600', 1, 654, NOW()),
                                                                                                                                           ('茶花挂钩', '8个装，塑料材质，免打孔', 9.90, 800, 50402, 'https://picsum.photos/id/337/600/600', 1, 1234, NOW()),
                                                                                                                                           ('不锈钢挂钩', '12个装，304不锈钢，免打孔', 39.90, 300, 50402, 'https://picsum.photos/id/338/600/600', 1, 567, NOW());

-- 50403 收纳箱
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('茶花收纳箱', '50L，3个装，塑料材质，带盖', 99.00, 400, 50403, 'https://picsum.photos/id/339/600/600', 1, 567, NOW()),
                                                                                                                                           ('禧天龙收纳箱', '60L，2个装，加厚塑料，带轮', 129.00, 300, 50403, 'https://picsum.photos/id/340/600/600', 1, 456, NOW()),
                                                                                                                                           ('宜家IKEA收纳箱', '40L，3个装，布艺材质，可折叠', 79.90, 350, 50403, 'https://picsum.photos/id/341/600/600', 1, 498, NOW()),
                                                                                                                                           ('太力真空收纳袋', '10件套，真空压缩，节省空间', 59.90, 500, 50403, 'https://picsum.photos/id/342/600/600', 1, 654, NOW());

-- 50404 雨伞
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('天堂伞全自动雨伞', '黑胶防晒，一键开合，晴雨两用', 59.90, 300, 50404, 'https://picsum.photos/id/343/600/600', 1, 456, NOW()),
                                                                                                                                           ('蕉下太阳伞', '黑胶防晒，超轻便携，防紫外线', 129.00, 200, 50404, 'https://picsum.photos/id/344/600/600', 1, 321, NOW()),
                                                                                                                                           ('小米米家自动雨伞', '全自动开合，黑胶防晒，晴雨两用', 79.90, 250, 50404, 'https://picsum.photos/id/345/600/600', 1, 389, NOW()),
                                                                                                                                           ('富仁雨伞', '手动折叠，黑胶防晒，晴雨两用', 29.90, 400, 50404, 'https://picsum.photos/id/346/600/600', 1, 567, NOW());

-- 50405 一次性用品
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('洁柔抽纸', '24包，3层120抽，原生木浆', 39.90, 1000, 50405, 'https://picsum.photos/id/347/600/600', 1, 1234, NOW()),
                                                                                                                                           ('维达卷纸', '27卷，4层140g，原生木浆', 49.90, 800, 50405, 'https://picsum.photos/id/348/600/600', 1, 1098, NOW()),
                                                                                                                                           ('心相印湿巾', '80片*3包，纯水配方，无酒精', 29.90, 600, 50405, 'https://picsum.photos/id/349/600/600', 1, 892, NOW()),
                                                                                                                                           ('妙洁一次性手套', '100只装，食品级PE，加厚耐用', 9.90, 1200, 50405, 'https://picsum.photos/id/350/600/600', 1, 1567, NOW());

-- 50501 灯具
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('欧普照明LED吸顶灯', '客厅灯，48W，调光调色，现代简约', 299.00, 150, 50501, 'https://picsum.photos/id/351/600/600', 1, 234, NOW()),
                                                                                                                                           ('雷士照明卧室吸顶灯', '24W，调光调色，北欧风格', 199.00, 200, 50501, 'https://picsum.photos/id/352/600/600', 1, 278, NOW()),
                                                                                                                                           ('飞利浦LED台灯', '护眼台灯，调光调色，学生专用', 159.00, 250, 50501, 'https://picsum.photos/id/353/600/600', 1, 321, NOW()),
                                                                                                                                           ('小米米家智能台灯', '护眼台灯，APP控制，语音控制', 199.00, 200, 50501, 'https://picsum.photos/id/354/600/600', 1, 289, NOW());

-- 50502 开关
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('公牛开关插座套装', '10只装，86型，白色，五孔插座', 99.00, 300, 50502, 'https://picsum.photos/id/355/600/600', 1, 456, NOW()),
                                                                                                                                           ('西门子开关插座', '单开双控，86型，白色', 29.90, 400, 50502, 'https://picsum.photos/id/356/600/600', 1, 567, NOW()),
                                                                                                                                           ('施耐德开关插座', '五孔插座，86型，金色', 39.90, 350, 50502, 'https://picsum.photos/id/357/600/600', 1, 498, NOW()),
                                                                                                                                           ('小米智能开关', '单开，WiFi连接，APP控制', 59.90, 200, 50502, 'https://picsum.photos/id/358/600/600', 1, 321, NOW());

-- 50503 五金
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('固特门锁', '室内门锁，不锈钢材质，静音设计', 129.00, 150, 50503, 'https://picsum.photos/id/359/600/600', 1, 189, NOW()),
                                                                                                                                           ('海蒂诗合页', '10只装，不锈钢材质，液压缓冲', 59.90, 200, 50503, 'https://picsum.photos/id/360/600/600', 1, 234, NOW()),
                                                                                                                                           ('DTC抽屉滑轨', '2副装，不锈钢材质，阻尼缓冲', 79.90, 180, 50503, 'https://picsum.photos/id/361/600/600', 1, 213, NOW()),
                                                                                                                                           ('公牛插排', '8插位，3米，过载保护', 59.90, 400, 50503, 'https://picsum.photos/id/362/600/600', 1, 567, NOW());

-- 50504 墙漆
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('立邦净味120乳胶漆', '5L，内墙漆，净味环保，白色', 299.00, 100, 50504, 'https://picsum.photos/id/363/600/600', 1, 123, NOW()),
                                                                                                                                           ('多乐士竹炭乳胶漆', '5L，内墙漆，竹炭净味，白色', 399.00, 80, 50504, 'https://picsum.photos/id/364/600/600', 1, 98, NOW()),
                                                                                                                                           ('三棵树健康漆', '5L，内墙漆，净味环保，可调色', 349.00, 90, 50504, 'https://picsum.photos/id/365/600/600', 1, 109, NOW()),
                                                                                                                                           ('华润乳胶漆', '5L，内墙漆，净味环保，白色', 259.00, 120, 50504, 'https://picsum.photos/id/366/600/600', 1, 156, NOW());

-- 50505 地板
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('圣象强化复合地板', '12mm，环保E0级，耐磨防水', 129.00, 200, 50505, 'https://picsum.photos/id/367/600/600', 1, 87, NOW()),
                                                                                                                                           ('大自然实木地板', '18mm，橡木材质，环保E0级', 399.00, 100, 50505, 'https://picsum.photos/id/368/600/600', 1, 56, NOW()),
                                                                                                                                           ('德尔实木复合地板', '15mm，环保E0级，耐磨防水', 229.00, 150, 50505, 'https://picsum.photos/id/369/600/600', 1, 76, NOW()),
                                                                                                                                           ('生活家巴洛克地板', '15mm，实木复合，仿古风格', 299.00, 80, 50505, 'https://picsum.photos/id/370/600/600', 1, 65, NOW());

-- ==================== 一级分类6：个护美妆 ====================
-- 60101 洁面
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('芙丽芳丝净润洗面霜', '100g，氨基酸配方，温和清洁', 99.00, 300, 60101, 'https://picsum.photos/id/371/600/600', 1, 567, NOW()),
                                                                                                                                           ('珂润润浸保湿洁颜泡沫', '150ml，氨基酸泡沫，敏感肌适用', 108.00, 250, 60101, 'https://picsum.photos/id/372/600/600', 1, 498, NOW()),
                                                                                                                                           ('旁氏米粹润泽洁面乳', '150g，氨基酸配方，性价比高', 29.90, 500, 60101, 'https://picsum.photos/id/373/600/600', 1, 892, NOW()),
                                                                                                                                           ('资生堂洗颜专科洁面乳', '120g，深层清洁，泡沫丰富', 45.00, 400, 60101, 'https://picsum.photos/id/374/600/600', 1, 654, NOW());

-- 60102 水乳
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('兰蔻粉水+清滢乳液套装', '水400ml+乳125ml，补水保湿', 699.00, 100, 60102, 'https://picsum.photos/id/375/600/600', 1, 234, NOW()),
                                                                                                                                           ('珂润保湿水乳套装', '水150ml+乳120ml，敏感肌适用', 329.00, 150, 60102, 'https://picsum.photos/id/376/600/600', 1, 278, NOW()),
                                                                                                                                           ('悦诗风吟绿茶水乳套装', '水200ml+乳160ml，清爽控油', 199.00, 200, 60102, 'https://picsum.photos/id/377/600/600', 1, 321, NOW()),
                                                                                                                                           ('SK-II神仙水+清莹露套装', '水230ml+露150ml，提亮肤色', 1599.00, 50, 60102, 'https://picsum.photos/id/378/600/600', 1, 123, NOW());

-- 60103 精华
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('雅诗兰黛小棕瓶精华', '50ml，修护抗老，保湿滋润', 699.00, 100, 60103, 'https://picsum.photos/id/379/600/600', 1, 234, NOW()),
                                                                                                                                           ('兰蔻小黑瓶精华', '50ml，肌底精华，修护维稳', 759.00, 90, 60103, 'https://picsum.photos/id/380/600/600', 1, 213, NOW()),
                                                                                                                                           ('SK-II小灯泡精华', '50ml，美白淡斑，提亮肤色', 1099.00, 60, 60103, 'https://picsum.photos/id/381/600/600', 1, 156, NOW()),
                                                                                                                                           ('欧莱雅黑精华', '50ml，肌底精华，抗初老', 259.00, 200, 60103, 'https://picsum.photos/id/382/600/600', 1, 321, NOW());

-- 60104 面霜
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('海蓝之谜经典面霜', '60ml，修护滋润，抗老紧致', 2399.00, 30, 60104, 'https://picsum.photos/id/383/600/600', 1, 65, NOW()),
                                                                                                                                           ('雅诗兰黛智妍面霜', '50ml，抗老紧致，保湿滋润', 699.00, 80, 60104, 'https://picsum.photos/id/384/600/600', 1, 123, NOW()),
                                                                                                                                           ('珂润保湿面霜', '40g，敏感肌适用，补水保湿', 159.00, 200, 60104, 'https://picsum.photos/id/385/600/600', 1, 278, NOW()),
                                                                                                                                           ('科颜氏高保湿面霜', '50ml，深层保湿，滋润修护', 299.00, 150, 60104, 'https://picsum.photos/id/386/600/600', 1, 234, NOW());

-- 60105 面膜
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('敷尔佳白膜医用面膜', '5片/盒，补水保湿，修护敏感肌', 129.00, 200, 60105, 'https://picsum.photos/id/387/600/600', 1, 321, NOW()),
                                                                                                                                           ('蒂佳婷蓝色药丸面膜', '5片/盒，深层补水，保湿滋润', 89.00, 250, 60105, 'https://picsum.photos/id/388/600/600', 1, 389, NOW()),
                                                                                                                                           ('兰蔻小黑瓶面膜', '5片/盒，精华面膜，修护维稳', 399.00, 100, 60105, 'https://picsum.photos/id/389/600/600', 1, 189, NOW()),
                                                                                                                                           ('自然堂喜马拉雅面膜', '21片/盒，补水保湿，多种功效', 99.00, 400, 60105, 'https://picsum.photos/id/390/600/600', 1, 567, NOW());

-- 60106 防晒
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('安热沙小金瓶防晒霜', '60ml，SPF50+，防水防汗', 229.00, 200, 60106, 'https://picsum.photos/id/391/600/600', 1, 321, NOW()),
                                                                                                                                           ('兰蔻小白管防晒霜', '50ml，SPF50+，清爽不油腻', 489.00, 100, 60106, 'https://picsum.photos/id/392/600/600', 1, 234, NOW()),
                                                                                                                                           ('资生堂蓝胖子防晒霜', '50ml，SPF50+，防水防汗', 389.00, 120, 60106, 'https://picsum.photos/id/393/600/600', 1, 213, NOW()),
                                                                                                                                           ('曼秀雷敦新碧防晒霜', '50g，SPF50+，清爽保湿', 69.90, 300, 60106, 'https://picsum.photos/id/394/600/600', 1, 456, NOW());

-- 60201 粉底
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('雅诗兰黛DW持妆粉底液', '30ml，持妆控油，遮瑕力强', 399.00, 150, 60201, 'https://picsum.photos/id/395/600/600', 1, 234, NOW()),
                                                                                                                                           ('兰蔻持妆粉底液', '30ml，持妆控油，轻薄服帖', 429.00, 120, 60201, 'https://picsum.photos/id/396/600/600', 1, 213, NOW()),
                                                                                                                                           ('阿玛尼蓝标大师粉底液', '30ml，轻薄服帖，提亮肤色', 589.00, 80, 60201, 'https://picsum.photos/id/397/600/600', 1, 156, NOW()),
                                                                                                                                           ('美宝莲Fitme粉底液', '30ml，持妆控油，性价比高', 89.00, 300, 60201, 'https://picsum.photos/id/398/600/600', 1, 456, NOW());

-- 60202 口红
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('迪奥999哑光口红', '3.5g，正红色，经典百搭', 329.00, 150, 60202, 'https://picsum.photos/id/399/600/600', 1, 278, NOW()),
                                                                                                                                           ('YSL小金条21号口红', '2.2g，复古正红，哑光质地', 359.00, 120, 60202, 'https://picsum.photos/id/400/600/600', 1, 256, NOW()),
                                                                                                                                           ('MAC子弹头Chili口红', '3g，小辣椒色，哑光质地', 179.00, 200, 60202, 'https://picsum.photos/id/401/600/600', 1, 321, NOW()),
                                                                                                                                           ('完美日记小细跟口红', '0.8g，丝绒质地，多色可选', 89.00, 300, 60202, 'https://picsum.photos/id/402/600/600', 1, 456, NOW());

-- 60203 眼影
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('3CE九宫格眼影盘', '8.1g，大地色系，日常百搭', 229.00, 150, 60203, 'https://picsum.photos/id/403/600/600', 1, 234, NOW()),
                                                                                                                                           ('TomFord四色眼影盘', '10g，经典大地色，粉质细腻', 699.00, 50, 60203, 'https://picsum.photos/id/404/600/600', 1, 87, NOW()),
                                                                                                                                           ('NYX16色眼影盘', '13.28g，大地色系，性价比高', 129.00, 200, 60203, 'https://picsum.photos/id/405/600/600', 1, 321, NOW()),
                                                                                                                                           ('完美日记动物眼影盘', '12色，粉质细腻，多色可选', 129.00, 180, 60203, 'https://picsum.photos/id/406/600/600', 1, 289, NOW());

-- 60204 眉笔
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('植村秀砍刀眉笔', '4g，防水防汗，自然持久', 209.00, 150, 60204, 'https://picsum.photos/id/407/600/600', 1, 234, NOW()),
                                                                                                                                           ('卡姿兰眉笔', '0.3g，双头设计，防水防汗', 39.90, 300, 60204, 'https://picsum.photos/id/408/600/600', 1, 456, NOW()),
                                                                                                                                           ('悦诗风吟眉笔', '0.3g，双头设计，自然持久', 29.90, 350, 60204, 'https://picsum.photos/id/409/600/600', 1, 498, NOW()),
                                                                                                                                           ('花西子眉笔', '0.08g，三角笔头，防水防汗', 69.90, 250, 60204, 'https://picsum.photos/id/410/600/600', 1, 389, NOW());

-- 60205 卸妆
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('贝德玛卸妆水', '500ml，粉水，温和清洁，敏感肌适用', 129.00, 300, 60205, 'https://picsum.photos/id/411/600/600', 1, 456, NOW()),
                                                                                                                                           ('曼丹眼唇卸妆液', '145ml，水油分离，温和不刺激', 59.90, 350, 60205, 'https://picsum.photos/id/412/600/600', 1, 498, NOW()),
                                                                                                                                           ('植村秀琥珀卸妆油', '450ml，深层清洁，养肤卸妆', 699.00, 80, 60205, 'https://picsum.photos/id/413/600/600', 1, 156, NOW()),
                                                                                                                                           ('完美日记白胖子卸妆水', '500ml，温和清洁，眼唇脸通用', 59.90, 400, 60205, 'https://picsum.photos/id/414/600/600', 1, 567, NOW());

-- 60301 沐浴露
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('舒肤佳纯白沐浴露', '720ml，温和清洁，长效抑菌', 29.90, 500, 60301, 'https://picsum.photos/id/415/600/600', 1, 654, NOW()),
                                                                                                                                           ('力士香氛沐浴露', '720ml，精油香氛，持久留香', 35.90, 450, 60301, 'https://picsum.photos/id/416/600/600', 1, 598, NOW()),
                                                                                                                                           ('多芬滋养沐浴露', '720ml，滋养保湿，温和清洁', 32.90, 480, 60301, 'https://picsum.photos/id/417/600/600', 1, 623, NOW()),
                                                                                                                                           ('欧舒丹樱花沐浴露', '250ml，樱花香氛，保湿滋润', 189.00, 150, 60301, 'https://picsum.photos/id/418/600/600', 1, 234, NOW());

-- 60302 身体乳
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('凡士林身体乳', '400ml，保湿滋润，秋冬适用', 39.90, 400, 60302, 'https://picsum.photos/id/419/600/600', 1, 567, NOW()),
                                                                                                                                           ('多芬身体乳', '300ml，滋养保湿，持久留香', 45.90, 350, 60302, 'https://picsum.photos/id/420/600/600', 1, 498, NOW()),
                                                                                                                                           ('欧舒丹樱花身体乳', '250ml，樱花香氛，保湿滋润', 229.00, 120, 60302, 'https://picsum.photos/id/421/600/600', 1, 189, NOW()),
                                                                                                                                           ('伊丽莎白雅顿绿茶身体乳', '500ml，绿茶香氛，清爽保湿', 159.00, 180, 60302, 'https://picsum.photos/id/422/600/600', 1, 234, NOW());

-- 60303 香皂
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('舒肤佳纯白香皂', '125g*3块，温和清洁，长效抑菌', 12.90, 600, 60303, 'https://picsum.photos/id/423/600/600', 1, 892, NOW()),
                                                                                                                                           ('力士香皂', '105g*4块，精油香氛，持久留香', 15.90, 550, 60303, 'https://picsum.photos/id/424/600/600', 1, 823, NOW()),
                                                                                                                                           ('滴露香皂', '125g*3块，抑菌消毒，温和清洁', 14.90, 500, 60303, 'https://picsum.photos/id/425/600/600', 1, 765, NOW()),
                                                                                                                                           ('欧舒丹乳木果香皂', '100g，天然乳木果，保湿滋润', 59.90, 200, 60303, 'https://picsum.photos/id/426/600/600', 1, 321, NOW());

-- 60304 脱毛
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('薇婷脱毛膏', '60g，温和脱毛，敏感肌适用', 39.90, 300, 60304, 'https://picsum.photos/id/427/600/600', 1, 456, NOW()),
                                                                                                                                           ('慕金脱毛仪', '家用激光脱毛仪，全身适用', 1299.00, 80, 60304, 'https://picsum.photos/id/428/600/600', 1, 123, NOW()),
                                                                                                                                           ('飞利浦脱毛仪', '家用激光脱毛仪，IPL技术', 1599.00, 60, 60304, 'https://picsum.photos/id/429/600/600', 1, 98, NOW()),
                                                                                                                                           ('吉列维纳斯脱毛刀', '女士剃毛刀，3层刀片，顺滑脱毛', 59.90, 250, 60304, 'https://picsum.photos/id/430/600/600', 1, 321, NOW());

-- 60401 牙膏
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('云南白药牙膏', '180g，减轻牙龈出血，清新口气', 39.90, 400, 60401, 'https://picsum.photos/id/431/600/600', 1, 567, NOW()),
                                                                                                                                           ('高露洁全面防蛀牙膏', '250g，防蛀固齿，清新口气', 19.90, 500, 60401, 'https://picsum.photos/id/432/600/600', 1, 654, NOW()),
                                                                                                                                           ('佳洁士3D炫白牙膏', '180g，美白牙齿，清新口气', 25.90, 450, 60401, 'https://picsum.photos/id/433/600/600', 1, 598, NOW()),
                                                                                                                                           ('黑人牙膏', '225g，双重薄荷，清新口气', 22.90, 480, 60401, 'https://picsum.photos/id/434/600/600', 1, 623, NOW());

-- 60402 牙刷
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('高露洁软毛牙刷', '4支装，软毛设计，清洁牙齿', 12.90, 500, 60402, 'https://picsum.photos/id/435/600/600', 1, 654, NOW()),
                                                                                                                                           ('佳洁士牙刷', '6支装，软毛设计，深入清洁', 15.90, 450, 60402, 'https://picsum.photos/id/436/600/600', 1, 598, NOW()),
                                                                                                                                           ('黑人牙刷', '4支装，软毛设计，护龈清洁', 14.90, 480, 60402, 'https://picsum.photos/id/437/600/600', 1, 623, NOW()),
                                                                                                                                           ('飞利浦电动牙刷头', '3支装，适配HX6730，清洁型', 99.00, 200, 60402, 'https://picsum.photos/id/438/600/600', 1, 321, NOW());

-- 60403 漱口水
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('李施德林冰蓝漱口水', '500ml，清新口气，杀菌消毒', 29.90, 300, 60403, 'https://picsum.photos/id/439/600/600', 1, 456, NOW()),
                                                                                                                                           ('高露洁贝齿漱口水', '500ml，清新口气，防蛀固齿', 25.90, 350, 60403, 'https://picsum.photos/id/440/600/600', 1, 498, NOW()),
                                                                                                                                           ('佳洁士漱口水', '500ml，清新口气，美白牙齿', 22.90, 400, 60403, 'https://picsum.photos/id/441/600/600', 1, 567, NOW()),
                                                                                                                                           ('参半益生菌漱口水', '500ml，益生菌配方，清新口气', 39.90, 250, 60403, 'https://picsum.photos/id/442/600/600', 1, 389, NOW());

-- 60404 牙线
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('欧乐B牙线', '50m，薄荷味，清洁牙缝', 19.90, 400, 60404, 'https://picsum.photos/id/443/600/600', 1, 567, NOW()),
                                                                                                                                           ('屈臣氏牙线棒', '50支*3盒，独立包装，便携', 25.90, 350, 60404, 'https://picsum.photos/id/444/600/600', 1, 498, NOW()),
                                                                                                                                           ('小鹿妈妈牙线棒', '100支*3盒，细滑牙线，清洁牙缝', 15.90, 500, 60404, 'https://picsum.photos/id/445/600/600', 1, 654, NOW()),
                                                                                                                                           ('3M牙线棒', '150支，细滑牙线，独立包装', 39.90, 300, 60404, 'https://picsum.photos/id/446/600/600', 1, 456, NOW());

-- 60501 男士香水
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('香奈儿蔚蓝男士香水', '50ml，木质香调，持久留香', 799.00, 80, 60501, 'https://picsum.photos/id/447/600/600', 1, 123, NOW()),
                                                                                                                                           ('迪奥旷野男士香水', '60ml，清新木质香调，持久留香', 699.00, 90, 60501, 'https://picsum.photos/id/448/600/600', 1, 134, NOW()),
                                                                                                                                           ('宝格丽大吉岭茶男士香水', '100ml，茶香调，清新淡雅', 599.00, 100, 60501, 'https://picsum.photos/id/449/600/600', 1, 156, NOW()),
                                                                                                                                           ('爱马仕大地男士香水', '50ml，木质香调，成熟稳重', 659.00, 85, 60501, 'https://picsum.photos/id/450/600/600', 1, 119, NOW());

-- 60502 女士香水
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('香奈儿5号香水', '50ml，经典花香调，优雅迷人', 1299.00, 60, 60502, 'https://picsum.photos/id/451/600/600', 1, 87, NOW()),
                                                                                                                                           ('迪奥真我香水', '50ml，花香调，优雅高贵', 899.00, 70, 60502, 'https://picsum.photos/id/452/600/600', 1, 98, NOW()),
                                                                                                                                           ('祖玛珑蓝风铃香水', '30ml，清新花香调，自然淡雅', 499.00, 100, 60502, 'https://picsum.photos/id/453/600/600', 1, 156, NOW()),
                                                                                                                                           ('YSL反转巴黎香水', '50ml，花果香调，甜美浪漫', 799.00, 80, 60502, 'https://picsum.photos/id/454/600/600', 1, 123, NOW());

-- 60503 中性香水
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('祖玛珑英国梨与小苍兰香水', '30ml，果香调，清新自然', 499.00, 100, 60503, 'https://picsum.photos/id/455/600/600', 1, 156, NOW()),
                                                                                                                                           ('芦丹氏柏林少女香水', '50ml，玫瑰香调，个性独特', 699.00, 60, 60503, 'https://picsum.photos/id/456/600/600', 1, 87, NOW()),
                                                                                                                                           ('三宅一生一生之水香水', '100ml，水生花香调，清新淡雅', 599.00, 80, 60503, 'https://picsum.photos/id/457/600/600', 1, 123, NOW()),
                                                                                                                                           ('CK One中性香水', '100ml，柑橘香调，清新自然', 299.00, 150, 60503, 'https://picsum.photos/id/458/600/600', 1, 234, NOW());
-- ==================== 一级分类7：母婴玩具 ====================
-- 70101 婴幼儿奶粉
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('飞鹤星飞帆A2奶粉 3段 700g', 'A2奶源，OPO结构脂，适合1-3岁宝宝', 368.00, 200, 70101, 'https://picsum.photos/id/459/600/600', 1, 456, NOW()),
                                                                                                                                           ('爱他美卓萃奶粉 3段 900g', '欧洲原装进口，NuMMOs模拟母乳低聚糖', 345.00, 180, 70101, 'https://picsum.photos/id/460/600/600', 1, 421, NOW()),
                                                                                                                                           ('皇家美素佳儿奶粉 3段 800g', '荷兰原装进口，乳铁蛋白，天然乳脂', 398.00, 150, 70101, 'https://picsum.photos/id/461/600/600', 1, 389, NOW()),
                                                                                                                                           ('伊利金领冠珍护奶粉 3段 900g', '中国专利配方，OPO结构脂，益生菌', 318.00, 220, 70101, 'https://picsum.photos/id/462/600/600', 1, 498, NOW());

-- 70102 孕妇奶粉
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('安满智孕宝孕妇奶粉 800g', '新西兰原装进口，叶酸+DHA，孕期营养', 198.00, 100, 70102, 'https://picsum.photos/id/463/600/600', 1, 234, NOW()),
                                                                                                                                           ('美赞臣安婴妈妈孕妇奶粉 900g', 'DHA+叶酸，孕期哺乳期适用', 178.00, 120, 70102, 'https://picsum.photos/id/464/600/600', 1, 213, NOW()),
                                                                                                                                           ('惠氏启韵孕妇奶粉 800g', '爱尔兰原装进口，低脂配方，铁元素', 268.00, 80, 70102, 'https://picsum.photos/id/465/600/600', 1, 189, NOW()),
                                                                                                                                           ('伊利金领冠孕妇奶粉 900g', '叶酸+DHA，益生菌，孕期营养', 158.00, 150, 70102, 'https://picsum.photos/id/466/600/600', 1, 278, NOW());

-- 70103 DHA
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('纽曼斯DHA藻油软胶囊 60粒', '美国进口，纯藻油，婴幼儿适用', 398.00, 100, 70103, 'https://picsum.photos/id/467/600/600', 1, 189, NOW()),
                                                                                                                                           ('Bio Island婴幼儿DHA 60粒', '澳大利亚进口，海藻油，宝宝专用', 168.00, 150, 70103, 'https://picsum.photos/id/468/600/600', 1, 234, NOW()),
                                                                                                                                           ('汤臣倍健DHA藻油软胶囊 60粒', '国产大牌，纯藻油，儿童适用', 128.00, 200, 70103, 'https://picsum.photos/id/469/600/600', 1, 278, NOW()),
                                                                                                                                           ('惠氏玛特纳DHA藻油软胶囊 30粒', '美国进口，高纯度，孕期哺乳期适用', 198.00, 120, 70103, 'https://picsum.photos/id/470/600/600', 1, 213, NOW());

-- 70104 钙铁锌
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('童年时光钙镁锌口服液 473ml', '美国进口，液体钙，好吸收', 168.00, 150, 70104, 'https://picsum.photos/id/471/600/600', 1, 234, NOW()),
                                                                                                                                           ('伊可新维生素AD滴剂 30粒', '0-1岁适用，促进钙吸收', 39.90, 300, 70104, 'https://picsum.photos/id/472/600/600', 1, 456, NOW()),
                                                                                                                                           ('迪巧小儿碳酸钙D3颗粒 10袋', '淡奶味，易冲调，宝宝喜欢', 49.90, 250, 70104, 'https://picsum.photos/id/473/600/600', 1, 421, NOW()),
                                                                                                                                           ('三精葡萄糖酸锌口服液 12支', '蓝瓶锌，口感好，儿童补锌', 29.90, 350, 70104, 'https://picsum.photos/id/474/600/600', 1, 498, NOW());

-- 70201 纸尿裤
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('帮宝适一级帮纸尿裤 L码 52片', '日本进口，超薄透气，瞬吸干爽', 128.00, 300, 70201, 'https://picsum.photos/id/475/600/600', 1, 567, NOW()),
                                                                                                                                           ('花王妙而舒纸尿裤 L码 54片', '日本原装进口，三倍透气，防侧漏', 148.00, 250, 70201, 'https://picsum.photos/id/476/600/600', 1, 523, NOW()),
                                                                                                                                           ('好奇铂金装纸尿裤 L码 50片', '超薄透气，柔软亲肤，3D立体防漏', 138.00, 280, 70201, 'https://picsum.photos/id/477/600/600', 1, 545, NOW()),
                                                                                                                                           ('尤妮佳皇家佑肌纸尿裤 L码 44片', '贵族棉，柔软亲肤，弱酸性表层', 168.00, 200, 70201, 'https://picsum.photos/id/478/600/600', 1, 489, NOW());

-- 70202 拉拉裤
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('帮宝适一级帮拉拉裤 XL码 40片', '日本进口，超薄透气，穿脱方便', 138.00, 280, 70202, 'https://picsum.photos/id/479/600/600', 1, 523, NOW()),
                                                                                                                                           ('花王妙而舒拉拉裤 XL码 38片', '日本原装进口，三倍透气，防侧漏', 158.00, 230, 70202, 'https://picsum.photos/id/480/600/600', 1, 489, NOW()),
                                                                                                                                           ('好奇铂金装拉拉裤 XL码 34片', '超薄透气，柔软亲肤，3D立体防漏', 148.00, 260, 70202, 'https://picsum.photos/id/481/600/600', 1, 501, NOW()),
                                                                                                                                           ('尤妮佳皇家佑肌拉拉裤 XL码 32片', '贵族棉，柔软亲肤，弱酸性表层', 178.00, 180, 70202, 'https://picsum.photos/id/482/600/600', 1, 456, NOW());

-- 70203 婴儿湿巾
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('好奇纯水婴儿湿巾 80抽*6包', '99%纯水，无添加，柔软亲肤', 69.90, 400, 70203, 'https://picsum.photos/id/483/600/600', 1, 654, NOW()),
                                                                                                                                           ('全棉时代婴儿湿巾 80抽*6包', '纯棉材质，无酒精，温和清洁', 89.90, 350, 70203, 'https://picsum.photos/id/484/600/600', 1, 598, NOW()),
                                                                                                                                           ('维达婴儿湿巾 80抽*8包', '纯水配方，无添加，柔软厚实', 59.90, 450, 70203, 'https://picsum.photos/id/485/600/600', 1, 723, NOW()),
                                                                                                                                           ('贝亲婴儿湿巾 80抽*6包', '弱酸性配方，温和清洁，无酒精', 79.90, 380, 70203, 'https://picsum.photos/id/486/600/600', 1, 623, NOW());

-- 70301 奶瓶
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('贝亲宽口径玻璃奶瓶 240ml', '玻璃材质，仿母乳奶嘴，防胀气', 89.90, 200, 70301, 'https://picsum.photos/id/487/600/600', 1, 321, NOW()),
                                                                                                                                           ('新安怡自然系列奶瓶 260ml', 'PP材质，防胀气，仿母乳设计', 79.90, 220, 70301, 'https://picsum.photos/id/488/600/600', 1, 298, NOW()),
                                                                                                                                           ('可么多么硅胶奶瓶 250ml', '硅胶材质，仿母乳质感，防摔', 168.00, 150, 70301, 'https://picsum.photos/id/489/600/600', 1, 234, NOW()),
                                                                                                                                           ('NUK宽口径PP奶瓶 300ml', '德国进口，防胀气奶嘴，耐高温', 69.90, 250, 70301, 'https://picsum.photos/id/490/600/600', 1, 356, NOW());

-- 70302 吸奶器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('美德乐丝韵翼双边电动吸奶器', '瑞士进口，双韵律，无痛吸乳', 1999.00, 50, 70302, 'https://picsum.photos/id/491/600/600', 1, 87, NOW()),
                                                                                                                                           ('新安怡自然系列双边电动吸奶器', '英国进口，自然韵律，按摩吸乳', 1299.00, 80, 70302, 'https://picsum.photos/id/492/600/600', 1, 123, NOW()),
                                                                                                                                           ('贝亲电动吸奶器', '单边电动，多档调节，静音设计', 399.00, 150, 70302, 'https://picsum.photos/id/493/600/600', 1, 234, NOW()),
                                                                                                                                           ('小白熊电动吸奶器', '单边电动，锂电池充电，便携设计', 299.00, 200, 70302, 'https://picsum.photos/id/494/600/600', 1, 278, NOW());

-- 70303 餐椅
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('好孩子儿童餐椅', '可折叠，多档调节，可拆卸餐盘', 299.00, 100, 70303, 'https://picsum.photos/id/495/600/600', 1, 189, NOW()),
                                                                                                                                           ('费雪多功能儿童餐椅', '可调节高度，可拆卸餐盘，安全稳固', 399.00, 80, 70303, 'https://picsum.photos/id/496/600/600', 1, 156, NOW()),
                                                                                                                                           ('宜家安迪洛儿童餐椅', '简约设计，易清洁，安全稳固', 99.00, 200, 70303, 'https://picsum.photos/id/497/600/600', 1, 321, NOW()),
                                                                                                                                           ('可优比儿童餐椅', '可折叠，多档调节，PU坐垫', 259.00, 120, 70303, 'https://picsum.photos/id/498/600/600', 1, 213, NOW());

-- 70304 辅食机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('飞利浦新安怡辅食机', '蒸煮搅拌一体，多功能，易清洁', 699.00, 80, 70304, 'https://picsum.photos/id/499/600/600', 1, 123, NOW()),
                                                                                                                                           ('小熊辅食机', '蒸煮搅拌一体，迷你容量，宝宝专用', 199.00, 200, 70304, 'https://picsum.photos/id/500/600/600', 1, 278, NOW()),
                                                                                                                                           ('贝亲辅食机', '蒸煮搅拌一体，多档调节，易清洁', 399.00, 150, 70304, 'https://picsum.photos/id/501/600/600', 1, 234, NOW()),
                                                                                                                                           ('九阳辅食机', '蒸煮搅拌一体，不锈钢内胆，安全健康', 259.00, 180, 70304, 'https://picsum.photos/id/502/600/600', 1, 213, NOW());

-- 70401 婴儿洗发水
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('贝亲婴儿洗发水 500ml', '无泪配方，温和清洁，植物精华', 49.90, 300, 70401, 'https://picsum.photos/id/503/600/600', 1, 456, NOW()),
                                                                                                                                           ('艾维诺婴儿洗发水 354ml', '燕麦精华，温和保湿，无泪配方', 89.90, 250, 70401, 'https://picsum.photos/id/504/600/600', 1, 421, NOW()),
                                                                                                                                           ('施巴婴儿洗发水 200ml', '弱酸性配方，温和清洁，保护头皮', 69.90, 280, 70401, 'https://picsum.photos/id/505/600/600', 1, 389, NOW()),
                                                                                                                                           ('红色小象婴儿洗发水 500ml', '植物精华，无泪配方，温和清洁', 39.90, 350, 70401, 'https://picsum.photos/id/506/600/600', 1, 498, NOW());

-- 70402 护臀膏
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('贝亲婴儿护臀膏 35g', '氧化锌配方，预防红屁屁，温和滋润', 29.90, 300, 70402, 'https://picsum.photos/id/507/600/600', 1, 456, NOW()),
                                                                                                                                           ('屁屁乐护臀膏 60g', '专业护臀，预防红屁屁，天然成分', 39.90, 250, 70402, 'https://picsum.photos/id/508/600/600', 1, 421, NOW()),
                                                                                                                                           ('Desitin护臀膏 113g', '美国进口，氧化锌配方，快速修复', 69.90, 200, 70402, 'https://picsum.photos/id/509/600/600', 1, 389, NOW()),
                                                                                                                                           ('新安怡婴儿护臀膏 50ml', '天然成分，温和滋润，预防红屁屁', 49.90, 280, 70402, 'https://picsum.photos/id/510/600/600', 1, 498, NOW());

-- 70403 安全座椅
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('宝得适百变骑士安全座椅', '9个月-12岁适用，ISOFIX接口，五点式安全带', 2999.00, 50, 70403, 'https://picsum.photos/id/511/600/600', 1, 87, NOW()),
                                                                                                                                           ('好孩子安全座椅', '0-12岁适用，360度旋转，ISOFIX接口', 1999.00, 80, 70403, 'https://picsum.photos/id/512/600/600', 1, 123, NOW()),
                                                                                                                                           ('猫头鹰卢娜安全座椅', '0-7岁适用，360度旋转，ISOFIX接口', 1599.00, 100, 70403, 'https://picsum.photos/id/513/600/600', 1, 156, NOW()),
                                                                                                                                           ('迈可适安全座椅', '0-4岁适用，反向安装，ISOFIX接口', 1299.00, 120, 70403, 'https://picsum.photos/id/514/600/600', 1, 189, NOW());

-- 70404 围栏
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('可优比儿童游戏围栏', '16+2片，安全稳固，易安装', 599.00, 100, 70404, 'https://picsum.photos/id/515/600/600', 1, 189, NOW()),
                                                                                                                                           ('澳乐儿童游戏围栏', '14+2片，环保材质，安全稳固', 499.00, 120, 70404, 'https://picsum.photos/id/516/600/600', 1, 213, NOW()),
                                                                                                                                           ('费雪儿童游戏围栏', '12+2片，折叠设计，易收纳', 699.00, 80, 70404, 'https://picsum.photos/id/517/600/600', 1, 156, NOW()),
                                                                                                                                           ('曼龙儿童游戏围栏', '16+2片，环保材质，卡通设计', 559.00, 90, 70404, 'https://picsum.photos/id/518/600/600', 1, 176, NOW());

-- 70501 积木
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('乐高经典创意积木 10698', '790块，大颗粒，适合4岁以上', 299.00, 150, 70501, 'https://picsum.photos/id/519/600/600', 1, 234, NOW()),
                                                                                                                                           ('费雪大颗粒积木 80块', '大颗粒，安全无毒，适合1-3岁', 99.00, 250, 70501, 'https://picsum.photos/id/520/600/600', 1, 389, NOW()),
                                                                                                                                           ('布鲁可大颗粒积木', '百变布鲁克，遥控积木，适合3-6岁', 199.00, 200, 70501, 'https://picsum.photos/id/521/600/600', 1, 321, NOW()),
                                                                                                                                           ('木玩世家木质积木 100块', '实木材质，环保水性漆，适合2-6岁', 129.00, 180, 70501, 'https://picsum.photos/id/522/600/600', 1, 278, NOW());

-- 70502 遥控车
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('乐高机械组遥控跑车 42125', '法拉利488GTE，遥控功能，1677块', 1299.00, 50, 70502, 'https://picsum.photos/id/523/600/600', 1, 87, NOW()),
                                                                                                                                           ('星辉遥控车 宝马X6', '1:14比例，遥控功能，仿真设计', 199.00, 150, 70502, 'https://picsum.photos/id/524/600/600', 1, 234, NOW()),
                                                                                                                                           ('美致遥控越野车', '四驱越野，大轮胎，攀爬能力强', 299.00, 120, 70502, 'https://picsum.photos/id/525/600/600', 1, 189, NOW()),
                                                                                                                                           ('奥迪双钻遥控赛车', '高速赛车，漂移功能，充电电池', 159.00, 180, 70502, 'https://picsum.photos/id/526/600/600', 1, 213, NOW());

-- 70503 毛绒玩具
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('迪士尼米奇毛绒玩具 30cm', '正版授权，柔软亲肤，可爱造型', 99.00, 200, 70503, 'https://picsum.photos/id/527/600/600', 1, 321, NOW()),
                                                                                                                                           ('LINE FRIENDS布朗熊毛绒玩具 40cm', '正版授权，柔软亲肤，可爱造型', 129.00, 180, 70503, 'https://picsum.photos/id/528/600/600', 1, 289, NOW()),
                                                                                                                                           ('Jellycat邦尼兔毛绒玩具 31cm', '英国进口，柔软亲肤，安抚玩具', 229.00, 100, 70503, 'https://picsum.photos/id/529/600/600', 1, 189, NOW()),
                                                                                                                                           ('费雪安抚海马', '声光安抚，柔软亲肤，宝宝安睡', 89.90, 250, 70503, 'https://picsum.photos/id/530/600/600', 1, 389, NOW());

-- ==================== 一级分类7：母婴玩具（剩余部分） ====================
-- 70504 益智玩具（修复并完成）
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('费雪探索学习六面盒', '多功能益智，双语学习，适合6-36个月', 199.00, 150, 70504, 'https://picsum.photos/id/531/600/600', 1, 234, NOW()),
                                                                                                                                           ('澳贝益智探索电子锤', '声光玩具，益智启蒙，适合1-3岁', 69.90, 250, 70504, 'https://picsum.photos/id/532/600/600', 1, 389, NOW()),
                                                                                                                                           ('Hape木质积木拼图', '环保材质，形状认知，适合2-4岁', 89.90, 200, 70504, 'https://picsum.photos/id/533/600/600', 1, 321, NOW()),
                                                                                                                                           ('伟易达双语学习桌', '多功能学习桌，双语教学，适合1-3岁', 399.00, 100, 70504, 'https://picsum.photos/id/534/600/600', 1, 189, NOW());

-- ==================== 一级分类8：食品生鲜 ====================
-- 80101 坚果
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('三只松鼠每日坚果 750g/30包', '混合坚果，独立包装，每日营养', 89.90, 500, 80101, 'https://picsum.photos/id/535/600/600', 1, 892, NOW()),
                                                                                                                                           ('良品铺子夏威夷果 200g', '奶油味，大颗粒，开口易剥', 29.90, 600, 80101, 'https://picsum.photos/id/536/600/600', 1, 765, NOW()),
                                                                                                                                           ('百草味巴旦木 180g', '盐焗味，薄壳大果，香脆可口', 25.90, 550, 80101, 'https://picsum.photos/id/537/600/600', 1, 823, NOW()),
                                                                                                                                           ('洽洽碧根果 200g', '奶香味，手剥薄壳，营养丰富', 32.90, 480, 80101, 'https://picsum.photos/id/538/600/600', 1, 698, NOW());

-- 80102 饼干
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('奥利奥原味夹心饼干 388g', '经典原味，夹心酥脆，独立包装', 24.90, 600, 80102, 'https://picsum.photos/id/539/600/600', 1, 923, NOW()),
                                                                                                                                           ('太平梳打饼干 香葱味 400g', '低糖低盐，酥脆可口，早餐零食', 19.90, 650, 80102, 'https://picsum.photos/id/540/600/600', 1, 876, NOW()),
                                                                                                                                           ('趣多多巧克力曲奇 340g', '巧克力豆，酥脆香甜，独立包装', 29.90, 550, 80102, 'https://picsum.photos/id/541/600/600', 1, 789, NOW()),
                                                                                                                                           ('丹麦蓝罐曲奇 908g', '丹麦进口，黄油曲奇，礼盒装', 89.90, 300, 80102, 'https://picsum.photos/id/542/600/600', 1, 456, NOW());

-- 80103 巧克力
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('德芙丝滑牛奶巧克力 252g', '丝滑口感，独立包装，分享装', 39.90, 500, 80103, 'https://picsum.photos/id/543/600/600', 1, 765, NOW()),
                                                                                                                                           ('费列罗榛果威化巧克力 30粒', '意大利进口，榛果夹心，礼盒装', 129.00, 300, 80103, 'https://picsum.photos/id/544/600/600', 1, 421, NOW()),
                                                                                                                                           ('明治雪吻巧克力 62g', '日本进口，入口即化，多种口味', 29.90, 400, 80103, 'https://picsum.photos/id/545/600/600', 1, 567, NOW()),
                                                                                                                                           ('瑞士莲软心巧克力 200g', '瑞士进口，软心夹心，混合口味', 69.90, 250, 80103, 'https://picsum.photos/id/546/600/600', 1, 389, NOW());

-- 80104 肉干
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('三只松鼠猪肉脯 150g', '靖江风味，肉质紧实，香甜可口', 29.90, 450, 80104, 'https://picsum.photos/id/547/600/600', 1, 567, NOW()),
                                                                                                                                           ('良品铺子牛肉干 100g', '内蒙古风味，手撕牛肉，香辣味', 39.90, 400, 80104, 'https://picsum.photos/id/548/600/600', 1, 498, NOW()),
                                                                                                                                           ('百草味鸭脖 170g', '卤味熟食，香辣入味，独立包装', 24.90, 500, 80104, 'https://picsum.photos/id/549/600/600', 1, 623, NOW()),
                                                                                                                                           ('周黑鸭鸭翅 180g', '武汉特产，麻辣鲜香，真空包装', 32.90, 350, 80104, 'https://picsum.photos/id/550/600/600', 1, 456, NOW());

-- 80105 海味
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('三只松鼠鱿鱼丝 88g', '手撕鱿鱼，鲜香有嚼劲，海鲜零食', 22.90, 400, 80105, 'https://picsum.photos/id/551/600/600', 1, 456, NOW()),
                                                                                                                                           ('良品铺子烤鱼片 60g', '深海鳕鱼片，鲜香酥脆，无添加', 25.90, 350, 80105, 'https://picsum.photos/id/552/600/600', 1, 421, NOW()),
                                                                                                                                           ('百草味海带结 200g', '酸辣味，爽脆可口，独立包装', 19.90, 450, 80105, 'https://picsum.photos/id/553/600/600', 1, 523, NOW()),
                                                                                                                                           ('来伊份小黄鱼 125g', '香酥小黄鱼，即食海鲜，酥脆可口', 29.90, 300, 80105, 'https://picsum.photos/id/554/600/600', 1, 389, NOW());

-- 80201 米
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('金龙鱼五常大米 5kg', '五常稻花香，软糯香甜，真空包装', 69.90, 300, 80201, 'https://picsum.photos/id/555/600/600', 1, 456, NOW()),
                                                                                                                                           ('福临门东北大米 10kg', '东北优质大米，颗粒饱满，口感好', 59.90, 250, 80201, 'https://picsum.photos/id/556/600/600', 1, 421, NOW()),
                                                                                                                                           ('十月稻田长粒香米 5kg', '东北长粒香，清香四溢，真空包装', 49.90, 350, 80201, 'https://picsum.photos/id/557/600/600', 1, 523, NOW()),
                                                                                                                                           ('泰国香米 5kg', '泰国进口，茉莉香米，香气浓郁', 79.90, 200, 80201, 'https://picsum.photos/id/558/600/600', 1, 321, NOW());

-- 80202 面
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('金龙鱼鸡蛋挂面 1kg', '鸡蛋和面，劲道爽滑，易煮不烂', 12.90, 500, 80202, 'https://picsum.photos/id/559/600/600', 1, 654, NOW()),
                                                                                                                                           ('康师傅红烧牛肉面 5包', '经典口味，方便快捷，速食泡面', 14.90, 600, 80202, 'https://picsum.photos/id/560/600/600', 1, 789, NOW()),
                                                                                                                                           ('统一老坛酸菜牛肉面 5包', '酸爽过瘾，经典口味，速食泡面', 14.90, 550, 80202, 'https://picsum.photos/id/561/600/600', 1, 723, NOW()),
                                                                                                                                           ('陈克明挂面 1kg', '精细挂面，劲道爽滑，多种宽度', 11.90, 450, 80202, 'https://picsum.photos/id/562/600/600', 1, 598, NOW());

-- 80203 油
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('金龙鱼非转基因大豆油 5L', '非转基因，物理压榨，健康食用油', 69.90, 200, 80203, 'https://picsum.photos/id/563/600/600', 1, 321, NOW()),
                                                                                                                                           ('鲁花5S压榨花生油 5L', '物理压榨，香味浓郁，一级花生油', 139.90, 150, 80203, 'https://picsum.photos/id/564/600/600', 1, 278, NOW()),
                                                                                                                                           ('福临门玉米油 5L', '非转基因，压榨玉米油，清淡健康', 79.90, 180, 80203, 'https://picsum.photos/id/565/600/600', 1, 298, NOW()),
                                                                                                                                           ('欧丽薇兰橄榄油 1L', '意大利进口，特级初榨，凉拌烹饪', 89.90, 120, 80203, 'https://picsum.photos/id/566/600/600', 1, 234, NOW());

-- 80204 酱油
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('海天金标生抽 500ml', '酿造酱油，鲜味浓郁，炒菜凉拌', 9.90, 500, 80204, 'https://picsum.photos/id/567/600/600', 1, 654, NOW()),
                                                                                                                                           ('李锦记老抽 500ml', '酿造老抽，上色红亮，红烧专用', 10.90, 450, 80204, 'https://picsum.photos/id/568/600/600', 1, 598, NOW()),
                                                                                                                                           ('厨邦美味鲜酱油 1.25L', '酿造酱油，鲜味十足，大瓶装', 19.90, 400, 80204, 'https://picsum.photos/id/569/600/600', 1, 523, NOW()),
                                                                                                                                           ('千禾零添加酱油 500ml', '零添加，酿造酱油，健康调味', 15.90, 350, 80204, 'https://picsum.photos/id/570/600/600', 1, 456, NOW());

-- 80205 醋
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('恒顺香醋 500ml', '镇江香醋，酸香浓郁，凉拌蘸料', 8.90, 500, 80205, 'https://picsum.photos/id/571/600/600', 1, 567, NOW()),
                                                                                                                                           ('山西老陈醋 500ml', '山西特产，陈酿香醋，醇厚香浓', 9.90, 450, 80205, 'https://picsum.photos/id/572/600/600', 1, 523, NOW()),
                                                                                                                                           ('海天白醋 500ml', '酿造白醋，酸度适中，清洁烹饪', 7.90, 400, 80205, 'https://picsum.photos/id/573/600/600', 1, 498, NOW()),
                                                                                                                                           ('保宁醋 500ml', '四川保宁醋，酸辣粉专用，香气独特', 10.90, 350, 80205, 'https://picsum.photos/id/574/600/600', 1, 421, NOW());

-- 80206 火锅底料
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('海底捞牛油火锅底料 150g', '正宗川味，麻辣鲜香，一人份', 12.90, 400, 80206, 'https://picsum.photos/id/575/600/600', 1, 456, NOW()),
                                                                                                                                           ('德庄清油火锅底料 300g', '重庆风味，麻辣适中，不油腻', 19.90, 350, 80206, 'https://picsum.photos/id/576/600/600', 1, 421, NOW()),
                                                                                                                                           ('小肥羊清汤火锅底料 160g', '内蒙古风味，鲜香不辣，骨汤熬制', 15.90, 300, 80206, 'https://picsum.photos/id/577/600/600', 1, 389, NOW()),
                                                                                                                                           ('桥头番茄火锅底料 200g', '酸甜可口，番茄味浓，不辣火锅', 14.90, 320, 80206, 'https://picsum.photos/id/578/600/600', 1, 356, NOW());

-- 80301 牛奶
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('伊利纯牛奶 250ml*24盒', '全脂纯牛奶，营养丰富，早餐必备', 69.90, 400, 80301, 'https://picsum.photos/id/579/600/600', 1, 789, NOW()),
                                                                                                                                           ('蒙牛特仑苏纯牛奶 250ml*12盒', '高端纯牛奶，品质保证，礼盒装', 59.90, 350, 80301, 'https://picsum.photos/id/580/600/600', 1, 654, NOW()),
                                                                                                                                           ('光明纯牛奶 250ml*24盒', '上海老字号，新鲜牛奶，口感醇厚', 65.90, 380, 80301, 'https://picsum.photos/id/581/600/600', 1, 723, NOW()),
                                                                                                                                           ('三元极致纯牛奶 250ml*12盒', '北京三元，高品质牛奶，营养健康', 55.90, 300, 80301, 'https://picsum.photos/id/582/600/600', 1, 598, NOW());

-- 80302 咖啡
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('雀巢速溶咖啡 1+2原味 100条', '经典原味，即冲即饮，提神醒脑', 89.90, 500, 80302, 'https://picsum.photos/id/583/600/600', 1, 892, NOW()),
                                                                                                                                           ('星巴克速溶咖啡 中度烘焙 10条', '星巴克品质，即溶咖啡，香醇浓郁', 39.90, 300, 80302, 'https://picsum.photos/id/584/600/600', 1, 456, NOW()),
                                                                                                                                           ('麦斯威尔特浓咖啡 100条', '特浓口味，提神效果好，独立包装', 79.90, 400, 80302, 'https://picsum.photos/id/585/600/600', 1, 654, NOW()),
                                                                                                                                           ('隅田川挂耳咖啡 20包', '现磨口感，挂耳式，多种口味', 49.90, 350, 80302, 'https://picsum.photos/id/586/600/600', 1, 523, NOW());

-- 80303 茶
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('立顿黄牌红茶 25包', '经典红茶，袋泡茶，方便快捷', 19.90, 500, 80303, 'https://picsum.photos/id/587/600/600', 1, 654, NOW()),
                                                                                                                                           ('西湖龙井茶叶 100g', '杭州特产，明前龙井，清香淡雅', 129.00, 200, 80303, 'https://picsum.photos/id/588/600/600', 1, 234, NOW()),
                                                                                                                                           ('铁观音茶叶 250g', '安溪铁观音，兰花香，回甘持久', 89.90, 250, 80303, 'https://picsum.photos/id/589/600/600', 1, 321, NOW()),
                                                                                                                                           ('普洱茶熟茶 357g', '云南普洱，陈香浓郁，茶饼包装', 69.90, 180, 80303, 'https://picsum.photos/id/590/600/600', 1, 278, NOW());

-- 80304 果汁
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('农夫山泉100%橙汁 1L*12瓶', '100%纯果汁，不添加糖，营养健康', 89.90, 300, 80304, 'https://picsum.photos/id/591/600/600', 1, 456, NOW()),
                                                                                                                                           ('汇源果汁 1L*6盒', '多种口味，浓缩果汁，家庭装', 49.90, 350, 80304, 'https://picsum.photos/id/592/600/600', 1, 421, NOW()),
                                                                                                                                           ('果粒橙 450ml*15瓶', '含真实果粒，橙汁饮料，酸甜可口', 45.90, 400, 80304, 'https://picsum.photos/id/593/600/600', 1, 523, NOW()),
                                                                                                                                           ('味全每日C橙汁 300ml*12瓶', '冷藏果汁，新鲜口感，每日补充维C', 59.90, 250, 80304, 'https://picsum.photos/id/594/600/600', 1, 389, NOW());

-- 80305 碳酸饮料
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('可口可乐 330ml*24罐', '经典可乐，畅爽解渴，碳酸饮料', 49.90, 500, 80305, 'https://picsum.photos/id/595/600/600', 1, 892, NOW()),
                                                                                                                                           ('百事可乐 330ml*24罐', '百事可乐，年轻选择，碳酸饮料', 47.90, 480, 80305, 'https://picsum.photos/id/596/600/600', 1, 823, NOW()),
                                                                                                                                           ('雪碧 330ml*24罐', '柠檬味汽水，清爽解渴，碳酸饮料', 49.90, 450, 80305, 'https://picsum.photos/id/597/600/600', 1, 765, NOW()),
                                                                                                                                           ('芬达橙味 330ml*24罐', '橙味汽水，香甜可口，碳酸饮料', 47.90, 420, 80305, 'https://picsum.photos/id/598/600/600', 1, 698, NOW());

-- 80401 水果
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('烟台红富士苹果 5斤', '脆甜多汁，新鲜采摘，产地直供', 29.90, 300, 80401, 'https://picsum.photos/id/599/600/600', 1, 456, NOW()),
                                                                                                                                           ('赣南脐橙 5斤', '酸甜可口，皮薄肉厚，新鲜水果', 39.90, 250, 80401, 'https://picsum.photos/id/600/600/600', 1, 421, NOW()),
                                                                                                                                           ('海南香蕉 5斤', '香甜软糯，新鲜采摘，自然成熟', 19.90, 350, 80401, 'https://picsum.photos/id/601/600/600', 1, 523, NOW()),
                                                                                                                                           ('新疆葡萄 2斤', '无籽葡萄，清甜爽口，新鲜水果', 49.90, 200, 80401, 'https://picsum.photos/id/602/600/600', 1, 321, NOW());

-- 80402 蔬菜
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('有机西红柿 500g', '有机种植，自然成熟，酸甜可口', 9.90, 200, 80402, 'https://picsum.photos/id/603/600/600', 1, 321, NOW()),
                                                                                                                                           ('新鲜黄瓜 500g', '脆嫩爽口，新鲜采摘，绿色蔬菜', 5.90, 250, 80402, 'https://picsum.photos/id/604/600/600', 1, 356, NOW()),
                                                                                                                                           ('有机西兰花 500g', '有机种植，营养丰富，绿色蔬菜', 12.90, 180, 80402, 'https://picsum.photos/id/605/600/600', 1, 289, NOW()),
                                                                                                                                           ('新鲜土豆 1kg', '黄心土豆，粉糯香甜，新鲜蔬菜', 6.90, 300, 80402, 'https://picsum.photos/id/606/600/600', 1, 421, NOW());

-- 80403 肉类
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('猪后腿肉 500g', '新鲜猪肉，肉质鲜嫩，适合炒菜', 29.90, 150, 80403, 'https://picsum.photos/id/607/600/600', 1, 234, NOW()),
                                                                                                                                           ('牛腱子肉 500g', '新鲜牛肉，适合卤制，肉质紧实', 69.90, 100, 80403, 'https://picsum.photos/id/608/600/600', 1, 189, NOW()),
                                                                                                                                           ('鸡胸肉 500g', '低脂高蛋白，健身必备，新鲜鸡肉', 19.90, 200, 80403, 'https://picsum.photos/id/609/600/600', 1, 321, NOW()),
                                                                                                                                           ('五花肉 500g', '肥瘦相间，适合红烧，新鲜猪肉', 35.90, 120, 80403, 'https://picsum.photos/id/610/600/600', 1, 278, NOW());

-- 80404 海鲜
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('鲜活基围虾 500g', '鲜活海虾，肉质鲜嫩，海鲜水产', 59.90, 100, 80404, 'https://picsum.photos/id/611/600/600', 1, 189, NOW()),
                                                                                                                                           ('冷冻带鱼段 500g', '舟山带鱼，中段精选，肉质厚实', 29.90, 150, 80404, 'https://picsum.photos/id/612/600/600', 1, 234, NOW()),
                                                                                                                                           ('鲜活大闸蟹 4只装', '阳澄湖大闸蟹，鲜活发货，蟹黄饱满', 199.00, 80, 80404, 'https://picsum.photos/id/613/600/600', 1, 123, NOW()),
                                                                                                                                           ('冷冻三文鱼 200g', '挪威进口，新鲜冷冻，刺身级', 49.90, 120, 80404, 'https://picsum.photos/id/614/600/600', 1, 156, NOW());

-- 80405 蛋品
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('土鸡蛋 30枚', '农家散养，新鲜鸡蛋，营养丰富', 39.90, 300, 80405, 'https://picsum.photos/id/615/600/600', 1, 567, NOW()),
                                                                                                                                           ('咸鸭蛋 20枚', '流油咸鸭蛋，真空包装，咸香可口', 29.90, 250, 80405, 'https://picsum.photos/id/616/600/600', 1, 498, NOW()),
                                                                                                                                           ('松花蛋 10枚', '无铅工艺，松花蛋，口感Q弹', 19.90, 200, 80405, 'https://picsum.photos/id/617/600/600', 1, 421, NOW()),
                                                                                                                                           ('鹌鹑蛋 500g', '新鲜鹌鹑蛋，营养丰富，适合卤制', 12.90, 280, 80405, 'https://picsum.photos/id/618/600/600', 1, 389, NOW());

-- 80501 白酒
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('茅台飞天53度 500ml', '酱香型白酒，国酒品质，收藏佳品', 1499.00, 50, 80501, 'https://picsum.photos/id/619/600/600', 1, 87, NOW()),
                                                                                                                                           ('五粮液52度 500ml', '浓香型白酒，高端白酒，商务宴请', 999.00, 80, 80501, 'https://picsum.photos/id/620/600/600', 1, 123, NOW()),
                                                                                                                                           ('泸州老窖特曲 52度 500ml', '浓香型白酒，中华老字号，口感醇厚', 199.00, 150, 80501, 'https://picsum.photos/id/621/600/600', 1, 234, NOW()),
                                                                                                                                           ('洋河蓝色经典海之蓝 52度 480ml', '浓香型白酒，绵柔口感，大众喜爱', 159.00, 200, 80501, 'https://picsum.photos/id/622/600/600', 1, 278, NOW());

-- 80502 啤酒
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('青岛啤酒经典 330ml*24罐', '经典青岛啤酒，麦香浓郁，口感清爽', 59.90, 400, 80502, 'https://picsum.photos/id/623/600/600', 1, 654, NOW()),
                                                                                                                                           ('百威啤酒 330ml*24罐', '美国品牌，淡色拉格，口感醇厚', 79.90, 350, 80502, 'https://picsum.photos/id/624/600/600', 1, 598, NOW()),
                                                                                                                                           ('雪花啤酒勇闯天涯 330ml*24罐', '清爽型啤酒，口感淡爽，适合畅饮', 49.90, 450, 80502, 'https://picsum.photos/id/625/600/600', 1, 723, NOW()),
                                                                                                                                           ('哈尔滨啤酒 330ml*24罐', '东北啤酒，口感清爽，冰爽解渴', 45.90, 400, 80502, 'https://picsum.photos/id/626/600/600', 1, 654, NOW());

-- 80503 红酒
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('张裕解百纳干红葡萄酒 750ml', '中国名牌，干红葡萄酒，口感醇厚', 99.00, 200, 80503, 'https://picsum.photos/id/627/600/600', 1, 321, NOW()),
                                                                                                                                           ('长城干红葡萄酒 750ml', '中粮出品，干红葡萄酒，性价比高', 79.00, 250, 80503, 'https://picsum.photos/id/628/600/600', 1, 356, NOW()),
                                                                                                                                           ('法国进口拉菲传奇干红 750ml', '法国波尔多产区，干红葡萄酒', 199.00, 150, 80503, 'https://picsum.photos/id/629/600/600', 1, 234, NOW()),
                                                                                                                                           ('奔富BIN28干红葡萄酒 750ml', '澳大利亚进口，干红葡萄酒', 299.00, 100, 80503, 'https://picsum.photos/id/630/600/600', 1, 189, NOW());

-- 80504 黄酒
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('古越龙山花雕酒 500ml', '绍兴黄酒，五年陈酿，花雕酒', 29.90, 200, 80504, 'https://picsum.photos/id/631/600/600', 1, 321, NOW()),
                                                                                                                                           ('女儿红黄酒 500ml', '绍兴特产，女儿红，三年陈酿', 25.90, 250, 80504, 'https://picsum.photos/id/632/600/600', 1, 356, NOW()),
                                                                                                                                           ('会稽山黄酒 500ml', '绍兴黄酒，十年陈酿，口感醇厚', 49.90, 150, 80504, 'https://picsum.photos/id/633/600/600', 1, 234, NOW()),
                                                                                                                                           ('塔牌黄酒 500ml', '绍兴黄酒，手工酿造，花雕酒', 35.90, 180, 80504, 'https://picsum.photos/id/634/600/600', 1, 278, NOW());

-- 80505 洋酒
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('芝华士12年威士忌 700ml', '苏格兰威士忌，调和型，口感顺滑', 199.00, 100, 80505, 'https://picsum.photos/id/635/600/600', 1, 189, NOW()),
                                                                                                                                           ('杰克丹尼威士忌 700ml', '美国田纳西威士忌，独特口感', 179.00, 120, 80505, 'https://picsum.photos/id/636/600/600', 1, 213, NOW()),
                                                                                                                                           ('人头马VSOP白兰地 700ml', '法国干邑白兰地，VSOP级别', 399.00, 80, 80505, 'https://picsum.photos/id/637/600/600', 1, 123, NOW()),
                                                                                                                                           ('绝对伏特加原味 700ml', '瑞典伏特加，原味，调酒必备', 129.00, 150, 80505, 'https://picsum.photos/id/638/600/600', 1, 156, NOW());

-- ==================== 一级分类9：图书文娱 ====================
-- 90101 科幻
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('三体全集 刘慈欣', '中国科幻里程碑，雨果奖获奖作品', 99.00, 300, 90101, 'https://picsum.photos/id/639/600/600', 1, 567, NOW()),
                                                                                                                                           ('流浪地球 刘慈欣', '同名电影原著，短篇科幻小说集', 39.90, 400, 90101, 'https://picsum.photos/id/640/600/600', 1, 498, NOW()),
                                                                                                                                           ('银河帝国：基地七部曲', '阿西莫夫经典，科幻史上不朽之作', 299.00, 150, 90101, 'https://picsum.photos/id/641/600/600', 1, 234, NOW()),
                                                                                                                                           ('沙丘 弗兰克·赫伯特', '科幻史上最伟大的作品之一', 59.90, 250, 90101, 'https://picsum.photos/id/642/600/600', 1, 321, NOW());

-- 90102 悬疑
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('白夜行 东野圭吾', '东野圭吾代表作，悬疑推理经典', 39.90, 350, 90102, 'https://picsum.photos/id/643/600/600', 1, 456, NOW()),
                                                                                                                                           ('嫌疑人X的献身 东野圭吾', '直木奖获奖作品，完美的诡计', 35.90, 380, 90102, 'https://picsum.photos/id/644/600/600', 1, 421, NOW()),
                                                                                                                                           ('无人生还 阿加莎·克里斯蒂', '暴风雪山庄模式开山之作', 29.90, 400, 90102, 'https://picsum.photos/id/645/600/600', 1, 523, NOW()),
                                                                                                                                           ('盗墓笔记 南派三叔', '中国悬疑探险小说巅峰之作', 199.00, 200, 90102, 'https://picsum.photos/id/646/600/600', 1, 389, NOW());

-- 90103 言情
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('何以笙箫默 顾漫', '经典言情小说，温暖治愈', 29.90, 300, 90103, 'https://picsum.photos/id/647/600/600', 1, 456, NOW()),
                                                                                                                                           ('微微一笑很倾城 顾漫', '网游言情经典，甜蜜浪漫', 25.90, 350, 90103, 'https://picsum.photos/id/648/600/600', 1, 421, NOW()),
                                                                                                                                           ('偷偷藏不住 竹已', '晋江金榜作品，青春校园言情', 39.90, 280, 90103, 'https://picsum.photos/id/649/600/600', 1, 389, NOW()),
                                                                                                                                           ('难哄 竹已', '偷偷藏不住姊妹篇，都市言情', 45.90, 250, 90103, 'https://picsum.photos/id/650/600/600', 1, 356, NOW());

-- 90104 武侠
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('天龙八部 金庸', '金庸武侠巅峰之作，全五册', 199.00, 150, 90104, 'https://picsum.photos/id/651/600/600', 1, 234, NOW()),
                                                                                                                                           ('射雕英雄传 金庸', '金庸武侠代表作，全四册', 159.00, 180, 90104, 'https://picsum.photos/id/652/600/600', 1, 213, NOW()),
                                                                                                                                           ('雪中悍刀行 烽火戏诸侯', '新武侠经典，全二十册', 399.00, 100, 90104, 'https://picsum.photos/id/653/600/600', 1, 156, NOW()),
                                                                                                                                           ('剑来 烽火戏诸侯', '仙侠武侠巨著，第一辑', 299.00, 120, 90104, 'https://picsum.photos/id/654/600/600', 1, 189, NOW());

-- 90105 经典
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('红楼梦 曹雪芹', '中国古典文学四大名著', 59.90, 250, 90105, 'https://picsum.photos/id/655/600/600', 1, 321, NOW()),
                                                                                                                                           ('百年孤独 加西亚·马尔克斯', '诺贝尔文学奖获奖作品', 49.90, 280, 90105, 'https://picsum.photos/id/656/600/600', 1, 289, NOW()),
                                                                                                                                           ('活着 余华', '中国当代文学经典，余华代表作', 29.90, 350, 90105, 'https://picsum.photos/id/657/600/600', 1, 421, NOW()),
                                                                                                                                           ('平凡的世界 路遥', '茅盾文学奖获奖作品，全三册', 79.90, 200, 90105, 'https://picsum.photos/id/658/600/600', 1, 356, NOW());

-- 90201 国产漫画
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('哪吒之魔童降世 漫画版', '同名电影改编，国漫经典', 49.90, 200, 90201, 'https://picsum.photos/id/659/600/600', 1, 234, NOW()),
                                                                                                                                           ('一人之下 米二', '国漫神作，异人世界', 29.90, 250, 90201, 'https://picsum.photos/id/660/600/600', 1, 289, NOW()),
                                                                                                                                           ('狐妖小红娘 庹小新', '国漫经典，爱情奇幻', 25.90, 280, 90201, 'https://picsum.photos/id/661/600/600', 1, 256, NOW()),
                                                                                                                                           ('斗罗大陆 唐家三少', '超人气国漫，玄幻冒险', 35.90, 220, 90201, 'https://picsum.photos/id/662/600/600', 1, 321, NOW());

-- 90202 日漫
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('海贼王 尾田荣一郎', '日本国民级漫画，航海冒险', 29.90, 300, 90202, 'https://picsum.photos/id/663/600/600', 1, 456, NOW()),
                                                                                                                                           ('火影忍者 岸本齐史', '经典热血漫画，忍者世界', 25.90, 350, 90202, 'https://picsum.photos/id/664/600/600', 1, 421, NOW()),
                                                                                                                                           ('进击的巨人 谏山创', '现象级漫画，末世题材', 35.90, 250, 90202, 'https://picsum.photos/id/665/600/600', 1, 321, NOW()),
                                                                                                                                           ('鬼灭之刃 吾峠呼世晴', '超人气日漫，斩鬼冒险', 39.90, 280, 90202, 'https://picsum.photos/id/666/600/600', 1, 389, NOW());

-- 90203 画集
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('宫崎骏动画原画集', '宫崎骏经典作品原画精选', 199.00, 100, 90203, 'https://picsum.photos/id/667/600/600', 1, 123, NOW()),
                                                                                                                                           ('鬼灭之刃官方画集', '鬼灭之刃角色设定与原画', 159.00, 120, 90203, 'https://picsum.photos/id/668/600/600', 1, 156, NOW()),
                                                                                                                                           ('原神官方插画集', '原神游戏角色与场景插画', 299.00, 80, 90203, 'https://picsum.photos/id/669/600/600', 1, 87, NOW()),
                                                                                                                                           ('哈利·波特电影魔法书', '哈利波特电影幕后与设定', 259.00, 90, 90203, 'https://picsum.photos/id/670/600/600', 1, 109, NOW());

-- 90301 摄影
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('美国纽约摄影学院摄影教材', '摄影入门经典，上下册', 199.00, 150, 90301, 'https://picsum.photos/id/671/600/600', 1, 189, NOW()),
                                                                                                                                           ('人像摄影用光指南', '人像摄影必备，光线运用技巧', 69.90, 200, 90301, 'https://picsum.photos/id/672/600/600', 1, 234, NOW()),
                                                                                                                                           ('风光摄影入门', '风光摄影技巧，构图与后期', 59.90, 220, 90301, 'https://picsum.photos/id/673/600/600', 1, 213, NOW()),
                                                                                                                                           ('手机摄影从入门到精通', '手机摄影技巧，人人都能学', 39.90, 300, 90301, 'https://picsum.photos/id/674/600/600', 1, 321, NOW());

-- 90302 绘画
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('素描的诀窍', '素描入门经典，零基础适用', 49.90, 250, 90302, 'https://picsum.photos/id/675/600/600', 1, 289, NOW()),
                                                                                                                                           ('水彩画入门教程', '水彩画技法，从入门到精通', 59.90, 220, 90302, 'https://picsum.photos/id/676/600/600', 1, 256, NOW()),
                                                                                                                                           ('动漫人物绘画教程', '动漫角色设计，人体结构', 39.90, 280, 90302, 'https://picsum.photos/id/677/600/600', 1, 321, NOW()),
                                                                                                                                           ('油画入门', '油画基础技法，色彩运用', 69.90, 180, 90302, 'https://picsum.photos/id/678/600/600', 1, 213, NOW());

-- 90303 建筑
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('中国建筑史 梁思成', '中国建筑史经典著作', 99.00, 100, 90303, 'https://picsum.photos/id/679/600/600', 1, 123, NOW()),
                                                                                                                                           ('建筑空间组合论', '建筑设计基础理论', 69.90, 120, 90303, 'https://picsum.photos/id/680/600/600', 1, 156, NOW()),
                                                                                                                                           ('安藤忠雄的建筑世界', '安藤忠雄作品集与设计理念', 159.00, 80, 90303, 'https://picsum.photos/id/681/600/600', 1, 87, NOW()),
                                                                                                                                           ('世界建筑史', '全球建筑发展历程，图文并茂', 199.00, 90, 90303, 'https://picsum.photos/id/682/600/600', 1, 109, NOW());

-- 90304 设计
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('写给大家看的设计书', '设计入门经典，四大原则', 49.90, 200, 90304, 'https://picsum.photos/id/683/600/600', 1, 234, NOW()),
                                                                                                                                           ('平面设计中的网格系统', '平面设计必备，网格排版', 69.90, 150, 90304, 'https://picsum.photos/id/684/600/600', 1, 189, NOW()),
                                                                                                                                           ('UI设计入门', '用户界面设计基础，APP设计', 59.90, 180, 90304, 'https://picsum.photos/id/685/600/600', 1, 213, NOW()),
                                                                                                                                           ('品牌设计法则', '品牌视觉设计，LOGO设计', 79.90, 120, 90304, 'https://picsum.photos/id/686/600/600', 1, 156, NOW());

-- 90401 中小学教辅
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('五年高考三年模拟 数学', '高考数学必备，真题模拟', 59.90, 300, 90401, 'https://picsum.photos/id/687/600/600', 1, 456, NOW()),
                                                                                                                                           ('黄冈小状元 语文 三年级', '小学语文同步练习，人教版', 29.90, 400, 90401, 'https://picsum.photos/id/688/600/600', 1, 523, NOW()),
                                                                                                                                           ('初中英语语法大全', '初中英语语法，系统讲解', 39.90, 350, 90401, 'https://picsum.photos/id/689/600/600', 1, 421, NOW()),
                                                                                                                                           ('高中物理必修一 教材全解', '高中物理同步辅导，知识点详解', 35.90, 320, 90401, 'https://picsum.photos/id/690/600/600', 1, 389, NOW());

-- 90402 考研
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('考研英语词汇 红宝书', '考研英语必备，词汇大全', 59.90, 250, 90402, 'https://picsum.photos/id/691/600/600', 1, 321, NOW()),
                                                                                                                                           ('考研政治 肖秀荣1000题', '考研政治经典，习题集', 49.90, 280, 90402, 'https://picsum.photos/id/692/600/600', 1, 289, NOW()),
                                                                                                                                           ('考研数学 李永乐复习全书', '考研数学基础，系统复习', 69.90, 220, 90402, 'https://picsum.photos/id/693/600/600', 1, 256, NOW()),
                                                                                                                                           ('考研英语历年真题详解', '考研英语真题，逐题解析', 39.90, 300, 90402, 'https://picsum.photos/id/694/600/600', 1, 356, NOW());

-- 90403 外语
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('新概念英语1', '英语入门经典，零基础适用', 39.90, 350, 90403, 'https://picsum.photos/id/695/600/600', 1, 421, NOW()),
                                                                                                                                           ('日语五十音图入门', '日语零基础，五十音学习', 29.90, 300, 90403, 'https://picsum.photos/id/696/600/600', 1, 389, NOW()),
                                                                                                                                           ('韩语入门一本通', '韩语零基础，发音与语法', 35.90, 250, 90403, 'https://picsum.photos/id/697/600/600', 1, 321, NOW()),
                                                                                                                                           ('雅思词汇词根+联想记忆法', '雅思词汇必备，俞敏洪编著', 49.90, 280, 90403, 'https://picsum.photos/id/698/600/600', 1, 356, NOW());

-- 90404 职业技能
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Python编程从入门到实践', 'Python入门经典，零基础适用', 59.90, 250, 90404, 'https://picsum.photos/id/699/600/600', 1, 321, NOW()),
                                                                                                                                           ('会计基础', '会计入门，零基础学会计', 39.90, 280, 90404, 'https://picsum.photos/id/700/600/600', 1, 289, NOW()),
                                                                                                                                           ('人力资源管理', 'HR入门，人力资源基础知识', 49.90, 220, 90404, 'https://picsum.photos/id/701/600/600', 1, 256, NOW()),
                                                                                                                                           ('市场营销学', '市场营销基础，营销理论与实践', 45.90, 250, 90404, 'https://picsum.photos/id/702/600/600', 1, 289, NOW());

-- 90501 音乐CD
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('周杰伦 周杰伦的床边故事 CD', '周杰伦专辑，经典歌曲', 99.00, 100, 90501, 'https://picsum.photos/id/703/600/600', 1, 123, NOW()),
                                                                                                                                           ('林俊杰 伟大的渺小 CD', '林俊杰专辑，华语流行', 89.90, 120, 90501, 'https://picsum.photos/id/704/600/600', 1, 156, NOW()),
                                                                                                                                           ('Taylor Swift 1989 CD', '泰勒·斯威夫特专辑，欧美流行', 129.00, 80, 90501, 'https://picsum.photos/id/705/600/600', 1, 87, NOW()),
                                                                                                                                           ('陈奕迅 U87 CD', '陈奕迅经典专辑，粤语流行', 79.90, 100, 90501, 'https://picsum.photos/id/706/600/600', 1, 109, NOW());

-- 90502 电子书
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Kindle电子书阅读器 青春版', '亚马逊电子书阅读器，6英寸', 699.00, 150, 90502, 'https://picsum.photos/id/707/600/600', 1, 189, NOW()),
                                                                                                                                           ('掌阅iReader Light2', '电子书阅读器，6英寸，轻薄便携', 599.00, 180, 90502, 'https://picsum.photos/id/708/600/600', 1, 213, NOW()),
                                                                                                                                           ('文石BOOX Poke4', '电子书阅读器，6英寸，安卓系统', 899.00, 100, 90502, 'https://picsum.photos/id/709/600/600', 1, 123, NOW()),
                                                                                                                                           ('小米多看电纸书Pro', '电子书阅读器，7.8英寸，高清屏幕', 799.00, 120, 90502, 'https://picsum.photos/id/710/600/600', 1, 156, NOW());

-- 90503 有声书
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('喜马拉雅年卡会员', '喜马拉雅VIP会员，畅听有声书', 199.00, 200, 90503, 'https://picsum.photos/id/711/600/600', 1, 321, NOW()),
                                                                                                                                           ('得到听书年卡', '得到APP听书会员，知识付费', 299.00, 150, 90503, 'https://picsum.photos/id/712/600/600', 1, 234, NOW()),
                                                                                                                                           ('三体有声书 全集', '三体小说有声版，多人演播', 99.00, 250, 90503, 'https://picsum.photos/id/713/600/600', 1, 389, NOW()),
                                                                                                                                           ('明朝那些事儿 有声书', '历史通俗读物，有声版', 69.90, 300, 90503, 'https://picsum.photos/id/714/600/600', 1, 421, NOW());

-- ==================== 一级分类10：运动户外 ====================
-- 100101 跑鞋
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('耐克Air Zoom Pegasus 40跑鞋', '专业跑鞋，气垫缓震，透气舒适', 899.00, 150, 100101, 'https://picsum.photos/id/715/600/600', 1, 234, NOW()),
                                                                                                                                           ('阿迪达斯Ultraboost 23跑鞋', 'Boost中底，缓震回弹，跑步鞋', 1099.00, 120, 100101, 'https://picsum.photos/id/716/600/600', 1, 189, NOW()),
                                                                                                                                           ('李宁赤兔6 Pro跑鞋', '专业竞速跑鞋，䨻科技，轻量透气', 399.00, 200, 100101, 'https://picsum.photos/id/717/600/600', 1, 321, NOW()),
                                                                                                                                           ('安踏创2.5跑鞋', '氮科技中底，缓震回弹，跑步鞋', 499.00, 180, 100101, 'https://picsum.photos/id/718/600/600', 1, 289, NOW());

-- 100102 篮球鞋
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('耐克Air Jordan 1篮球鞋', '经典篮球鞋，潮流百搭', 1299.00, 100, 100102, 'https://picsum.photos/id/719/600/600', 1, 156, NOW()),
                                                                                                                                           ('阿迪达斯哈登7篮球鞋', '哈登签名鞋，实战篮球鞋', 1199.00, 80, 100102, 'https://picsum.photos/id/720/600/600', 1, 123, NOW()),
                                                                                                                                           ('李宁韦德之道10篮球鞋', '韦德签名鞋，实战篮球鞋', 1399.00, 60, 100102, 'https://picsum.photos/id/721/600/600', 1, 87, NOW()),
                                                                                                                                           ('安踏KT8篮球鞋', '汤普森签名鞋，实战篮球鞋', 999.00, 90, 100102, 'https://picsum.photos/id/722/600/600', 1, 109, NOW());

-- 100103 运动T恤
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('耐克速干运动T恤', '速干面料，透气舒适，运动必备', 199.00, 250, 100103, 'https://picsum.photos/id/723/600/600', 1, 321, NOW()),
                                                                                                                                           ('阿迪达斯运动T恤', '纯棉面料，简约设计，日常运动', 159.00, 280, 100103, 'https://picsum.photos/id/724/600/600', 1, 289, NOW()),
                                                                                                                                           ('李宁运动T恤', '速干面料，透气排汗，跑步健身', 99.00, 300, 100103, 'https://picsum.photos/id/725/600/600', 1, 389, NOW()),
                                                                                                                                           ('安踏运动T恤', '冰丝面料，凉爽舒适，夏季运动', 129.00, 260, 100103, 'https://picsum.photos/id/726/600/600', 1, 356, NOW());

-- 100104 运动裤
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('耐克运动长裤', '速干面料，束脚设计，跑步健身', 299.00, 200, 100104, 'https://picsum.photos/id/727/600/600', 1, 289, NOW()),
                                                                                                                                           ('阿迪达斯运动短裤', '速干面料，透气舒适，夏季运动', 199.00, 250, 100104, 'https://picsum.photos/id/728/600/600', 1, 321, NOW()),
                                                                                                                                           ('李宁运动长裤', '纯棉面料，宽松舒适，日常运动', 159.00, 280, 100104, 'https://picsum.photos/id/729/600/600', 1, 289, NOW()),
                                                                                                                                           ('安踏运动短裤', '冰丝面料，凉爽透气，跑步健身', 129.00, 300, 100104, 'https://picsum.photos/id/730/600/600', 1, 356, NOW());

-- 100201 瑜伽垫
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Lululemon瑜伽垫', '专业瑜伽垫，防滑耐磨，6mm厚', 599.00, 100, 100201, 'https://picsum.photos/id/731/600/600', 1, 123, NOW()),
                                                                                                                                           ('Keep瑜伽垫', 'TPE材质，防滑耐磨，8mm厚', 99.00, 300, 100201, 'https://picsum.photos/id/732/600/600', 1, 321, NOW()),
                                                                                                                                           ('李宁瑜伽垫', 'NBR材质，加厚10mm，隔音减震', 79.90, 350, 100201, 'https://picsum.photos/id/733/600/600', 1, 389, NOW()),
                                                                                                                                           ('奥义瑜伽垫', 'TPE材质，双面防滑，6mm厚', 59.90, 400, 100201, 'https://picsum.photos/id/734/600/600', 1, 421, NOW());

-- 100202 哑铃
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('迪卡侬可调节哑铃', '5-25kg可调节，家用健身', 299.00, 150, 100202, 'https://picsum.photos/id/735/600/600', 1, 234, NOW()),
                                                                                                                                           ('李宁包胶哑铃', '5kg*2，包胶设计，静音防滑', 199.00, 200, 100202, 'https://picsum.photos/id/736/600/600', 1, 289, NOW()),
                                                                                                                                           ('Keep哑铃', '10kg*2，环保材质，家用健身', 259.00, 180, 100202, 'https://picsum.photos/id/737/600/600', 1, 256, NOW()),
                                                                                                                                           ('飞尔顿哑铃', '可调节重量，1-10kg，女士健身', 159.00, 220, 100202, 'https://picsum.photos/id/738/600/600', 1, 321, NOW());

-- 100203 跑步机
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('爱康跑步机', '家用高端跑步机，静音减震', 4999.00, 50, 100203, 'https://picsum.photos/id/739/600/600', 1, 87, NOW()),
                                                                                                                                           ('舒华跑步机', '家用跑步机，折叠设计，静音', 2999.00, 80, 100203, 'https://picsum.photos/id/740/600/600', 1, 123, NOW()),
                                                                                                                                           ('亿健跑步机', '家用跑步机，智能联网，减震', 1999.00, 100, 100203, 'https://picsum.photos/id/741/600/600', 1, 156, NOW()),
                                                                                                                                           ('小米米家跑步机', '家用跑步机，折叠设计，智能控制', 2499.00, 90, 100203, 'https://picsum.photos/id/742/600/600', 1, 134, NOW());

-- 100204 拉力带
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('Keep拉力带', '5条装，不同阻力，健身塑形', 29.90, 400, 100204, 'https://picsum.photos/id/743/600/600', 1, 456, NOW()),
                                                                                                                                           ('李宁拉力带', '3条装，天然乳胶，弹力十足', 19.90, 450, 100204, 'https://picsum.photos/id/744/600/600', 1, 498, NOW()),
                                                                                                                                           ('迪卡侬拉力带', '4条装，不同强度，全身训练', 39.90, 350, 100204, 'https://picsum.photos/id/745/600/600', 1, 421, NOW()),
                                                                                                                                           ('奥义拉力带', '5条装，防滑设计，瑜伽健身', 25.90, 400, 100204, 'https://picsum.photos/id/746/600/600', 1, 456, NOW());

-- 100301 帐篷
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('牧高笛冷山3帐篷', '3-4人帐篷，防雨防风，露营必备', 599.00, 100, 100301, 'https://picsum.photos/id/747/600/600', 1, 156, NOW()),
                                                                                                                                           ('挪客自动帐篷', '3-4人，自动速开，防雨防晒', 399.00, 150, 100301, 'https://picsum.photos/id/748/600/600', 1, 213, NOW()),
                                                                                                                                           ('迪卡侬帐篷', '2-3人，防雨防风，入门级', 299.00, 200, 100301, 'https://picsum.photos/id/749/600/600', 1, 289, NOW()),
                                                                                                                                           ('骆驼帐篷', '3-4人，双层防雨，自动速开', 359.00, 180, 100301, 'https://picsum.photos/id/750/600/600', 1, 256, NOW());

-- 100302 睡袋
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('黑冰睡袋', '羽绒睡袋，-10℃温标，户外露营', 599.00, 80, 100302, 'https://picsum.photos/id/751/600/600', 1, 123, NOW()),
                                                                                                                                           ('挪客睡袋', '棉睡袋，10℃温标，春秋露营', 199.00, 150, 100302, 'https://picsum.photos/id/752/600/600', 1, 213, NOW()),
                                                                                                                                           ('迪卡侬睡袋', '棉睡袋，15℃温标，入门级', 129.00, 200, 100302, 'https://picsum.photos/id/753/600/600', 1, 289, NOW()),
                                                                                                                                           ('骆驼睡袋', '羽绒睡袋，0℃温标，冬季露营', 399.00, 100, 100302, 'https://picsum.photos/id/754/600/600', 1, 189, NOW());

-- 100303 登山杖
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('鲁滨逊登山杖', '碳纤维材质，超轻可折叠', 199.00, 150, 100303, 'https://picsum.photos/id/755/600/600', 1, 213, NOW()),
                                                                                                                                           ('挪客登山杖', '铝合金材质，三节伸缩，防滑手柄', 99.00, 200, 100303, 'https://picsum.photos/id/756/600/600', 1, 289, NOW()),
                                                                                                                                           ('迪卡侬登山杖', '铝合金材质，可调节长度', 79.90, 250, 100303, 'https://picsum.photos/id/757/600/600', 1, 321, NOW()),
                                                                                                                                           ('牧高笛登山杖', '碳纤维材质，超轻便携', 159.00, 180, 100303, 'https://picsum.photos/id/758/600/600', 1, 256, NOW());

-- 100304 野餐垫
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('挪客野餐垫', '200*200cm，防水防潮，可折叠', 99.00, 250, 100304, 'https://picsum.photos/id/759/600/600', 1, 321, NOW()),
                                                                                                                                           ('牧高笛野餐垫', '150*200cm，牛津布材质，防水', 79.90, 300, 100304, 'https://picsum.photos/id/760/600/600', 1, 389, NOW()),
                                                                                                                                           ('迪卡侬野餐垫', '140*170cm，防水防潮，便携', 59.90, 350, 100304, 'https://picsum.photos/id/761/600/600', 1, 421, NOW()),
                                                                                                                                           ('骆驼野餐垫', '200*200cm，加厚设计，防水', 89.90, 280, 100304, 'https://picsum.photos/id/762/600/600', 1, 356, NOW());

-- 100401 篮球
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('斯伯丁篮球', '7号标准篮球，PU材质，室内外通用', 199.00, 200, 100401, 'https://picsum.photos/id/763/600/600', 1, 289, NOW()),
                                                                                                                                           ('耐克篮球', '7号标准篮球，耐磨防滑，室内外通用', 259.00, 180, 100401, 'https://picsum.photos/id/764/600/600', 1, 256, NOW()),
                                                                                                                                           ('李宁篮球', '7号标准篮球，PU材质，室内外通用', 129.00, 250, 100401, 'https://picsum.photos/id/765/600/600', 1, 321, NOW()),
                                                                                                                                           ('安踏篮球', '7号标准篮球，耐磨防滑，室内外通用', 159.00, 220, 100401, 'https://picsum.photos/id/766/600/600', 1, 289, NOW());

-- 100402 足球
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('阿迪达斯足球', '5号标准足球，PU材质，比赛用球', 299.00, 150, 100402, 'https://picsum.photos/id/767/600/600', 1, 213, NOW()),
                                                                                                                                           ('耐克足球', '5号标准足球，耐磨防滑，训练用球', 259.00, 180, 100402, 'https://picsum.photos/id/768/600/600', 1, 256, NOW()),
                                                                                                                                           ('李宁足球', '5号标准足球，PU材质，室内外通用', 129.00, 200, 100402, 'https://picsum.photos/id/769/600/600', 1, 289, NOW()),
                                                                                                                                           ('红双喜足球', '5号标准足球，耐磨耐用，训练用球', 99.00, 250, 100402, 'https://picsum.photos/id/770/600/600', 1, 321, NOW());

-- 100403 羽毛球
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('尤尼克斯羽毛球拍', '全碳素材质，超轻进攻型', 599.00, 100, 100403, 'https://picsum.photos/id/771/600/600', 1, 156, NOW()),
                                                                                                                                           ('李宁羽毛球拍', '全碳素材质，攻守兼备型', 399.00, 150, 100403, 'https://picsum.photos/id/772/600/600', 1, 213, NOW()),
                                                                                                                                           ('胜利羽毛球拍', '全碳素材质，防守型', 299.00, 180, 100403, 'https://picsum.photos/id/773/600/600', 1, 256, NOW()),
                                                                                                                                           ('红双喜羽毛球', '12只装，耐打王，训练用球', 39.90, 300, 100403, 'https://picsum.photos/id/774/600/600', 1, 421, NOW());

-- 100404 滑板
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('DBH滑板', '专业双翘滑板，入门级', 299.00, 150, 100404, 'https://picsum.photos/id/775/600/600', 1, 213, NOW()),
                                                                                                                                           ('沸点滑板', '专业双翘滑板，进阶款', 399.00, 120, 100404, 'https://picsum.photos/id/776/600/600', 1, 189, NOW()),
                                                                                                                                           ('小鱼板滑板', '单翘滑板，代步刷街', 199.00, 200, 100404, 'https://picsum.photos/id/777/600/600', 1, 289, NOW()),
                                                                                                                                           ('长板滑板', '舞板长板，刷街代步', 499.00, 100, 100404, 'https://picsum.photos/id/778/600/600', 1, 156, NOW());

-- 100501 自行车
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('捷安特山地自行车', '27速变速，铝合金车架，减震前叉', 2999.00, 50, 100501, 'https://picsum.photos/id/779/600/600', 1, 87, NOW()),
                                                                                                                                           ('美利达公路自行车', '18速变速，碳纤维车架，轻量化', 5999.00, 30, 100501, 'https://picsum.photos/id/780/600/600', 1, 56, NOW()),
                                                                                                                                           ('喜德盛山地自行车', '24速变速，铝合金车架，入门级', 1999.00, 80, 100501, 'https://picsum.photos/id/781/600/600', 1, 123, NOW()),
                                                                                                                                           ('永久自行车', '21速变速，铝合金车架，通勤代步', 999.00, 100, 100501, 'https://picsum.photos/id/782/600/600', 1, 156, NOW());

-- 100502 头盔
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('GIRO自行车头盔', '专业骑行头盔，通风透气，安全防护', 399.00, 100, 100502, 'https://picsum.photos/id/783/600/600', 1, 156, NOW()),
                                                                                                                                           ('迪卡侬自行车头盔', '入门级头盔，通风设计，安全防护', 99.00, 200, 100502, 'https://picsum.photos/id/784/600/600', 1, 289, NOW()),
                                                                                                                                           ('PMT自行车头盔', '轻量化设计，通风透气，安全防护', 199.00, 150, 100502, 'https://picsum.photos/id/785/600/600', 1, 213, NOW()),
                                                                                                                                           ('洛克兄弟头盔', '一体成型，通风设计，安全防护', 159.00, 180, 100502, 'https://picsum.photos/id/786/600/600', 1, 256, NOW());

-- 100503 骑行服
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('捷安特骑行服', '短袖套装，速干透气，骑行专用', 299.00, 100, 100503, 'https://picsum.photos/id/787/600/600', 1, 156, NOW()),
                                                                                                                                           ('迪卡侬骑行服', '长袖套装，防风保暖，春秋骑行', 399.00, 80, 100503, 'https://picsum.photos/id/788/600/600', 1, 123, NOW()),
                                                                                                                                           ('森地客骑行服', '短袖上衣，速干透气，专业骑行', 199.00, 150, 100503, 'https://picsum.photos/id/789/600/600', 1, 213, NOW()),
                                                                                                                                           ('洛克兄弟骑行裤', '长裤，硅胶坐垫，舒适骑行', 159.00, 180, 100503, 'https://picsum.photos/id/790/600/600', 1, 256, NOW());

-- 100504 车灯
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('洛克兄弟自行车灯', '前灯+尾灯套装，USB充电，强光照明', 59.90, 200, 100504, 'https://picsum.photos/id/791/600/600', 1, 289, NOW()),
                                                                                                                                           ('迪卡侬自行车灯', '前灯，USB充电，多档调节', 39.90, 250, 100504, 'https://picsum.photos/id/792/600/600', 1, 321, NOW()),
                                                                                                                                           ('猫眼自行车灯', '前灯，强光照明，防水设计', 99.00, 150, 100504, 'https://picsum.photos/id/793/600/600', 1, 213, NOW()),
                                                                                                                                           ('神火自行车灯', '前灯+尾灯，USB充电，超长续航', 49.90, 220, 100504, 'https://picsum.photos/id/794/600/600', 1, 256, NOW());

-- ==================== 一级分类11：汽车用品 ====================
-- 110101 行车记录仪
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('360行车记录仪G300', '1080P高清，夜视增强，停车监控', 299.00, 200, 110101, 'https://picsum.photos/id/795/600/600', 1, 321, NOW()),
                                                                                                                                           ('海康威视行车记录仪', '4K高清，夜视增强，语音控制', 599.00, 150, 110101, 'https://picsum.photos/id/796/600/600', 1, 234, NOW()),
                                                                                                                                           ('盯盯拍行车记录仪mini3', '1600P高清，内置存储，语音控制', 399.00, 180, 110101, 'https://picsum.photos/id/797/600/600', 1, 289, NOW()),
                                                                                                                                           ('小米行车记录仪2', '1080P高清，夜视增强，停车监控', 259.00, 220, 110101, 'https://picsum.photos/id/798/600/600', 1, 356, NOW());

-- 110102 车载充电器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('绿联车载充电器', '65W快充，双口输出，兼容多设备', 59.90, 300, 110102, 'https://picsum.photos/id/799/600/600', 1, 456, NOW()),
                                                                                                                                           ('倍思车载充电器', '100W快充，三口输出，数显电压', 79.90, 250, 110102, 'https://picsum.photos/id/800/600/600', 1, 421, NOW()),
                                                                                                                                           ('小米车载充电器', '37W快充，双口输出，金属外壳', 49.90, 350, 110102, 'https://picsum.photos/id/801/600/600', 1, 498, NOW()),
                                                                                                                                           ('公牛车载充电器', '30W快充，双口输出，安全可靠', 39.90, 400, 110102, 'https://picsum.photos/id/802/600/600', 1, 523, NOW());

-- 110103 净化器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('飞利浦车载空气净化器', 'HEPA滤网，除甲醛，PM2.5', 399.00, 150, 110103, 'https://picsum.photos/id/803/600/600', 1, 234, NOW()),
                                                                                                                                           ('小米车载空气净化器', 'HEPA滤网，除甲醛，PM2.5', 299.00, 200, 110103, 'https://picsum.photos/id/804/600/600', 1, 289, NOW()),
                                                                                                                                           ('3M车载空气净化器', 'HEPA滤网，除异味，PM2.5', 259.00, 220, 110103, 'https://picsum.photos/id/805/600/600', 1, 256, NOW()),
                                                                                                                                           ('夏普车载空气净化器', '净离子群技术，除甲醛，PM2.5', 499.00, 120, 110103, 'https://picsum.photos/id/806/600/600', 1, 189, NOW());

-- 110201 脚垫
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('3M全包围汽车脚垫', '环保材质，专车专用，防水耐磨', 599.00, 100, 110201, 'https://picsum.photos/id/807/600/600', 1, 156, NOW()),
                                                                                                                                           ('固特异汽车脚垫', 'TPE材质，环保无异味，防水耐磨', 399.00, 150, 110201, 'https://picsum.photos/id/808/600/600', 1, 213, NOW()),
                                                                                                                                           ('五福金牛汽车脚垫', '全包围，皮革材质，专车专用', 499.00, 120, 110201, 'https://picsum.photos/id/809/600/600', 1, 189, NOW()),
                                                                                                                                           ('御马汽车脚垫', '丝圈材质，环保无异味，易清洗', 299.00, 180, 110201, 'https://picsum.photos/id/810/600/600', 1, 256, NOW());

-- 110202 座套
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('牧宝汽车座套', '真皮材质，专车专用，四季通用', 1299.00, 50, 110202, 'https://picsum.photos/id/811/600/600', 1, 87, NOW()),
                                                                                                                                           ('尼罗河汽车座套', '亚麻材质，透气舒适，四季通用', 599.00, 100, 110202, 'https://picsum.photos/id/812/600/600', 1, 156, NOW()),
                                                                                                                                           ('五福金牛汽车座套', '皮革材质，全包围，专车专用', 799.00, 80, 110202, 'https://picsum.photos/id/813/600/600', 1, 123, NOW()),
                                                                                                                                           ('南极人汽车座套', '冰丝材质，夏季专用，透气凉爽', 299.00, 150, 110202, 'https://picsum.photos/id/814/600/600', 1, 213, NOW());

-- 110203 挂件
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('汽车挂件 平安符', '桃木材质，平安祈福，车内装饰', 29.90, 300, 110203, 'https://picsum.photos/id/815/600/600', 1, 456, NOW()),
                                                                                                                                           ('汽车挂件 水晶葫芦', '水晶材质，招财进宝，车内装饰', 49.90, 250, 110203, 'https://picsum.photos/id/816/600/600', 1, 421, NOW()),
                                                                                                                                           ('汽车挂件 毛主席像', '金属材质，保平安，车内装饰', 39.90, 280, 110203, 'https://picsum.photos/id/817/600/600', 1, 389, NOW()),
                                                                                                                                           ('汽车挂件 香水', '香薰挂件，清新空气，车内装饰', 59.90, 220, 110203, 'https://picsum.photos/id/818/600/600', 1, 356, NOW());

-- 110204 方向盘套
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('真皮方向盘套', '头层牛皮，防滑耐磨，四季通用', 129.00, 200, 110204, 'https://picsum.photos/id/819/600/600', 1, 289, NOW()),
                                                                                                                                           ('翻毛皮方向盘套', '翻毛皮材质，防滑吸汗，运动风格', 99.00, 250, 110204, 'https://picsum.photos/id/820/600/600', 1, 321, NOW()),
                                                                                                                                           ('冰丝方向盘套', '冰丝材质，夏季专用，透气凉爽', 59.90, 300, 110204, 'https://picsum.photos/id/821/600/600', 1, 389, NOW()),
                                                                                                                                           ('毛绒方向盘套', '毛绒材质，冬季专用，保暖舒适', 49.90, 280, 110204, 'https://picsum.photos/id/822/600/600', 1, 356, NOW());

-- 110301 机油
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('美孚1号全合成机油 5W-30', '全合成机油，API SN级，4L装', 399.00, 100, 110301, 'https://picsum.photos/id/823/600/600', 1, 156, NOW()),
                                                                                                                                           ('嘉实多极护全合成机油 5W-40', '全合成机油，API SN级，4L装', 359.00, 120, 110301, 'https://picsum.photos/id/824/600/600', 1, 189, NOW()),
                                                                                                                                           ('壳牌超凡喜力全合成机油 5W-30', '全合成机油，API SN级，4L装', 379.00, 110, 110301, 'https://picsum.photos/id/825/600/600', 1, 176, NOW()),
                                                                                                                                           ('长城金吉星全合成机油 5W-30', '全合成机油，API SN级，4L装', 259.00, 150, 110301, 'https://picsum.photos/id/826/600/600', 1, 213, NOW());

-- 110302 玻璃水
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('龟牌玻璃水 -25℃', '防冻玻璃水，清洁去污，冬季专用', 19.90, 400, 110302, 'https://picsum.photos/id/827/600/600', 1, 567, NOW()),
                                                                                                                                           ('3M玻璃水 0℃', '夏季玻璃水，清洁去污，防眩光', 15.90, 450, 110302, 'https://picsum.photos/id/828/600/600', 1, 523, NOW()),
                                                                                                                                           ('蓝星玻璃水 -40℃', '超防冻玻璃水，北方冬季专用', 29.90, 300, 110302, 'https://picsum.photos/id/829/600/600', 1, 421, NOW()),
                                                                                                                                           ('车仆玻璃水 0℃', '夏季玻璃水，清洁去污，虫胶去除', 12.90, 500, 110302, 'https://picsum.photos/id/830/600/600', 1, 623, NOW());

-- 110303 洗车液
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('龟牌洗车液', '高泡洗车液，去污上光，中性配方', 29.90, 300, 110303, 'https://picsum.photos/id/831/600/600', 1, 456, NOW()),
                                                                                                                                           ('3M洗车液', '浓缩洗车液，强力去污，不伤车漆', 39.90, 250, 110303, 'https://picsum.photos/id/832/600/600', 1, 421, NOW()),
                                                                                                                                           ('化学小子洗车液', '高端洗车液，中性配方，上光保护', 99.00, 100, 110303, 'https://picsum.photos/id/833/600/600', 1, 156, NOW()),
                                                                                                                                           ('车仆洗车液', '高泡洗车液，去污清洁，经济实惠', 19.90, 350, 110303, 'https://picsum.photos/id/834/600/600', 1, 498, NOW());

-- 110304 车蜡
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('龟牌白金蜡', '固体车蜡，上光保护，防紫外线', 59.90, 200, 110304, 'https://picsum.photos/id/835/600/600', 1, 289, NOW()),
                                                                                                                                           ('3M水晶蜡', '液体车蜡，去污上光，持久保护', 79.90, 180, 110304, 'https://picsum.photos/id/836/600/600', 1, 256, NOW()),
                                                                                                                                           ('化学小子车蜡', '高端车蜡，棕榈蜡，镜面效果', 199.00, 80, 110304, 'https://picsum.photos/id/837/600/600', 1, 123, NOW()),
                                                                                                                                           ('车仆车蜡', '固体车蜡，上光保护，经济实惠', 39.90, 250, 110304, 'https://picsum.photos/id/838/600/600', 1, 321, NOW());

-- 110401 灭火器
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('车载干粉灭火器 1kg', 'ABC干粉灭火器，车载必备，消防认证', 49.90, 200, 110401, 'https://picsum.photos/id/839/600/600', 1, 289, NOW()),
                                                                                                                                           ('车载水基灭火器 2L', '水基型灭火器，环保无毒，可灭电火', 79.90, 150, 110401, 'https://picsum.photos/id/840/600/600', 1, 213, NOW()),
                                                                                                                                           ('气溶胶灭火器', '便携式灭火器，无残留，车载专用', 129.00, 100, 110401, 'https://picsum.photos/id/841/600/600', 1, 156, NOW()),
                                                                                                                                           ('二氧化碳灭火器 2kg', 'CO2灭火器，精密仪器专用', 159.00, 80, 110401, 'https://picsum.photos/id/842/600/600', 1, 123, NOW());

-- 110402 安全锤
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('多功能安全锤', '破窗+割安全带，车载应急必备', 29.90, 300, 110402, 'https://picsum.photos/id/843/600/600', 1, 456, NOW()),
                                                                                                                                           ('汽车安全锤 铝合金', '铝合金材质，破窗神器，应急逃生', 19.90, 350, 110402, 'https://picsum.photos/id/844/600/600', 1, 498, NOW()),
                                                                                                                                           ('车载安全锤 带手电筒', '破窗+割安全带+手电筒，多功能', 39.90, 250, 110402, 'https://picsum.photos/id/845/600/600', 1, 421, NOW()),
                                                                                                                                           ('迷你安全锤', '小巧便携，破窗神器，钥匙扣式', 9.90, 400, 110402, 'https://picsum.photos/id/846/600/600', 1, 523, NOW());

-- 110403 急救包
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('车载急救包 28件套', '应急医疗用品，车载必备', 59.90, 200, 110403, 'https://picsum.photos/id/847/600/600', 1, 289, NOW()),
                                                                                                                                           ('户外急救包 40件套', '户外旅行必备，多功能急救', 99.00, 150, 110403, 'https://picsum.photos/id/848/600/600', 1, 213, NOW()),
                                                                                                                                           ('家庭急救包 50件套', '家庭应急医疗用品，全面防护', 129.00, 120, 110403, 'https://picsum.photos/id/849/600/600', 1, 189, NOW()),
                                                                                                                                           ('便携急救包 18件套', '小巧便携，随身应急必备', 29.90, 250, 110403, 'https://picsum.photos/id/850/600/600', 1, 321, NOW());

-- 110404 三角牌
INSERT INTO `tb_product` (`name`, `description`, `price`, `stock`, `category_id`, `image_url`, `status`, `sales_count`, `create_time`) VALUES
                                                                                                                                           ('汽车三角警示牌', '国标反光，折叠设计，车载必备', 29.90, 300, 110404, 'https://picsum.photos/id/851/600/600', 1, 456, NOW()),
                                                                                                                                           ('反光三角警示牌', '高强度反光，夜间可见，安全警示', 39.90, 250, 110404, 'https://picsum.photos/id/852/600/600', 1, 421, NOW()),
                                                                                                                                           ('折叠三角警示牌', '便携折叠，国标认证，应急必备', 19.90, 350, 110404, 'https://picsum.photos/id/853/600/600', 1, 498, NOW()),
                                                                                                                                           ('车载三角警示牌 大号', '大号尺寸，更醒目，安全警示', 49.90, 200, 110404, 'https://picsum.photos/id/854/600/600', 1, 389, NOW());


