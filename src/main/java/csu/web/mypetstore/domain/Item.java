package csu.web.mypetstore.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品明细实体类
 * 对应数据库表：ITEM
 */
@Data
@TableName("ITEM")
public class Item implements Serializable {

    private static final long serialVersionUID = -2159121673445254631L;

    /**
     * 商品ID，主键
     */
    @TableId(value = "ITEMID", type = IdType.INPUT)
    private String itemId;

    /**
     * 产品ID
     */
    @TableField("PRODUCTID")
    private String productId;

    /**
     * 标价
     */
    @TableField("LISTPRICE")
    private BigDecimal listPrice;

    /**
     * 单位成本
     */
    @TableField("UNITCOST")
    private BigDecimal unitCost;

    /**
     * 供应商ID
     */
    @TableField("SUPPLIER")
    private int supplierId;

    /**
     * 状态
     */
    @TableField("STATUS")
    private String status;

    /**
     * 属性1
     */
    @TableField("ATTR1")
    private String attribute1;

    /**
     * 属性2
     */
    @TableField("ATTR2")
    private String attribute2;

    /**
     * 属性3
     */
    @TableField("ATTR3")
    private String attribute3;

    /**
     * 属性4
     */
    @TableField("ATTR4")
    private String attribute4;

    /**
     * 属性5
     */
    @TableField("ATTR5")
    private String attribute5;

    /**
     * 关联的产品对象（非数据库字段）
     */
    @TableField(exist = false)
    private Product product;

    /**
     * 库存数量（非数据库字段，用于业务逻辑）
     */
    @TableField(exist = false)
    private int quantity;
}