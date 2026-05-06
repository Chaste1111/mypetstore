package csu.web.mypetstore.persistence.Impl;

import csu.web.mypetstore.domain.Cart;
import csu.web.mypetstore.domain.CartItem;
import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.persistence.CartDao;
import csu.web.mypetstore.persistence.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartDaoImpl implements CartDao {
    private static final String CREATE_CART = "INSERT INTO cart (cart_id, user_id, create_time, update_time) VALUES (?, ?, ?, ?)";
    private static final String GET_CART_BY_USERID = "SELECT * FROM cart WHERE user_id = ?";
    private static final String UPDATE_CART = "UPDATE cart SET update_time = ? WHERE cart_id = ?";
    private static final String ADD_CART_ITEM = "INSERT INTO cart_item (cart_id, item_id, quantity, in_stock) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_CART_ITEM = "UPDATE cart_item SET quantity = ?, in_stock = ? WHERE cart_id = ? AND item_id = ?";
    private static final String REMOVE_CART_ITEM = "DELETE FROM cart_item WHERE cart_id = ? AND item_id = ?";
    private static final String GET_CART_ITEMS = "SELECT * FROM cart_item WHERE cart_id = ?";
    private static final String CLEAR_CART = "DELETE FROM cart_item WHERE cart_id = ?";

    @Override
    public void createCart(Cart cart) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CREATE_CART)) {
            pstmt.setString(1, cart.getCartId());
            pstmt.setString(2, cart.getUserId());
            pstmt.setTimestamp(3, new java.sql.Timestamp(cart.getCreateTime().getTime()));
            pstmt.setTimestamp(4, new java.sql.Timestamp(cart.getUpdateTime().getTime()));
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cart getCartByUserId(String userId) {
        Cart cart = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_CART_BY_USERID)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                cart = new Cart();
                cart.setCartId(rs.getString("cart_id"));
                cart.setUserId(rs.getString("user_id"));
                cart.setCreateTime(rs.getTimestamp("create_time"));
                cart.setUpdateTime(rs.getTimestamp("update_time"));

                // 加载购物车项
                List<CartItem> items = getCartItems(cart.getCartId());
                cart.loadItemsFromList(items);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cart;
    }

    @Override
    public void updateCart(Cart cart) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_CART)) {
            pstmt.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setString(2, cart.getCartId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCartItem(CartItem cartItem) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(ADD_CART_ITEM)) {
            pstmt.setString(1, cartItem.getCartId());
            pstmt.setString(2, cartItem.getItem().getItemId());
            pstmt.setInt(3, cartItem.getQuantity());
            pstmt.setBoolean(4, cartItem.isInStock());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCartItemQuantity(String cartId, String itemId, int quantity) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_CART_ITEM)) {
            pstmt.setInt(1, quantity);
            // 库存状态需要查询Item表更新
            ItemDaoImpl itemDao = new ItemDaoImpl();
            boolean inStock = itemDao.getInventoryQuantity(itemId) > 0;
            pstmt.setBoolean(2, inStock);
            pstmt.setString(3, cartId);
            pstmt.setString(4, itemId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeCartItem(String cartId, String itemId) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(REMOVE_CART_ITEM)) {
            pstmt.setString(1, cartId);
            pstmt.setString(2, itemId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CartItem> getCartItems(String cartId) {
        List<CartItem> items = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_CART_ITEMS)) {
            pstmt.setString(1, cartId);
            ResultSet rs = pstmt.executeQuery();

            ItemDaoImpl itemDao = new ItemDaoImpl();
            while (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setCartId(cartId);
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setInStock(rs.getBoolean("in_stock"));

                String itemId = rs.getString("item_id");
                Item item = itemDao.getItem(itemId);
                cartItem.setItem(item);

                items.add(cartItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public void clearCart(String cartId) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CLEAR_CART)) {
            pstmt.setString(1, cartId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}