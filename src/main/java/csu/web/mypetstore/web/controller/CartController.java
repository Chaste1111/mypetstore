package csu.web.mypetstore.web.controller;

import csu.web.mypetstore.common.Result;
import csu.web.mypetstore.domain.Cart;
import csu.web.mypetstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 购物车 RESTful 控制器
 * 提供购物车操作相关接口
 */
@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 获取用户购物车
     * GET /api/carts/user/{userId}
     *
     * @param userId 用户ID
     * @return 购物车信息
     */
    @GetMapping("/user/{userId}")
    public Result<Cart> getCartByUserId(@PathVariable String userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return Result.success(cart);
    }

    /**
     * 添加商品到购物车
     * POST /api/carts/user/{userId}/items/{itemId}
     *
     * @param userId 用户ID
     * @param itemId 商品ID
     * @return 操作结果
     */
    @PostMapping("/user/{userId}/items/{itemId}")
    public Result<String> addItemToCart(@PathVariable String userId, @PathVariable String itemId) {
        cartService.addItemToCart(userId, itemId);
        return Result.success("商品已添加到购物车");
    }

    /**
     * 更新购物车商品数量
     * PUT /api/carts/user/{userId}/items/{itemId}
     *
     * @param userId   用户ID
     * @param itemId   商品ID
     * @param quantity 数量
     * @return 操作结果
     */
    @PutMapping("/user/{userId}/items/{itemId}")
    public Result<String> updateCartItemQuantity(
            @PathVariable String userId,
            @PathVariable String itemId,
            @RequestParam int quantity) {
        cartService.updateCartItemQuantity(userId, itemId, quantity);
        return Result.success("购物车已更新");
    }

    /**
     * 从购物车移除商品
     * DELETE /api/carts/user/{userId}/items/{itemId}
     *
     * @param userId 用户ID
     * @param itemId 商品ID
     * @return 操作结果
     */
    @DeleteMapping("/user/{userId}/items/{itemId}")
    public Result<String> removeCartItem(@PathVariable String userId, @PathVariable String itemId) {
        cartService.removeCartItem(userId, itemId);
        return Result.success("商品已从购物车移除");
    }

    /**
     * 清空购物车
     * DELETE /api/carts/user/{userId}
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/user/{userId}")
    public Result<String> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return Result.success("购物车已清空");
    }
}