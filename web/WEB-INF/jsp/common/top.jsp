<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>MyPetStore</title>
    <link rel="stylesheet" href="css/mypetstore.css" type="text/css" media="all">
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
</head>

<body>
<div id="Header">
    <div id="Logo">
        <div id="LogoContent">
            <a href="mainForm"><img src="images/logo-topbar.gif" /></a>
        </div>
    </div>

    <div id="Menu">
        <div id="MenuContent">
            <a href="cartForm"><img align="middle" name="img_cart" src="images/cart.gif" /></a>
            <img align="middle" src="images/separator.gif" />

            <%
                // 从Session中获取用户对象，key为loginAccount
                Object loginAccount = session.getAttribute("loginAccount");
                if (loginAccount == null) {  // 未登录
            %>
            <a href="signOnForm">Sign In</a>
            <img align="middle" src="images/separator.gif" />
            <%
            } else {  // 已登录
            %>
            <a href="signOut">Sign Out</a>
            <img align="middle" src="images/separator.gif" />
            <a href="${pageContext.request.contextPath}/updateAccount">My Account</a>
            <img align="middle" src="images/separator.gif" />
            <!-- 新增：我的订单入口 -->
            <a href="${pageContext.request.contextPath}/viewOrders">My Orders</a>
            <img align="middle" src="images/separator.gif" />
            <%
                }
            %>

            <a href="help.html">?</a>
        </div>
    </div>

    <!-- 核心修改：设置表单action为SearchServlet的映射路径，方法改为get -->
    <div id="Search">
        <div id="SearchContent">
            <form action="${pageContext.request.contextPath}/search" method="get">
                <input type="text" name="keyword" size="14" id="keyword" autocomplete="false" placeholder="Search categories/products/items">
                <input type="submit" value="Search">
            </form>
            <div id="productAutoComplete">
                <ul id="productAutoList">
<%--                    <li class="productAutoItem" data-productId="aa">Labrador Retriever</li>
                    <li class="productAutoItem">Labrador Retriever</li>
                    <li class="productAutoItem">Labrador Retriever</li>
                    <li class="productAutoItem">Labrador Retriever</li>
                    <li class="productAutoItem">Labrador Retriever</li>--%>
                </ul>
            </div>
        </div>
    </div>

    <div id="QuickLinks">
        <a href="categoryForm?categoryId=FISH"><img src="images/sm_fish.gif" /></a>
        <img src="images/separator.gif" />
        <a href="categoryForm?categoryId=DOGS"><img src="images/sm_dogs.gif" /></a>
        <img src="images/separator.gif" />
        <a href="categoryForm?categoryId=REPTILES"><img src="images/sm_reptiles.gif" /></a>
        <img src="images/separator.gif" />
        <a href="categoryForm?categoryId=CATS"><img src="images/sm_cats.gif" /></a>
        <img src="images/separator.gif" />
        <a href="categoryForm?categoryId=BIRDS"><img src="images/sm_birds.gif" /></a>
    </div>
</div>

<div id="Content">