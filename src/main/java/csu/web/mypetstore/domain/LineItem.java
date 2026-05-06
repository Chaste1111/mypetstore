package csu.web.mypetstore.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单项实体类
 * 对应数据库表：LINEITEM
 */
@Data
@TableName("LINEITEM")
public class LineItem {

    /**
     * 订单ID
     */
    @TableField("ORDERID")
    private String orderId;

    /**
     * 行号
     */
    @TableField("LINENUM")
    private int lineNumber;

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
     * 单价
     */
    @TableField("UNITPRICE")
    private BigDecimal unitPrice;

    /**
     * 关联的商品对象（业务字段）
     */
    @TableField(exist = false)
    private Item item;

    /**
     * 计算单项总价
     */
    public BigDecimal getTotal() {
        return unitPrice.multiply(new BigDecimal(quantity));
    }
}