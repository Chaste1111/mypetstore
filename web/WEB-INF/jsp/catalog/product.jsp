<%@ page import="java.util.List" %>
<%@ page import="csu.web.mypetstore.domain.Product" %>
<%@ page import="csu.web.mypetstore.domain.Item" %>
<%@ page import="csu.web.mypetstore.domain.Category" %>
<%@ include file="../common/top.jsp"%>

<div id="BackLink">
    <%
        Category categoryProduct = (Category) session.getAttribute("category");
        if (categoryProduct != null) {
    %>
    <a href="categoryForm?categoryId=<%=categoryProduct.getCategoryId()%>">Return to <%=categoryProduct.getName()%></a>
    <%
        }
    %>
</div>

<div id="Catalog">
    <%
        Product product = (Product) session.getAttribute("product");
        List<Item> itemList = (List<Item>) session.getAttribute("itemList");
    %>
    <h2><%= product != null ? product.getName() : "Product Not Found" %></h2>

    <table>
        <tr>
            <th>Item ID</th>
            <th>Product ID</th>
            <th>Description</th>
            <th>List Price</th>
            <th>&nbsp;</th>
        </tr>
        <%
            if (itemList != null && !itemList.isEmpty()) {
                for (Item item : itemList) {
                    Product itemProduct = item.getProduct();
        %>
        <tr>
            <td><a href="itemForm?itemId=<%=item.getItemId()%>&productId=<%=itemProduct.getProductId()%>"><%=item.getItemId()%></a></td>
            <td><%=itemProduct.getProductId()%></td>
            <td> <%=item.getAttribute1()%>
                <%=item.getAttribute2() != null ? " " + item.getAttribute2() : ""%>
                <%=item.getAttribute3() != null ? " " + item.getAttribute3() : ""%>
                <%=item.getAttribute4() != null ? " " + item.getAttribute4() : ""%>
                <%=item.getAttribute5() != null ? " " + item.getAttribute5() : ""%>
                <%=product != null ? product.getName() : ""%>
            </td>
            <td><%=String.format("$%.2f", item.getListPrice())%></td>
            <td><a href="addItemToCart?workingItemId=<%=item.getItemId()%>" class="Button">Add to Cart</a></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</div>

<%@ include file="../common/bottom.jsp"%>