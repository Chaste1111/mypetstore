package csu.web.mypetstore.service;

import csu.web.mypetstore.domain.Cart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 购物车服务接口
 * 继承 MyBatis Plus 的 IService，自动获得基础 CRUD 方法
 */
public interface CartService extends IService<Cart> {

    /**
     * 获取用户购物车（不存在则创建）
     * @param userId 用户ID
     * @return 购物车对象
     */
    Cart getCartByUserId(String userId);

    /**
     * 添加商品到购物车
     * @param userId 用户ID
     * @param itemId 商品ID
     */
    void addItemToCart(String userId, String itemId);

    /**
     * 更新购物车项数量
     * @param userId 用户ID
     * @param itemId 商品ID
     * @param quantity 数量
     */
    void updateCartItemQuantity(String userId, String itemId, int quantity);

    /**
     * 从购物车移除商品
     * @param userId 用户ID
     * @param itemId 商品ID
     */
    void removeCartItem(String userId, String itemId);

    /**
     * 清空购物车
     * @param userId 用户ID
     */
    void clearCart(String userId);
}