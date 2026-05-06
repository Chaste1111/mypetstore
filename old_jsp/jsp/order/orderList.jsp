<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="csu.web.mypetstore.domain.Order" %>
<%@ page import="java.text.SimpleDateFormat" %> <%-- 导入日期格式化类 --%>
<%@ include file="../common/top.jsp"%>

<%
  // 从请求域获取订单列表
  List<Order> orders = (List<Order>) request.getAttribute("orders");
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 定义仅显示年月日的格式
%>

<div id="Content">
  <h2>My Orders</h2>
  <% if (orders == null || orders.isEmpty()) { %>
  <p style="text-align: center; margin: 20px;">You have no orders yet. <a href="<%= request.getContextPath() %>/mainForm">Start shopping</a>!</p>
  <% } else { %>
  <table style="margin: 0 auto; border-collapse: collapse; width: 80%;" border="1">
    <tr style="background-color: #e2e2e2;">
      <th style="padding: 8px; text-align: center;">Order ID</th>
      <th style="padding: 8px; text-align: center;">Order Date</th>
      <th style="padding: 8px; text-align: center;">Total Price</th>
      <th style="padding: 8px; text-align: center;">Status</th>
      <th style="padding: 8px; text-align: center;">Action</th>
    </tr>
    <% for (Order order : orders) { %>
    <tr>
      <td style="padding: 8px; text-align: center;"><%= order.getOrderId() %></td>
      <td style="padding: 8px; text-align: center;"><%= order.getOrderDate() != null ? sdf.format(order.getOrderDate()) : "N/A" %></td> <%-- 格式化日期 --%>
      <td style="padding: 8px; text-align: center;">$<%= order.getTotalPrice() %></td>
      <td style="padding: 8px; text-align: center;"><%= order.getStatus() %></td>
      <td style="padding: 8px; text-align: center;">
        <a href="<%= request.getContextPath() %>/viewOrderDetail?orderId=<%= order.getOrderId() %>">View Detail</a>
      </td>
    </tr>
    <% } %>
  </table>
  <% } %>
</div>

<%@ include file="../common/bottom.jsp"%>