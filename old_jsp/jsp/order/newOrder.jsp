<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="csu.web.mypetstore.domain.Account" %>
<%@ page import="csu.web.mypetstore.domain.Cart" %>
<%@ page import="csu.web.mypetstore.domain.CartItem" %>
<%@ page import="java.util.List" %>
<%@ page import="csu.web.mypetstore.domain.Item" %>
<%@ include file="../common/top.jsp"%>

<%
    // 修改变量名，避免重复定义
    HttpSession sess = request.getSession();
    Account accountFromSession = (Account) sess.getAttribute("loginAccount");
    Cart cart = (Cart) sess.getAttribute("cart");
    List<CartItem> cartItemList = null;
    if (cart != null) {
        cartItemList = cart.getCartItemList();
    }
    String checkoutMsg = (String) request.getAttribute("checkoutMsg");
%>

<div id="Catalog">
    <h2>Proceed to Checkout</h2>

    <% if (checkoutMsg != null && !checkoutMsg.isEmpty()) { %>
    <p style="color: <%= checkoutMsg.contains("success") ? "green" : "red" %>; text-align: center;">
        <%= checkoutMsg %>
    </p>
    <% } %>

    <form action="<%= request.getContextPath() %>/submitOrder" method="post">
        <h3>Order Summary</h3>
        <table style="margin: 0 auto; border-collapse: collapse; width: 80%;" border="1">
            <tr style="background-color: #e2e2e2;">
                <th style="padding: 8px; text-align: center;">Item ID</th>
                <th style="padding: 8px; text-align: center;">Product Name</th>
                <th style="padding: 8px; text-align: center;">Quantity</th>
                <th style="padding: 8px; text-align: center;">Unit Price</th>
                <th style="padding: 8px; text-align: center;">Subtotal</th>
            </tr>
            <% if (cartItemList != null && !cartItemList.isEmpty()) { %>
            <% for (CartItem cartItem : cartItemList) { %>
            <% Item item = cartItem.getItem(); %>
            <tr>
                <td style="padding: 8px; text-align: center;"><%= item.getItemId() %></td>
                <td style="padding: 8px; text-align: center;"><%= item.getProduct().getName() %></td>
                <td style="padding: 8px; text-align: center;"><%= cartItem.getQuantity() %></td>
                <td style="padding: 8px; text-align: center;">$<%= item.getListPrice() %></td>
                <td style="padding: 8px; text-align: center;">$<%= cartItem.getTotal() %></td>
                <input type="hidden" name="itemIdList" value="<%= item.getItemId() %>">
                <input type="hidden" name="quantityList" value="<%= cartItem.getQuantity() %>">
            </tr>
            <% } %>
            <tr style="background-color: #f5f5f5;">
                <td colspan="4" align="right" style="padding: 8px; font-weight: bold;">Total Amount:</td>
                <td style="padding: 8px; text-align: center; font-weight: bold;">$<%= cart.getSubTotal() %></td>
            </tr>
            <% } else { %>
            <tr>
                <td colspan="5" align="center" style="padding: 15px;">
                    Your cart is empty! <a href="<%= request.getContextPath() %>/mainForm">Continue Shopping</a>
                </td>
            </tr>
            <% } %>
        </table>

        <h3 style="margin-top: 30px;">Shipping Information</h3>
        <table style="margin: 20px auto; border-collapse: collapse; width: 60%;" border="1">
            <% if (accountFromSession != null) { %>
            <tr>
                <td style="padding: 8px; width: 30%;">First Name:</td>
                <td style="padding: 8px;">
                    <input type="text" name="shippingFirstName"
                           value="<%= accountFromSession.getFirstName() != null ? accountFromSession.getFirstName() : "" %>"
                           required style="width: 80%; padding: 5px;">
                </td>
            </tr>
            <tr>
                <td style="padding: 8px;">Last Name:</td>
                <td style="padding: 8px;">
                    <input type="text" name="shippingLastName"
                           value="<%= accountFromSession.getLastName() != null ? accountFromSession.getLastName() : "" %>"
                           required style="width: 80%; padding: 5px;">
                </td>
            </tr>
            <tr>
                <td style="padding: 8px;">Address 1:</td>
                <td style="padding: 8px;">
                    <input type="text" name="shippingAddress1"
                           value="<%= accountFromSession.getAddress1() != null ? accountFromSession.getAddress1() : "" %>"
                           required style="width: 80%; padding: 5px;">
                </td>
            </tr>
            <tr>
                <td style="padding: 8px;">Address 2:</td>
                <td style="padding: 8px;">
                    <input type="text" name="shippingAddress2"
                           value="<%= accountFromSession.getAddress2() != null ? accountFromSession.getAddress2() : "" %>"
                           style="width: 80%; padding: 5px;">
                </td>
            </tr>
            <tr>
                <td style="padding: 8px;">City:</td>
                <td style="padding: 8px;">
                    <input type="text" name="shippingCity"
                           value="<%= accountFromSession.getCity() != null ? accountFromSession.getCity() : "" %>"
                           required style="width: 80%; padding: 5px;">
                </td>
            </tr>
            <tr>
                <td style="padding: 8px;">State:</td>
                <td style="padding: 8px;">
                    <input type="text" name="shippingState"
                           value="<%= accountFromSession.getState() != null ? accountFromSession.getState() : "" %>"
                           required style="width: 80%; padding: 5px;">
                </td>
            </tr>
            <tr>
                <td style="padding: 8px;">Zip Code:</td>
                <td style="padding: 8px;">
                    <input type="text" name="shippingZip"
                           value="<%= accountFromSession.getZip() != null ? accountFromSession.getZip() : "" %>"
                           required style="width: 80%; padding: 5px;">
                </td>
            </tr>
            <tr>
                <td style="padding: 8px;">Country:</td>
                <td style="padding: 8px;">
                    <input type="text" name="shippingCountry"
                           value="<%= accountFromSession.getCountry() != null ? accountFromSession.getCountry() : "" %>"
                           required style="width: 80%; padding: 5px;">
                </td>
            </tr>
            <tr>
                <td style="padding: 8px;">Phone:</td>
                <td style="padding: 8px;">
                    <input type="text" name="shippingPhone"
                           value="<%= accountFromSession.getPhone() != null ? accountFromSession.getPhone() : "" %>"
                           required style="width: 80%; padding: 5px;">
                </td>
            </tr>
            <% } else { %>
            <tr>
                <td colspan="2" align="center" style="padding: 15px;">
                    Please <a href="<%= request.getContextPath() %>/signOnForm">log in</a> to proceed with checkout.
                </td>
            </tr>
            <% } %>
        </table>

        <h3 style="margin-top: 30px;">Payment Method</h3>
        <table style="margin: 0 auto; border-collapse: collapse; width: 60%;" border="1">
            <tr>
                <td style="padding: 8px; width: 30%;">Payment Type:</td>
                <td style="padding: 8px;">
                    <select name="paymentType" required style="width: 82%; padding: 5px;">
                        <option value="Credit Card">Credit Card</option>
                        <option value="PayPal">PayPal</option>
                        <option value="Debit Card">Debit Card</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="padding: 8px;">Card Number (if applicable):</td>
                <td style="padding: 8px;">
                    <input type="text" name="cardNumber" placeholder="e.g. 4111-1111-1111-1111"
                           style="width: 80%; padding: 5px;">
                </td>
            </tr>
        </table>

        <div style="text-align: center; margin: 30px 0;">
            <% if (accountFromSession != null && cartItemList != null && !cartItemList.isEmpty()) { %>
            <input type="submit" value="Place Order" class="Button" style="padding: 8px 20px;">
            <a href="<%= request.getContextPath() %>/cartForm" class="Button" style="margin-left: 20px; padding: 8px 20px;">Cancel</a>
            <% } else { %>
            <a href="<%= request.getContextPath() %>/mainForm" class="Button" style="padding: 8px 20px;">Continue Shopping</a>
            <% } %>
        </div>
    </form>
</div>

<%@ include file="../common/bottom.jsp"%>