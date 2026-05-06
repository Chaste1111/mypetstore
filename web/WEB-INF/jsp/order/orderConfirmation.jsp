<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="csu.web.mypetstore.domain.Order" %>
<%@ page import="csu.web.mypetstore.domain.LineItem" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ include file="../common/top.jsp"%>

<%
  // 从请求域获取订单对象和提示信息
  Order order = (Order) request.getAttribute("order");
  String checkoutMsg = (String) request.getAttribute("checkoutMsg");
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>

<div id="Content">
  <h2>Order Confirmation</h2>

  <%-- 显示提交结果提示（成功/失败） --%>
  <p style="text-align: center; margin: 20px; color: green; font-weight: bold;">
    <%= checkoutMsg != null ? checkoutMsg : "Order placed successfully!" %>
  </p>

  <% if (order != null) { %>
  <%-- 订单核心信息 --%>
  <div style="margin: 20px auto; width: 80%;">
    <table style="border-collapse: collapse; width: 100%;" border="1">
      <tr style="background-color: #e2e2e2;">
        <th style="padding: 8px; text-align: center;" colspan="2">Order Summary</th>
      </tr>
      <tr>
        <td style="padding: 8px; text-align: right; width: 30%;">Order ID:</td>
        <td style="padding: 8px; text-align: left;"><%= order.getOrderId() %></td>
      </tr>
      <tr>
        <td style="padding: 8px; text-align: right;">Order Date:</td>
        <td style="padding: 8px; text-align: left;">
          <%= order.getOrderDate() != null ? sdf.format(order.getOrderDate()) : "N/A" %>
        </td>
      </tr>
      <tr>
        <td style="padding: 8px; text-align: right;">Total Price:</td>
        <td style="padding: 8px; text-align: left;">$<%= order.getTotalPrice() %></td>
      </tr>
      <tr>
        <td style="padding: 8px; text-align: right;">Payment Type:</td>
        <td style="padding: 8px; text-align: left;"><%= order.getPaymentType() %></td>
      </tr>
      <tr>
        <td style="padding: 8px; text-align: right;">Status:</td>
        <td style="padding: 8px; text-align: left;"><%= order.getStatus() %></td>
      </tr>
    </table>
  </div>

  <%-- 收货地址 --%>
  <div style="margin: 20px auto; width: 80%;">
    <table style="border-collapse: collapse; width: 100%;" border="1">
      <tr style="background-color: #e2e2e2;">
        <th style="padding: 8px; text-align: center;" colspan="2">Shipping Address</th>
      </tr>
      <tr>
        <td style="padding: 8px; text-align: right; width: 30%;">Address 1:</td>
        <td style="padding: 8px; text-align: left;"><%= order.getShippingAddress1() != null ? order.getShippingAddress1() : "" %></td>
      </tr>
      <% if (order.getShippingAddress2() != null && !order.getShippingAddress2().trim().isEmpty()) { %>
      <tr>
        <td style="padding: 8px; text-align: right;">Address 2:</td>
        <td style="padding: 8px; text-align: left;"><%= order.getShippingAddress2() %></td>
      </tr>
      <% } %>
      <tr>
        <td style="padding: 8px; text-align: right;">City/State/Zip:</td>
        <td style="padding: 8px; text-align: left;">
          <%= order.getShippingCity() != null ? order.getShippingCity() : "" %>,
          <%= order.getShippingState() != null ? order.getShippingState() : "" %>
          <%= order.getShippingZip() != null ? order.getShippingZip() : "" %>
        </td>
      </tr>
      <tr>
        <td style="padding: 8px; text-align: right;">Country:</td>
        <td style="padding: 8px; text-align: left;"><%= order.getShippingCountry() != null ? order.getShippingCountry() : "" %></td>
      </tr>
    </table>
  </div>

  <%-- 订单项列表 --%>
  <div style="margin: 20px auto; width: 80%;">
    <table style="border-collapse: collapse; width: 100%;" border="1">
      <tr style="background-color: #e2e2e2;">
        <th style="padding: 8px; text-align: center;">Item ID</th>
        <th style="padding: 8px; text-align: center;">Product Name</th>
        <th style="padding: 8px; text-align: center;">Quantity</th>
        <th style="padding: 8px; text-align: center;">Unit Price</th>
        <th style="padding: 8px; text-align: center;">Total</th>
      </tr>
      <% List<LineItem> lineItems = order.getLineItems(); %>
      <% if (lineItems != null && !lineItems.isEmpty()) { %>
      <% for (LineItem item : lineItems) { %>
      <tr>
        <td style="padding: 8px; text-align: center;"><%= item.getItem() != null ? item.getItem().getItemId() : "N/A" %></td>
        <td style="padding: 8px; text-align: center;">
          <% if (item.getItem() != null && item.getItem().getProduct() != null) { %>
          <%= item.getItem().getProduct().getName() %>
          <% } else { %>
          N/A
          <% } %>
        </td>
        <td style="padding: 8px; text-align: center;"><%= item.getQuantity() %></td>
        <td style="padding: 8px; text-align: center;">$<%= item.getUnitPrice() != null ? item.getUnitPrice() : "0.00" %></td>
        <td style="padding: 8px; text-align: center;">$<%= item.getTotal() != null ? item.getTotal() : "0.00" %></td>
      </tr>
      <% } %>
      <% } else { %>
      <tr>
        <td colspan="5" style="padding: 8px; text-align: center;">No items in this order</td>
      </tr>
      <% } %>
    </table>
  </div>

  <%-- 操作按钮（匹配系统样式） --%>
  <div style="text-align: center; margin: 30px;">
    <a href="<%= request.getContextPath() %>/viewOrders">View My Orders</a>
    &nbsp;&nbsp;&nbsp;&nbsp;
    <a href="<%= request.getContextPath() %>/mainForm">Continue Shopping</a>
  </div>
  <% } %>
</div>

<%@ include file="../common/bottom.jsp"%>