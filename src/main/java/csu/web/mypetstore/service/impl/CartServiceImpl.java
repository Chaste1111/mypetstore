package csu.web.mypetstore.service.impl;

import csu.web.mypetstore.domain.Cart;
import csu.web.mypetstore.domain.CartItem;
import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.persistence.CartDao;
import csu.web.mypetstore.service.CartService;
import csu.web.mypetstore.service.CatalogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * 购物车服务实现类
 * 继承 MyBatis Plus 的 ServiceImpl，自动获得基础 CRUD 方法
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartDao, Cart> implements CartService {

    @Autowired
    private CartDao cartDao;

    @Autowired
    private CatalogService catalogService;

    /**
     * 获取用户购物车（不存在则创建）
     * 使用 LambdaQueryWrapper 查询用户购物车
     */
    @Override
    public Cart getCartByUserId(String userId) {
        // 使用 LambdaQueryWrapper 构建查询条件
        Cart cart = baseMapper.selectOne(
            new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId)
        );
        
        if (cart == null) {
            // 创建新购物车
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
            cart.setUserId(userId);
            cart.setCreateTime(new Date());
            cart.setUpdateTime(new Date());
            cartDao.createCart(cart);
        }
        return cart;
    }

    /**
     * 添加商品到购物车
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addItemToCart(String userId, String itemId) {
        Cart cart = getCartByUserId(userId);

        // 检查购物车是否已包含该商品
        boolean containsItem = cart.getCartItemList().stream()
            .anyMatch(item -> item.getItemId().equals(itemId));

        if (containsItem) {
            // 增加数量
            cart.getCartItemList().stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .ifPresent(CartItem::incrementQuantity);
        } else {
            // 添加新商品
            boolean isInStock = catalogService.isItemInStock(itemId);
            Item item = catalogService.getItem(itemId);
            
            CartItem cartItem = new CartItem();
            cartItem.setCartId(cart.getCartId());
            cartItem.setItem(item);
            cartItem.setQuantity(1);
            cartItem.setInStock(isInStock);
            
            cart.addItem(item, isInStock);
            cartDao.addCartItem(cartItem);
        }

        cart.setUpdateTime(new Date());
        cartDao.updateCart(cart);
    }

    /**
     * 更新购物车项数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCartItemQuantity(String userId, String itemId, int quantity) {
        Cart cart = getCartByUserId(userId);
        
        // 更新内存中的购物车
        cart.getCartItemList().stream()
            .filter(item -> item.getItemId().equals(itemId))
            .findFirst()
            .ifPresent(item -> item.setQuantity(quantity));

        if (quantity <= 0) {
            cartDao.removeCartItem(cart.getCartId(), itemId);
            cart.removeItemById(itemId);
        } else {
            cartDao.updateCartItemQuantity(cart.getCartId(), itemId, quantity);
        }
        
        cart.setUpdateTime(new Date());
        cartDao.updateCart(cart);
    }

    /**
     * 从购物车移除商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeCartItem(String userId, String itemId) {
        Cart cart = getCartByUserId(userId);
        cart.removeItemById(itemId);
        cartDao.removeCartItem(cart.getCartId(), itemId);
        
        cart.setUpdateTime(new Date());
        cartDao.updateCart(cart);
    }

    /**
     * 清空购物车
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearCart(String userId) {
        Cart cart = getCartByUserId(userId);
        cartDao.clearCart(cart.getCartId());
        cart.getCartItemList().clear();
        cart.getItemMap().clear();
        
        cart.setUpdateTime(new Date());
    }
}