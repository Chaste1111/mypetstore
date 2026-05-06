package csu.web.mypetstore.persistence;

import csu.web.mypetstore.domain.Cart;
import csu.web.mypetstore.domain.CartItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 购物车数据访问接口
 * 继承 MyBatis Plus 的 BaseMapper，自动获得基础 CRUD 方法
 */
@Mapper
public interface CartDao extends BaseMapper<Cart> {

    /**
     * 创建购物车
     */
    void createCart(Cart cart);

    /**
     * 根据用户ID获取购物车
     */
    Cart getCartByUserId(String userId);

    /**
     * 更新购物车
     */
    void updateCart(Cart cart);

    /**
     * 添加购物车项
     */
    void addCartItem(CartItem cartItem);

    /**
     * 更新购物车项数量
     */
    void updateCartItemQuantity(String cartId, String itemId, int quantity);

    /**
     * 删除购物车项
     */
    void removeCartItem(String cartId, String itemId);

    /**
     * 获取购物车所有项
     */
    List<CartItem> getCartItems(String cartId);

    /**
     * 清空购物车
     */
    void clearCart(String cartId);
}