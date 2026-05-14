package csu.web.mypetstore.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 购物车实体类
 * 对应数据库表：CART（根据你的建表脚本，只有CARTID字段）
 */
@Data
@TableName("CART")
public class Cart implements Serializable {

    private static final long serialVersionUID = 8329559983943337176L;

    /**
     * 购物车ID，主键（对应建表脚本中的CARTID）
     */
    @TableId(value = "CARTID", type = IdType.INPUT)
    private String cartId;

    /**
     * 用户ID（业务字段，与cartId相同，不存在于数据库）
     */
    @TableField(exist = false)
    private String userId;

    /**
     * 创建时间（业务字段，不存在于数据库）
     */
    @TableField(exist = false)
    private Date createTime;

    /**
     * 更新时间（业务字段，不存在于数据库）
     */
    @TableField(exist = false)
    private Date updateTime;

    /**
     * 购物车商品项映射（内存中的业务字段）
     */
    @TableField(exist = false)
    private final Map<String, CartItem> itemMap = Collections.synchronizedMap(new HashMap<>());

    /**
     * 购物车商品项列表（内存中的业务字段）
     */
    @TableField(exist = false)
    private final List<CartItem> itemList = new ArrayList<>();

    /**
     * 购物车商品项列表（数据库字段）
     */
    @TableField(exist = false)
    private List<CartItem> cartItems = new ArrayList<>();

    /**
     * 获取购物车商品项列表（用于JSON序列化）
     * @return 购物车商品项列表
     */
    public List<CartItem> getItems() {
        return this.itemList;
    }

    /**
     * 从列表加载商品项
     */
    public void loadItemsFromList(List<CartItem> items) {
        itemMap.clear();
        itemList.clear();
        for (CartItem item : items) {
            itemMap.put(item.getItem().getItemId(), item);
            itemList.add(item);
        }
    }

    /**
     * 获取购物车商品项迭代器
     */
    public Iterator<CartItem> getCartItems() {
        return this.itemList.iterator();
    }

    /**
     * 获取购物车商品项列表
     */
    public List<CartItem> getCartItemList() {
        return this.itemList;
    }

    /**
     * 获取商品数量
     */
    public int getNumberOfItems() {
        return this.itemList.size();
    }

    /**
     * 添加商品到购物车
     *
     * @param item     商品
     * @param inStock  是否有库存
     */
    public void addItem(Item item, boolean inStock) {
        CartItem cartItem = itemMap.get(item.getItemId());
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setItem(item);
            cartItem.setQuantity(0);
            cartItem.setInStock(inStock);
            itemMap.put(item.getItemId(), cartItem);
            itemList.add(cartItem);
        }
        cartItem.incrementQuantity();
    }

    /**
     * 根据商品ID移除购物车项
     *
     * @param itemId 商品ID
     */
    public void removeItemById(String itemId) {
        CartItem cartItem = itemMap.remove(itemId);
        if (cartItem != null) {
            itemList.remove(cartItem);
        }
    }

    /**
     * 获取商品映射
     *
     * @return 商品映射
     */
    public Map<String, CartItem> getItemMap() {
        return this.itemMap;
    }
}