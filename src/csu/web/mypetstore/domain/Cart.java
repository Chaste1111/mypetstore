package csu.web.mypetstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class Cart implements Serializable {
    private static final long serialVersionUID = 8329559983943337176L;
    private final Map<String, CartItem> itemMap = Collections.synchronizedMap(new HashMap<String,CartItem>());
    private final List<CartItem> itemList = new ArrayList<CartItem>();
    private String cartId;
    private String userId;
    private Date createTime;
    private Date updateTime;
    private List<CartItem> cartItems = new ArrayList<>();

    public void loadItemsFromList(List<CartItem> items) {
        itemMap.clear();
        itemList.clear();
        for (CartItem item : items) {
            itemMap.put(item.getItem().getItemId(), item);
            itemList.add(item);
        }
    }

    // 原有方法保持不变，新增getter/setter
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Map<String, CartItem> getItemMap() {
        return itemMap;
    }

    public List<CartItem> getItemList() {
        return itemList;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Iterator<CartItem> getCartItems() {
        return this.itemList.iterator();
    }

    public List<CartItem> getCartItemList() {
        return this.itemList;
    }

    public int getNumberOfItems() {
        return this.itemList.size();
    }

    public Iterator<CartItem> getAllCartItems() {
        return this.itemList.iterator();
    }

    public boolean containsItemId(String itemId) {
        return this.itemMap.containsKey(itemId);
    }

    public void addItem(Item item, boolean isInStock) {
        CartItem cartItem = (CartItem)this.itemMap.get(item.getItemId());
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setItem(item);
            cartItem.setQuantity(0);
            cartItem.setInStock(isInStock);
            this.itemMap.put(item.getItemId(), cartItem);
            this.itemList.add(cartItem);
        }

        cartItem.incrementQuantity();
    }

    public Item removeItemById(String itemId) {
        CartItem cartItem = (CartItem)this.itemMap.remove(itemId);
        if (cartItem == null) {
            return null;
        } else {
            this.itemList.remove(cartItem);
            return cartItem.getItem();
        }
    }

    public void incrementQuantityByItemId(String itemId) {
        CartItem cartItem = (CartItem)this.itemMap.get(itemId);
        cartItem.incrementQuantity();
    }

    public void setQuantityByItemId(String itemId, int quantity) {
        CartItem cartItem = (CartItem)this.itemMap.get(itemId);
        cartItem.setQuantity(quantity);
    }

    public BigDecimal getSubTotal() {
        BigDecimal subTotal = new BigDecimal("0");
        BigDecimal listPrice;
        BigDecimal quantity;
        for(Iterator items = this.getAllCartItems(); items.hasNext(); subTotal = subTotal.add(listPrice.multiply(quantity))) {
            CartItem cartItem = (CartItem)items.next();
            Item item = cartItem.getItem();
            listPrice = item.getListPrice();
            quantity = new BigDecimal(String.valueOf(cartItem.getQuantity()));
        }

        return subTotal;
    }

}
