<%@ include file="../common/top.jsp"%>

<div id="Catalog">
  <form action="signOn" method="post">
    <p>Please enter your username and password.</p>
    <c:if test="${requestScope.signOnMsg != null}">
      <p><font color="red">${requestScope.signOnMsg}</font></p>
    </c:if>
    <p>
      Username:<input type="text" name="username"><br />
      Password:<input type="password" name="password"><br />
      <%-- 新增验证码部分 --%>
      Verification code:<input type="text" name="captcha" style="width: 80px; margin-right: 10px;">
      <img src="${pageContext.request.contextPath}/captcha" alt="Verification code"
           style="cursor: pointer; vertical-align: middle;"
           onclick="this.src='${pageContext.request.contextPath}/captcha?'+Math.random()">
      <br>(Click the picture to refresh)
    </p>
    <input type="submit" value="Login">
  </form>
  Need a username and password?
  <a href="register">Register Now!</a>

</div>

<%@ include file="../common/bottom.jsp"%>