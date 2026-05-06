<%@ page import="csu.web.mypetstore.domain.Product" %>

<%@ page import="java.util.List" %>
<%@ include file="../common/top.jsp"%>
<div id="BackLink">
    <a href="mainForm">Return to Main Menu</a>
</div>

<div id="Catalog">
    <h2>${sessionScope.category.name}</h2>

    <table>
        <tr>
            <th>Product ID</th>
            <th>Name</th>
        </tr>
        <%
            List<Product> productList = (List<Product>) session.getAttribute("productList");
            if (productList != null) {
                for (Product product : productList) {
        %>
        <tr>
            <td><a href="productForm?productId=<%=product.getProductId()%>"><%=product.getProductId()%></a></td>
            <td><%=product.getName()%></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</div>

<%@ include file="../common/bottom.jsp"%>