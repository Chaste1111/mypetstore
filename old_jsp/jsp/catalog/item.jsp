<%@ page import="csu.web.mypetstore.domain.Product" %>
<%@ page import="csu.web.mypetstore.domain.Item" %>
<%@ include file="../common/top.jsp"%>

<div id="BackLink">
    <%
        Product product = (Product) session.getAttribute("product");
        if (product != null) {
    %>
    <a href="productForm?productId=<%=product.getProductId()%>">
        Return to <%=product.getProductId()%>
    </a>
    <%
        }
    %>
</div>

<div id="Catalog">
    <%
        Product currentProduct = (Product) session.getAttribute("product");
        Item item = (Item) session.getAttribute("item");
    %>

    <table>
        <% if (currentProduct != null) { %>
        <tr>
            <td><%=currentProduct.getDescription()%></td>
        </tr>
        <% } %>

        <% if (item != null) { %>
        <tr>
            <td><b><%=item.getItemId()%></b></td>
        </tr>
        <tr>
            <td><b><font size="4">
                <%=item.getAttribute1()%>
                <%=item.getAttribute2() != null ? " " + item.getAttribute2() : ""%>
                <%=item.getAttribute3() != null ? " " + item.getAttribute3() : ""%>
                <%=item.getAttribute4() != null ? " " + item.getAttribute4() : ""%>
                <%=item.getAttribute5() != null ? " " + item.getAttribute5() : ""%>
                <%=currentProduct != null ? currentProduct.getName() : ""%>
            </font></b></td>
        </tr>
        <% } %>

        <% if (currentProduct != null) { %>
        <tr>
            <td><%=currentProduct.getName()%></td>
        </tr>
        <% } %>

        <% if (item != null) { %>
        <tr>
            <td>
                <% if (item.getQuantity() <= 0) { %>
                Back ordered.
                <% } else { %>
                <%=item.getQuantity()%> in stock.
                <% } %>
            </td>
        </tr>
        <tr>
            <td><%=String.format("$%.2f", item.getListPrice())%></td>
        </tr>
        <tr>
            <td>
                <a href="addItemToCart?workingItemId=<%=item.getItemId()%>" class="Button">
                    Add to Cart
                </a>
            </td>
        </tr>
        <% } %>
    </table>
</div>

<%@ include file="../common/bottom.jsp"%>