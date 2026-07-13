package org.lzx.lakemart.service.client;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lzx.lakemart.model.entity.CartItem;
import org.lzx.lakemart.model.vo.CartItemVO;
import java.util.List;
/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 * @author lzx
 * @since 2026-04-20
 */
public interface CartItemService extends IService<CartItem> {
    /**
     * 添加商品到购物车
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 数量
     */
    void addToCart(Long userId, Long productId, Integer quantity);

    /**
     * 获取用户的购物车列表（含商品详情）
     * @param userId 用户ID
     * @return 购物车项VO列表
     */
    List<CartItemVO> getCartList(Long userId);

    /**
     * 修改购物车中某个商品的数量
     * @param cartItemId 购物车项ID
     * @param quantity 新数量
     */
    void updateQuantity(Long cartItemId, Integer quantity);

    /**
     * 删除购物车中的某个商品
     * @param cartItemId 购物车项ID
     */
    void removeCartItem(Long cartItemId);

    /**
     * 清空用户购物车
     * @param userId 用户ID
     */
    void clearCart(Long userId);
}
