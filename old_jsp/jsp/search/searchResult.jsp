<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="csu.web.mypetstore.domain.Category" %>
<%@ page import="csu.web.mypetstore.domain.Product" %>
<%@ page import="csu.web.mypetstore.domain.Item" %>
<%@ include file="../common/top.jsp"%>

<div id="Content">
    <%-- 显示搜索关键词 --%>
    <h2>Search Results for: <%= request.getParameter("keyword") == null ? "" : request.getParameter("keyword") %></h2>

    <%
        // 从请求域获取三类搜索结果
        List<Category> categories = (List<Category>) request.getAttribute("categories");
        List<Product> products = (List<Product>) request.getAttribute("products");
        List<Item> items = (List<Item>) request.getAttribute("items");
    %>

    <%-- 1. 展示分类结果 --%>
    <% if (categories != null && !categories.isEmpty()) { %>
    <h3>Categories</h3>
    <table style="margin: 0 auto; border-collapse: collapse; width: 80%;" border="1">
        <tr style="background-color: #e2e2e2;">
            <th style="padding: 8px; text-align: center;">Category ID</th>
            <th style="padding: 8px; text-align: center;">Name</th>
            <th style="padding: 8px; text-align: center;">Description</th>
        </tr>
        <% for (Category c : categories) { %>
        <tr>
            <td style="padding: 8px; text-align: center;"><%= c.getCategoryId() %></td>
            <td style="padding: 8px; text-align: center;">
                <a href="<%= request.getContextPath() %>/categoryForm?categoryId=<%= c.getCategoryId() %>">
                    <%= c.getName() %>
                </a>
            </td>
            <td style="padding: 8px; text-align: center;"><%= c.getDescription() %></td>
        </tr>
        <% } %>
    </table>
    <% } %>

    <%-- 2. 展示商品结果（复用你原有Product搜索逻辑） --%>
    <% if (products != null && !products.isEmpty()) { %>
    <h3 style="margin-top: 20px;">Products</h3>
    <table style="margin: 0 auto; border-collapse: collapse; width: 80%;" border="1">
        <tr style="background-color: #e2e2e2;">
            <th style="padding: 8px; text-align: center;">Product ID</th>
            <th style="padding: 8px; text-align: center;">Name</th>
            <th style="padding: 8px; text-align: center;">Description</th>
        </tr>
        <% for (Product p : products) { %>
        <tr>
            <td style="padding: 8px; text-align: center;"><%= p.getProductId() %></td>
            <td style="padding: 8px; text-align: center;">
                <a href="<%= request.getContextPath() %>/productForm?productId=<%= p.getProductId() %>">
                    <%= p.getName() %>
                </a>
            </td>
            <td style="padding: 8px; text-align: center;"><%= p.getDescription() %></td>
        </tr>
        <% } %>
    </table>
    <% } %>

    <%-- 3. 展示库存项结果 --%>
    <% if (items != null && !items.isEmpty()) { %>
    <h3 style="margin-top: 20px;">Items</h3>
    <table style="margin: 0 auto; border-collapse: collapse; width: 80%;" border="1">
        <tr style="background-color: #e2e2e2;">
            <th style="padding: 8px; text-align: center;">Item ID</th>
            <th style="padding: 8px; text-align: center;">Product Name</th>
            <th style="padding: 8px; text-align: center;">Unit Price</th>
        </tr>
        <% for (Item i : items) { %>
        <tr>
            <td style="padding: 8px; text-align: center;"><%= i.getItemId() %></td>
            <td style="padding: 8px; text-align: center;">
                <a href="<%= request.getContextPath() %>/itemForm?itemId=<%= i.getItemId() %>">
                    <%= i.getProduct().getName() %>
                </a>
            </td>
            <td style="padding: 8px; text-align: center;">$<%= i.getListPrice() %></td>
        </tr>
        <% } %>
    </table>
    <% } %>

    <%-- 无结果提示 --%>
    <% if ((categories == null || categories.isEmpty())
            && (products == null || products.isEmpty())
            && (items == null || items.isEmpty())) { %>
    <p style="text-align: center; margin: 30px; font-size: 1.2em;">
        No results found. Try another keyword!
    </p>
    <% } %>
</div>

<%@ include file="../common/bottom.jsp"%>