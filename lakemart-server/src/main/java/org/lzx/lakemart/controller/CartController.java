package org.lzx.lakemart.controller;

import jakarta.validation.Valid;
import org.lzx.lakemart.model.dto.AddToCartRequest;
import org.lzx.lakemart.model.dto.UpdateCartRequest;
import org.lzx.lakemart.model.vo.CartItemVO;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    /**
     * 添加商品到购物车
     * 需要登录，从 Security 上下文中获取当前用户ID
     */
    @PostMapping("/add")
    public Result<String> addToCart(@AuthenticationPrincipal Long userId,
                                    @Valid @RequestBody AddToCartRequest request) {
        cartItemService.addToCart(userId, request.getProductId(), request.getQuantity());
        return Result.success("添加成功");
    }

    /**
     * 获取当前用户的购物车列表
     */
    @GetMapping("/list")
    public Result<List<CartItemVO>> getCartList(@AuthenticationPrincipal Long userId) {
        List<CartItemVO> list = cartItemService.getCartList(userId);
        return Result.success(list);
    }

    /**
     * 修改购物车中商品的数量
     */
    @PutMapping("/update")
    public Result<String> updateQuantity(@Valid @RequestBody UpdateCartRequest request) {
        cartItemService.updateQuantity(request.getCartItemId(), request.getQuantity());
        return Result.success("修改成功");
    }

    /**
     * 删除购物车中的某个商品
     */
    @DeleteMapping("/remove/{cartItemId}")
    public Result<String> removeCartItem(@PathVariable("cartItemId") Long cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return Result.success("删除成功");
    }

    /**
     * 清空购物车（下单后调用）
     */
    @DeleteMapping("/clear")
    public Result<String> clearCart(@AuthenticationPrincipal Long userId) {
        cartItemService.clearCart(userId);
        return Result.success("清空成功");
    }
}