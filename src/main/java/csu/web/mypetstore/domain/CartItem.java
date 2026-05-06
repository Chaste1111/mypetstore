package csu.web.mypetstore.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车商品项实体类
 * 对应数据库表：CARTITEM
 */
@Data
@TableName("CARTITEM")
public class CartItem implements Serializable {

    private static final long serialVersionUID = 6620528781626504362L;

    /**
     * 购物车ID
     */
    @TableField("CARTID")
    private String cartId;

    /**
     * 商品ID
     */
    @TableField("ITEMID")
    private String itemId;

    /**
     * 数量
     */
    @TableField("QUANTITY")
    private int quantity;

    /**
     * 是否有库存（业务字段）
     */
    @TableField(exist = false)
    private boolean inStock;

    /**
     * 总价（业务字段）
     */
    @TableField(exist = false)
    private BigDecimal total;

    /**
     * 关联的商品对象（业务字段）
     */
    @TableField(exist = false)
    private Item item;

    /**
     * 设置商品，同时计算总价
     */
    public void setItem(Item item) {
        this.item = item;
        this.calculateTotal();
    }

    /**
     * 设置数量，同时计算总价
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.calculateTotal();
    }

    /**
     * 增加数量
     */
    public void incrementQuantity() {
        ++this.quantity;
        this.calculateTotal();
    }

    /**
     * 计算总价
     */
    private void calculateTotal() {
        if (this.item != null && this.item.getListPrice() != null) {
            this.total = this.item.getListPrice().multiply(new BigDecimal(this.quantity));
        } else {
            this.total = null;
        }
    }
}