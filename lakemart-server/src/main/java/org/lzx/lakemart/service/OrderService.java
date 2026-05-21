package org.lzx.lakemart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.lakemart.model.dto.OrderPageQueryDTO;
import org.lzx.lakemart.model.dto.OrderStatisticsDTO;
import org.lzx.lakemart.model.entity.Order;
import org.lzx.lakemart.model.vo.DailyAmountVO;
import org.lzx.lakemart.model.vo.OrderVO;
import org.lzx.lakemart.model.vo.ProductSalesVO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public interface OrderService extends IService<Order> {

    /**
     * 创建订单（从购物车选中商品下单）
     * @param userId 用户ID
     * @param cartItemIds 选中的购物车项ID列表
     * @return 订单VO
     */
    OrderVO createOrder(Long userId, List<Long> cartItemIds, Long addressId);
    /**
     * 模拟支付订单（仅待支付状态可支付）
     * @param orderId 订单ID
     * @param userId 当前用户ID
     */
    void payOrder(Long orderId, Long userId);

    /**
     * 取消订单（仅待支付状态可取消，取消后恢复库存）
     * @param orderId 订单ID
     * @param userId 当前用户ID
     */
    void cancelOrder(Long orderId, Long userId);

    /**
     * 获取用户的订单列表
     * @param userId 用户ID
     * @return 订单VO列表
     */
    List<OrderVO> getUserOrders(Long userId);

    /**
     * 获取订单详情（包含订单项）
     * @param orderId 订单ID
     * @param userId 用户ID（用于权限校验）
     * @return 订单VO
     */
    OrderVO getOrderDetail(Long orderId, Long userId);
    // 管理端分页查询订单
    Page<OrderVO> adminQueryOrders(OrderPageQueryDTO query);
    // 管理端修改订单状态（发货、完成、取消）
    void adminUpdateOrderStatus(Long orderId, Integer status);
    List<OrderStatisticsDTO> getDailyStatistics(LocalDate startDate, LocalDate endDate);
    /**
     * 获取热销商品排行
     * @param limit 排行数量
     * @return 商品销量列表
     */
    List<ProductSalesVO> getHotProducts(int limit);
    /**
     * 获取指定日期范围内的每日销售额
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 每日销售额列表
     */
    List<DailyAmountVO> getDailySalesAmount(LocalDate startDate, LocalDate endDate);
    OrderVO getOrderDetailForAdmin(Long orderId);
    Page<OrderVO> getUserOrdersPage(Long userId, int pageNum, int pageSize);
    void confirmReceipt(Long orderId, Long userId);

}