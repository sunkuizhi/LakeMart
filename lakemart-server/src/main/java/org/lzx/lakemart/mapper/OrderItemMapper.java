package org.lzx.lakemart.mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lzx.lakemart.model.entity.OrderItem;

import java.util.List;

/**
 * <p>
 * 订单项表 Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    // OrderItemMapper.java
    void batchInsert(@Param("list") List<OrderItem> items);
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);
}
