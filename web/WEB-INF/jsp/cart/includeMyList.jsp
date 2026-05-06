<%
  // 获取session中的myList对象
  Object myListObj = session.getAttribute("myList");
  // 判断myList不为空且是一个集合（假设myList是List<Product>类型）
  if (myListObj != null && myListObj instanceof java.util.List) {
    java.util.List<?> myList = (java.util.List<?>) myListObj;
    if (!myList.isEmpty()) {  // 对应${!empty sessionScope.myList}
%>
<p>Pet Favorites <br />
  Shop for more of your favorite pets here.</p>
<ul>
  <%
    // 遍历集合（假设元素是Product类型）
    for (Object item : myList) {
      // 强制转换为Product（确保实际类型匹配）
      csu.web.mypetstore.domain.Product product = (csu.web.mypetstore.domain.Product) item;
  %>
  <li>
    <a href="productForm?productId=<%=product.getProductId()%>"><%=product.getName()%></a>
    (<%=product.getProductId()%>)
  </li>
  <%
    }
  %>
</ul>
<%
    }
  }
%>