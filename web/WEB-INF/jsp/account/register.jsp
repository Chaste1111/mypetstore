<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../common/top.jsp"%>

<div id="Catalog">
  <form action="${pageContext.request.contextPath}/register" method="post">
    <p>Please fill in the form to register.</p>

    <c:if test="${requestScope.registerMsg != null}">
      <p><font color="red">${requestScope.registerMsg}</font></p>
    </c:if>

    <p>
      Username:<input type="text" name="username"><br />
      Password:<input type="password" name="password"><br />
      Email:<input type="email" name="email"><br />
      First Name:<input type="text" name="firstName"><br />
      Last Name:<input type="text" name="lastName"><br />
      Address 1:<input type="text" name="address1"><br />
      Address 2:<input type="text" name="address2"><br />
      City:<input type="text" name="city"><br />
      State:<input type="text" name="state"><br />
      Zip:<input type="text" name="zip"><br />
      Country:<input type="text" name="country" value="China"><br />
      Phone:<input type="text" name="phone"><br />
      <%-- 新增验证码部分 --%>
      Verification code:<input type="text" name="captcha" style="width: 80px; margin-right: 10px;">
      <img src="${pageContext.request.contextPath}/captcha" alt="Verification code"
           style="cursor: pointer; vertical-align: middle;"
           onclick="this.src='${pageContext.request.contextPath}/captcha?'+Math.random()">
      <br>(Click the picture to refresh)
    </p>

    <input type="submit" value="Register">
  </form>

  Already have an account?
  <a href="${pageContext.request.contextPath}/signOnForm">Login Here</a>

</div>

<%-- 新增AJAX验证逻辑 --%>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script>
  $(function() {
    // 监听用户名输入框失去焦点
    $("#username").blur(function() {
      let username = $(this).val().trim();
      $("#usernameTip").html("");

      if (username === "") {
        $("#usernameTip").html("Username cannot be empty!");
        return;
      }

      // 发送AJAX请求
      $.ajax({
        url: "${pageContext.request.contextPath}/checkUsername",
        type: "POST",
        data: {username: username},
        dataType: "json",
        success: function(result) {
          if (result.exists) {
            $("#usernameTip").html("Username already exists!");
          } else {
            $("#usernameTip").html("<font color='green'>Username is available!</font>");
          }
        },
        error: function() {
          $("#usernameTip").html("Verification failed, please try again!");
        }
      });
    });
  });
</script>

<%@ include file="../common/bottom.jsp"%>