<%@ page import="java.util.Iterator" %>
<%@ page import="csu.web.mypetstore.domain.Cart" %>
<%@ page import="csu.web.mypetstore.domain.CartItem" %>
<%@ page import="csu.web.mypetstore.domain.Item" %>
<%@ page import="csu.web.mypetstore.domain.Product" %>
<%@ include file="../common/top.jsp"%>

<div id="BackLink">
    <a href="mainForm">Return to Main Menu</a>
</div>

<div id="Catalog">
    <div id="Cart">
        <h2>Shopping Cart</h2>
        <table>
            <tr>
                <th><b>Item ID</b></th>
                <th><b>Product ID</b></th>
                <th><b>Description</b></th>
                <th><b>In Stock?</b></th>
                <th><b>Quantity</b></th>
                <th><b>List Price</b></th>
                <th><b>Total Cost</b></th>
                <th>&nbsp;</th>
            </tr>

            <%
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                }

                if (cart.getNumberOfItems() == 0) {
            %>
            <tr>
                <td colspan="8"><b>Your cart is empty.</b></td>
            </tr>
            <%
            } else {
                Iterator<CartItem> cartItemIterator = cart.getAllCartItems();
                while (cartItemIterator.hasNext()) {
                    CartItem cartItem = cartItemIterator.next();
                    Item item = cartItem.getItem();
                    Product product = item.getProduct();
            %>
            <tr id="row_<%=item.getItemId()%>">  <!-- 为每行添加唯一ID -->
                <td>
                    <a href="itemForm?itemId=<%=item.getItemId()%>"><%=item.getItemId()%></a>
                </td>
                <td><%=product.getProductId()%></td>
                <td>
                    <%=item.getAttribute1()%>
                    <%=item.getAttribute2() != null ? " " + item.getAttribute2() : ""%>
                    <%=item.getAttribute3() != null ? " " + item.getAttribute3() : ""%>
                    <%=item.getAttribute4() != null ? " " + item.getAttribute4() : ""%>
                    <%=item.getAttribute5() != null ? " " + item.getAttribute5() : ""%>
                    <%=product.getName()%>
                </td>
                <td><%=cartItem.isInStock() ? "Yes" : "No"%></td>
                <td>
                    <!-- 修改数量输入框：添加ID和事件触发 -->
                    <input type="number"
                           id="quantity_<%=item.getItemId()%>"
                           value="<%=cartItem.getQuantity()%>"
                           min="0"
                           onblur="updateQuantity('<%=item.getItemId()%>')"
                           onkeydown="if(event.key === 'Enter') { updateQuantity('<%=item.getItemId()%>'); return false; }">
                </td>
                <td><%=String.format("$%.2f", item.getListPrice())%></td>
                <td id="total_<%=item.getItemId()%>"><%=String.format("$%.2f", cartItem.getTotal())%></td>  <!-- 添加单项总价ID -->
                <td>
                    <a href="removeCartItem?itemId=<%=item.getItemId()%>" class="Button">Remove</a>
                </td>
            </tr>
            <%
                    }
                }
            %>

            <tr>
                <td colspan="7">
                    <!-- 修改小计显示：添加ID用于实时更新 -->
                    Sub Total: <span id="subTotal"><%=String.format("$%.2f", cart.getSubTotal())%></span>
                    <!-- 移除原有的提交按钮 -->
                </td>
                <td>&nbsp;</td>
            </tr>
        </table>

        <%
            if (cart.getNumberOfItems() > 0) {
        %>
        <a href="newOrderForm" class="Button">Proceed to Checkout</a>
        <%
            }
        %>
    </div>

    <div id="MyList">
        <c:if test="${sessionScope.loginAccount != null}">
            <c:if test="${!empty sessionScope.loginAccount.listOption}">
                <%@ include file="includeMyList.jsp"%>
            </c:if>
        </c:if>
    </div>

    <div id="Separator">&nbsp;</div>
</div>

<!-- 添加AJAX处理脚本 -->
<script>
    // 发送AJAX请求更新购物车数量
    function updateQuantity(itemId) {
        // 获取输入框元素和数量值
        const input = document.getElementById("quantity_" + itemId);
        const quantity = parseInt(input.value.trim());
        const row = document.getElementById("row_" + itemId);
        const totalCell = document.getElementById("total_" + itemId);

        // 输入验证
        if (isNaN(quantity) || quantity < 0) {
            alert("Please enter a valid quantity (0 or more)");
            input.value = 1; // 重置为默认值
            return;
        }

        // 创建AJAX请求
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "${pageContext.request.contextPath}/updateCartAjax", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        // 处理响应
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    try {
                        const response = JSON.parse(xhr.responseText);
                        if (response.success) {
                            // 更新页面显示的小计
                            document.getElementById("subTotal").textContent = "$" + response.subTotal.toFixed(2);

                            // 如果数量为0，移除当前行
                            if (quantity === 0) {
                                row.remove();
                            } else {
                                // 关键修改：使用后端返回的itemTotal更新单个商品总价
                                totalCell.textContent = "$" + response.itemTotal.toFixed(2);
                            }
                        }
                        //alert(response.message); // 显示操作结果
                    } catch (e) {
                        alert("Error parsing response: " + e.message);
                    }
                } else {
                    alert("Failed to update cart. Please try again.");
                }
            }
        };

        // 发送请求参数
        xhr.send("itemId=" + encodeURIComponent(itemId) + "&quantity=" + quantity);
    }
</script>


<%@ include file="../common/bottom.jsp"%>