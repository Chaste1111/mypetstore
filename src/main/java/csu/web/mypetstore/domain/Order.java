package csu.web.mypetstore.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单实体类
 * 对应数据库表：ORDERS
 */
@Data
@TableName("ORDERS")
public class Order {

    /**
     * 订单ID，主键
     */
    @TableId(value = "ORDERID", type = IdType.INPUT)
    private String orderId;

    /**
     * 用户名
     */
    @TableField("USERID")
    private String username;

    /**
     * 订单日期
     */
    @TableField("ORDERDATE")
    private Date orderDate;

    /**
     * 配送地址1
     */
    @TableField("SHIPADDR1")
    private String shippingAddress1;

    /**
     * 配送地址2
     */
    @TableField("SHIPADDR2")
    private String shippingAddress2;

    /**
     * 配送城市
     */
    @TableField("SHIPCITY")
    private String shippingCity;

    /**
     * 配送州/省
     */
    @TableField("SHIPSTATE")
    private String shippingState;

    /**
     * 配送邮编
     */
    @TableField("SHIPZIP")
    private String shippingZip;

    /**
     * 配送国家
     */
    @TableField("SHIPCOUNTRY")
    private String shippingCountry;

    /**
     * 账单地址1
     */
    @TableField("BILLADDR1")
    private String billingAddress1;

    /**
     * 账单地址2
     */
    @TableField("BILLADDR2")
    private String billingAddress2;

    /**
     * 账单城市
     */
    @TableField("BILLCITY")
    private String billingCity;

    /**
     * 账单州/省
     */
    @TableField("BILLSTATE")
    private String billingState;

    /**
     * 账单邮编
     */
    @TableField("BILLZIP")
    private String billingZip;

    /**
     * 账单国家
     */
    @TableField("BILLCOUNTRY")
    private String billingCountry;

    /**
     * 订单总价
     */
    @TableField("TOTALPRICE")
    private BigDecimal totalPrice;

    /**
     * 快递公司
     */
    @TableField("COURIER")
    private String courier;

    /**
     * 账单收件人名字
     */
    @TableField("BILLTOFIRSTNAME")
    private String billToFirstName;

    /**
     * 账单收件人姓氏
     */
    @TableField("BILLTOLASTNAME")
    private String billToLastName;

    /**
     * 配送收件人名字
     */
    @TableField("SHIPTOFIRSTNAME")
    private String shipToFirstName;

    /**
     * 配送收件人姓氏
     */
    @TableField("SHIPTOLASTNAME")
    private String shipToLastName;

    /**
     * 信用卡号
     */
    @TableField("CREDITCARD")
    private String creditCard;

    /**
     * 有效期
     */
    @TableField("EXPRDATE")
    private String exprDate;

    /**
     * 卡类型
     */
    @TableField("CARDTYPE")
    private String cardType;

    /**
     * 语言环境
     */
    @TableField("LOCALE")
    private String locale;

    /**
     * 订单状态
     */
    @TableField("STATUS")
    private String status;

    /**
     * Deleted: /**
     * 支付类型
     * /
     * @TableField("PAYMENTTYPE")
     * private String paymentType;

    /**
     * 订单关联的商品项（业务字段）
     */
    @TableField(exist = false)
    private List<LineItem> lineItems = new ArrayList<>();

    /**
     * 添加订单项
     */
    public void addLineItem(LineItem lineItem) {
        this.lineItems.add(lineItem);
    }
}