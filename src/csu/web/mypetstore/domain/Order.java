package csu.web.mypetstore.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private String orderId;
    private String username;
    private Date orderDate;
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String shippingCountry;
    private String billingAddress1;
    private String billingAddress2;
    private String billingCity;
    private String billingState;
    private String billingZip;
    private String billingCountry;
    private BigDecimal totalPrice;
    private String paymentType;
    private String status;
    private List<LineItem> lineItems = new ArrayList<>();  // 订单关联的商品项

    // Getter 和 Setter 方法
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public String getShippingAddress1() { return shippingAddress1; }
    public void setShippingAddress1(String shippingAddress1) { this.shippingAddress1 = shippingAddress1; }

    public String getShippingAddress2() { return shippingAddress2; }
    public void setShippingAddress2(String shippingAddress2) { this.shippingAddress2 = shippingAddress2; }

    public String getShippingCity() { return shippingCity; }
    public void setShippingCity(String shippingCity) { this.shippingCity = shippingCity; }

    public String getShippingState() { return shippingState; }
    public void setShippingState(String shippingState) { this.shippingState = shippingState; }

    public String getShippingZip() { return shippingZip; }
    public void setShippingZip(String shippingZip) { this.shippingZip = shippingZip; }

    public String getShippingCountry() { return shippingCountry; }
    public void setShippingCountry(String shippingCountry) { this.shippingCountry = shippingCountry; }

    public String getBillingAddress1() { return billingAddress1; }
    public void setBillingAddress1(String billingAddress1) { this.billingAddress1 = billingAddress1; }

    public String getBillingAddress2() { return billingAddress2; }
    public void setBillingAddress2(String billingAddress2) { this.billingAddress2 = billingAddress2; }

    public String getBillingCity() { return billingCity; }
    public void setBillingCity(String billingCity) { this.billingCity = billingCity; }

    public String getBillingState() { return billingState; }
    public void setBillingState(String billingState) { this.billingState = billingState; }

    public String getBillingZip() { return billingZip; }
    public void setBillingZip(String billingZip) { this.billingZip = billingZip; }

    public String getBillingCountry() { return billingCountry; }
    public void setBillingCountry(String billingCountry) { this.billingCountry = billingCountry; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<LineItem> getLineItems() { return lineItems; }
    public void setLineItems(List<LineItem> lineItems) { this.lineItems = lineItems; }
    public void addLineItem(LineItem lineItem) { this.lineItems.add(lineItem); }
}