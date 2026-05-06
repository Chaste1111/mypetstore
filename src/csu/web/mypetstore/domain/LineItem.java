package csu.web.mypetstore.domain;

import java.math.BigDecimal;

public class LineItem {
    private int lineNumber;
    private Item item;
    private int quantity;
    private BigDecimal unitPrice;

    // Getter 和 Setter 方法
    public int getLineNumber() { return lineNumber; }
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    // 计算单项总价
    public BigDecimal getTotal() {
        return unitPrice.multiply(new BigDecimal(quantity));
    }
}