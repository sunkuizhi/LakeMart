package org.lzx.lakemart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lzx.lakemart.mapper.CartItemMapper;
import org.lzx.lakemart.mapper.ProductMapper;
import org.lzx.lakemart.model.entity.CartItem;
import org.lzx.lakemart.model.entity.Product;
import org.lzx.lakemart.model.vo.CartItemVO;
import org.lzx.lakemart.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 添加商品到购物车：如果已存在则增加数量，否则新增记录
     */
    @Override
    @Transactional
    public void addToCart(Long userId, Long productId, Integer quantity) {
        // 查询是否已存在该商品在购物车中
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId).eq(CartItem::getProductId, productId);
        CartItem existItem = baseMapper.selectOne(wrapper);

        if (existItem != null) {
            // 已存在，增加数量
            existItem.setQuantity(existItem.getQuantity() + quantity);
            baseMapper.updateById(existItem);
        } else {
            // 不存在，新增记录
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            baseMapper.insert(newItem);
        }
    }

    /**
     * 获取用户购物车列表，并关联查询商品信息（名称、价格、图片）
     */
    @Override
    public List<CartItemVO> getCartList(Long userId) {
        // 查询用户的所有购物车项
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId);
        List<CartItem> cartItems = baseMapper.selectList(wrapper);

        if (cartItems.isEmpty()) {
            return List.of();
        }

        // 查询商品信息并组装 VO
        return cartItems.stream().map(item -> {
            Product product = productMapper.selectById(item.getProductId());
            if (product == null) {
                return null; // 商品已被删除，可忽略
            }
            return CartItemVO.builder()
                    .cartId(item.getId())
                    .productId(product.getId())
                    .productName(product.getName())
                    .productImage(product.getImageUrl())
                    .price(product.getPrice())
                    .quantity(item.getQuantity())
                    .subtotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .build();
        }).filter(vo -> vo != null).collect(Collectors.toList());
    }

    /**
     * 修改购物车中某个商品的数量
     */
    @Override
    @Transactional
    public void updateQuantity(Long cartItemId, Integer quantity) {
        CartItem item = baseMapper.selectById(cartItemId);
        if (item == null) {
            throw new RuntimeException("购物车项不存在");
        }
        item.setQuantity(quantity);
        baseMapper.updateById(item);
    }

    /**
     * 删除购物车中的某个商品
     */
    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        baseMapper.deleteById(cartItemId);
    }

    /**
     * 清空用户购物车
     */
    @Override
    @Transactional
    public void clearCart(Long userId) {
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId);
        baseMapper.delete(wrapper);
    }
}