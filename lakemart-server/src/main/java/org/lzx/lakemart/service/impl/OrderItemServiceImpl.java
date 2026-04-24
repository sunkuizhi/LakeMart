package org.lzx.lakemart.service.impl;

import org.lzx.lakemart.model.entity.OrderItem;
import org.lzx.lakemart.mapper.OrderItemMapper;
import org.lzx.lakemart.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单项表 服务实现类
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
