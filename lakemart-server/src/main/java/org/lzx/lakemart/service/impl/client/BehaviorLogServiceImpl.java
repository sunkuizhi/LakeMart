package org.lzx.lakemart.service.impl.client;

import org.lzx.lakemart.model.dto.BehaviorMessage;
import org.lzx.lakemart.model.dto.BehaviorMonitorDto;
import org.lzx.lakemart.service.client.BehaviorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BehaviorLogServiceImpl implements BehaviorLogService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveBehaviorLog(BehaviorMessage message) {
        String sql = "INSERT INTO user_behavior_log (user_id, action, product_id, create_time) VALUES (?, ?, ?, ?)";
        LocalDateTime createTime = message.getTs() != null ?
                LocalDateTime.parse(message.getTs(), DateTimeFormatter.ISO_LOCAL_DATE_TIME) :
                LocalDateTime.now();
        jdbcTemplate.update(sql, message.getUserId(), message.getAction(), message.getProductId(), createTime);
    }

    @Override
    public List<BehaviorMonitorDto> getRecentBehaviors(int limit) {
        String sql = "SELECT " +
                "b.user_id, " +
                "b.action, " +
                "b.product_id, " +
                "b.create_time, " +
                "COALESCE(u.username, '未知') AS username, " +
                "COALESCE(p.name, '未知商品') AS product_name " +
                "FROM user_behavior_log b " +
                "LEFT JOIN tb_user u ON b.user_id = u.id " +
                "LEFT JOIN tb_product p ON b.product_id = p.id " +
                "WHERE b.user_id IS NOT NULL AND b.product_id IS NOT NULL " +
                "ORDER BY b.create_time DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{limit}, (rs, rowNum) -> {
            BehaviorMonitorDto dto = new BehaviorMonitorDto();
            dto.setUserId(rs.getLong("user_id"));
            dto.setUsername(rs.getString("username"));
            dto.setAction(rs.getString("action"));
            dto.setProductId(rs.getLong("product_id"));
            dto.setProductName(rs.getString("product_name"));
            // 处理时间格式
            java.sql.Timestamp ts = rs.getTimestamp("create_time");
            if (ts != null) {
                dto.setTime(ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                dto.setTime("");
            }
            return dto;
        });
    }
}