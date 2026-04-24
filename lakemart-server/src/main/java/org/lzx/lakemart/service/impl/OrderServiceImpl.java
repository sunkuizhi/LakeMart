package org.lzx.lakemart.service.impl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.hutool.core.util.IdUtil; // 需要添加 hutool 依赖，或自己实现订单号生成
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.mapper.*;
import org.lzx.lakemart.model.dto.OrderPageQueryDTO;
import org.lzx.lakemart.model.dto.OrderStatisticsDTO;
import org.lzx.lakemart.model.entity.*;
import org.lzx.lakemart.model.vo.DailyAmountVO;
import org.lzx.lakemart.model.vo.OrderItemVO;
import org.lzx.lakemart.model.vo.OrderVO;
import org.lzx.lakemart.model.vo.ProductSalesVO;
import org.lzx.lakemart.service.OrderService;
import org.lzx.lakemart.service.PointsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressMapper addressMapper;  // 注入地址Mapper
    @Autowired
    private PointsLogService pointsLogService;

    /**
     * 创建订单（核心业务）
     * 1. 根据购物车项ID获取商品信息
     * 2. 扣减库存（乐观锁，防止超卖）
     * 3. 计算总金额
     * 4. 生成订单号和订单记录
     * 5. 保存订单项（快照）
     * 6. 清空购物车
     * 7. 增加用户积分（可选，按金额比例）
     */
    @Override
    @Transactional
    public OrderVO createOrder(Long userId, List<Long> cartItemIds,Long addressId) {
        // 1. 查询购物车项（同时校验所有权）
        List<CartItem> cartItems = cartItemMapper.selectBatchIds(cartItemIds);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车项不存在");
        }
        // 确保所有购物车项属于当前用户
        for (CartItem item : cartItems) {
            if (!item.getUserId().equals(userId)) {
                throw new RuntimeException("购物车项不属于当前用户");
            }
        }

        // 2. 准备订单项列表和扣减库存
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            Product product = productMapper.selectById(cartItem.getProductId());
            if (product == null) {
                throw new RuntimeException("商品不存在: " + cartItem.getProductId());
            }
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("商品库存不足: " + product.getName());
            }
            // 扣减库存（这里使用乐观锁，需要在 ProductMapper 中实现 updateStock 方法）
            int updateRows = productMapper.decreaseStock(product.getId(), cartItem.getQuantity());
            if (updateRows == 0) {
                throw new RuntimeException("扣减库存失败，请重试");
            }
            // 构建订单项（快照）
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
            // 累加总金额
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        // 3. 查询收货地址
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("收货地址不存在或无权限");
        }
        String fullAddress = address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress();

        // 4. 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        // 设置收货信息
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setReceiverAddress(fullAddress);
        this.save(order);

        // 4. 保存订单项（关联订单ID）
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }
        orderItemMapper.batchInsert(orderItems); // 需要自定义批量插入方法，或循环插入

        // 5. 清空购物车（只删除选中的项）
        cartItemMapper.deleteBatchIds(cartItemIds);

        // 6. 增加用户积分（例如每10元增加1积分）
        int pointsToAdd = totalAmount.divide(BigDecimal.TEN).intValue();
        if (pointsToAdd > 0) {
            userMapper.addPoints(userId, pointsToAdd);
            // 记录积分明细
            pointsLogService.recordPoints(userId, pointsToAdd, "ORDER_CREATE", order.getId(), "下单获得积分");
        }

        // 7. 返回订单VO
        return buildOrderVO(order, orderItems);
    }

    /**
     * 获取用户的订单列表
     */
    @Override
    public List<OrderVO> getUserOrders(Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId).orderByDesc(Order::getCreateTime);
        List<Order> orders = this.list(wrapper);
        if (orders.isEmpty()) {
            return List.of();
        }
        // 批量查询订单项（优化性能，这里简单循环，实际可用 SQL 批量）
        List<OrderVO> voList = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
            voList.add(buildOrderVO(order, items));
        }
        return voList;
    }

    /**
     * 获取订单详情
     */
    @Override
    public OrderVO getOrderDetail(Long orderId, Long userId) {
        Order order = this.getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在或无权限");
        }
        List<OrderItem> items = orderItemMapper.selectByOrderId(orderId);
        return buildOrderVO(order, items);
    }

    // ---------- 辅助方法 ----------
    private String generateOrderNo() {
        // 使用 hutool 工具生成唯一订单号，也可自己实现：时间戳+随机数
        return IdUtil.getSnowflakeNextIdStr(); // 需要引入 hutool-all 依赖
        // 或者简单实现：System.currentTimeMillis() + (int)(Math.random()*10000)
    }

    private OrderVO buildOrderVO(Order order, List<OrderItem> items) {
        List<OrderItemVO> itemVOs = items.stream().map(item -> OrderItemVO.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build()).collect(Collectors.toList());

        return OrderVO.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .statusDesc(getStatusDesc(order.getStatus()))
                .createTime(order.getCreateTime())
                .items(itemVOs)
                .build();
    }

    private String getStatusDesc(Integer status) {
        switch (status) {
            case 0: return "待支付";
            case 1: return "已支付";
            case 2: return "已发货";
            case 3: return "已完成";
            case 4: return "已取消";
            default: return "未知";
        }
    }
    // 用于恢复库存
    @Override
    @Transactional
    public void payOrder(Long orderId, Long userId) {
        Order order = this.getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在或无权限");
        }
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确，无法支付");
        }
        // 更新订单状态为已支付，记录支付时间
        order.setStatus(1);
        order.setPayTime(LocalDateTime.now());
        this.updateById(order);
        // 实际支付逻辑（调用第三方支付网关）省略，这里仅模拟
    }
    // 需要恢复库存
    @Override
    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        Order order = this.getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在或无权限");
        }
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确，无法取消");
        }
        // 查询订单项，恢复商品库存
        List<OrderItem> items = orderItemMapper.selectByOrderId(orderId);
        for (OrderItem item : items) {
            productMapper.increaseStock(item.getProductId(), item.getQuantity());
        }
        // 计算应扣除的积分
        int pointsToDeduct = order.getTotalAmount().divide(BigDecimal.TEN).intValue();
        if (pointsToDeduct > 0) {
            userMapper.addPoints(userId, -pointsToDeduct);
            pointsLogService.recordPoints(userId, -pointsToDeduct, "ORDER_CANCEL", order.getId(), "取消订单扣回积分");
        }
        // 更新订单状态为已取消
        order.setStatus(4);
        this.updateById(order);
    }
    @Override
    public Page<OrderVO> adminQueryOrders(OrderPageQueryDTO query) {
        Page<Order> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (query.getUserId() != null) {
            wrapper.eq(Order::getUserId, query.getUserId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(Order::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> orderPage = baseMapper.selectPage(page, wrapper);

        // 转换 Order -> OrderVO
        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        List<OrderVO> voList = orderPage.getRecords().stream().map(order -> {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
            return buildOrderVO(order, items);
        }).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional
    public void adminUpdateOrderStatus(Long orderId, Integer status) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        // 状态转换校验：只有已支付(1)可以发货(2)；发货后可以完成(3)；取消(4)仅限待支付(0)或已支付(1)但未发货
        if (status == 2 && order.getStatus() != 1) {
            throw new RuntimeException("只有已支付的订单才能发货");
        }
        if (status == 3 && order.getStatus() != 2) {
            throw new RuntimeException("只有已发货的订单才能完成");
        }
        if (status == 4 && (order.getStatus() != 0 && order.getStatus() != 1)) {
            throw new RuntimeException("当前状态不可取消");
        }
        order.setStatus(status);
        if (status == 2) {
            order.setDeliveryTime(LocalDateTime.now());
        } else if (status == 3) {
            order.setCompleteTime(LocalDateTime.now());
        }
        this.updateById(order);
    }

    @Override
    public List<OrderStatisticsDTO> getDailyStatistics(LocalDate startDate, LocalDate endDate) {
        // 默认最近7天（不包含今天）
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(7);
        }
        if (endDate == null) {
            endDate = LocalDate.now(); // 不包含当天
        }
        return baseMapper.getDailyStatistics(startDate, endDate);
    }
    @Override
    public List<ProductSalesVO> getHotProducts(int limit) {
        return baseMapper.getHotProducts(limit);
    }

    @Override
    public List<DailyAmountVO> getDailySalesAmount(LocalDate startDate, LocalDate endDate) {
        // 默认最近7天（不包含今天）
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(7);
        }
        if (endDate == null) {
            endDate = LocalDate.now(); // 不包含当天
        }
        return baseMapper.getDailySalesAmount(startDate, endDate);
    }

    @Override
    public OrderVO getOrderDetailForAdmin(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        List<OrderItem> items = orderItemMapper.selectByOrderId(orderId);
        return buildOrderVO(order, items);
    }

    @Override
    public Page<OrderVO> getUserOrdersPage(Long userId, int pageNum, int pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId).orderByDesc(Order::getCreateTime);
        Page<Order> orderPage = baseMapper.selectPage(page, wrapper);
        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        voPage.setRecords(orderPage.getRecords().stream().map(order -> {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
            return buildOrderVO(order, items);
        }).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    @Transactional
    public void confirmReceipt(Long orderId, Long userId) {
        Order order = this.getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在或无权限");
        }
        if (order.getStatus() != 2) {
            throw new RuntimeException("只有已发货的订单才能确认收货");
        }
        order.setStatus(3);
        order.setCompleteTime(LocalDateTime.now());
        this.updateById(order);
    }
}