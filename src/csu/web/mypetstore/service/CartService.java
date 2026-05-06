package csu.web.mypetstore.service;

import csu.web.mypetstore.domain.Cart;
import csu.web.mypetstore.domain.CartItem;
import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.persistence.Impl.CartDaoImpl;
import csu.web.mypetstore.persistence.CartDao;

import java.util.Date;
import java.util.UUID;

public class CartService {
    private CartDao cartDao = new CartDaoImpl();
    private CatalogService catalogService = new CatalogService();

    // 获取用户购物车（不存在则创建）
    public Cart getCartByUserId(String userId) {
        Cart cart = cartDao.getCartByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
            cart.setUserId(userId);
            cart.setCreateTime(new Date());
            cart.setUpdateTime(new Date());
            cartDao.createCart(cart);
        }
        return cart;
    }

    // 添加商品到购物车
    public void addItemToCart(String userId, String itemId) {
        Cart cart = getCartByUserId(userId);

        if (cart.containsItemId(itemId)) {
            cart.incrementQuantityByItemId(itemId);
        } else {
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

    // 更新购物车项数量
    public void updateCartItemQuantity(String userId, String itemId, int quantity) {
        Cart cart = getCartByUserId(userId);
        cart.setQuantityByItemId(itemId, quantity);
        if (quantity <= 0) {
            cartDao.removeCartItem(cart.getCartId(), itemId);
        } else {
            cartDao.updateCartItemQuantity(cart.getCartId(), itemId, quantity);
        }
        cart.setUpdateTime(new Date());
        cartDao.updateCart(cart);
    }

    // 从购物车移除商品
    public void removeCartItem(String userId, String itemId) {
        Cart cart = getCartByUserId(userId);
        cart.removeItemById(itemId);
        cartDao.removeCartItem(cart.getCartId(), itemId);
        cart.setUpdateTime(new Date());
        cartDao.updateCart(cart);
    }

    // 清空购物车
    public void clearCart(String userId) {
        Cart cart = getCartByUserId(userId);
        cartDao.clearCart(cart.getCartId());
        cart.getCartItemList().clear();
        cart.getItemMap().clear();
        cart.setUpdateTime(new Date());
        cartDao.updateCart(cart);
    }
}