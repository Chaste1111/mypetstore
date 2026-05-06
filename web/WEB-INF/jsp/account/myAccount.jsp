<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="csu.web.mypetstore.domain.Account" %>
<html>
<head>
  <title>My Account</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypetstore.css" type="text/css" media="all">
  <style>
    /* 仅保留居中必要样式，不添加额外背景美化 */
    .center-container {
      width: 80%; /* 控制整体宽度 */
      margin: 0 auto; /* 水平居中 */
      padding: 20px;
    }
    .center-table {
      width: 100%;
      max-width: 800px; /* 限制表格最大宽度 */
      margin: 0 auto; /* 表格居中 */
      border-collapse: collapse; /* 合并边框（沿用项目风格） */
    }
    .center-table th, .center-table td {
      padding: 8px 12px;
      text-align: left;
      border: 1px solid #ccc; /* 基础边框（沿用项目表格风格） */
    }
    .section-header {
      text-align: center; /* 标题居中 */
      margin: 15px 0;
    }
    .btn-container {
      text-align: center; /* 按钮居中 */
      margin: 15px 0;
    }
  </style>
  <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
  <script>
    // 切换展示/编辑模式
    function toggleEditForm() {
      const viewDiv = document.getElementById("infoView");
      const editDiv = document.getElementById("infoEdit");
      viewDiv.style.display = viewDiv.style.display === "none" ? "block" : "none";
      editDiv.style.display = editDiv.style.display === "none" ? "block" : "none";
    }
    //新增用户名验证逻辑
    $(function() {
      $("#editUsername").blur(function() {
        let username = $(this).val().trim();
        let currentUsername = $("#currentUsername").val();
        $("#usernameTip").html("");

        if (username === "") {
          $("#usernameTip").html("Username cannot be empty!");
          return;
        }

        $.ajax({
          url: "${pageContext.request.contextPath}/checkUsername",
          type: "POST",
          data: {
            username: username,
            currentUsername: currentUsername
          },
          dataType: "json",
          success: function(result) {
            if (result.exists) {
              $("#usernameTip").html("<font color='red'>Username already exists!</font>");
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
</head>
<body>
<div id="Header">
  <%@ include file="../common/top.jsp" %>
</div>

<div id="Content" class="center-container">
  <h2 class="section-header">My Account Information</h2>

  <%-- 提示信息 --%>
  <%
    String updateMsg = (String) request.getAttribute("updateMsg");
    if (updateMsg != null) {
  %>
  <p style="text-align: center; color: <%= updateMsg.contains("successfully") ? "green" : "red" %>">
    <%= updateMsg %>
  </p>
  <% } %>

  <%-- 获取用户信息 --%>
  <%
    Account account = (Account) request.getAttribute("account");
    if (account == null) {
      account = (Account) session.getAttribute("loginAccount");
    }
  %>

  <%-- 信息展示区 --%>
  <div id="infoView" style="display: block;">
    <table class="center-table">
      <tr><td colspan="2" style="font-weight: bold; text-align: center;">Basic Information</td></tr>
      <tr><th>Username:</th><td><%= account.getUsername() %></td></tr>
      <tr><th>Email:</th><td><%= account.getEmail() %></td></tr>
      <tr><th>Name:</th><td><%= account.getFirstName() %> <%= account.getLastName() %></td></tr>
      <tr><th>Phone:</th><td><%= account.getPhone() %></td></tr>

      <tr><td colspan="2" style="font-weight: bold; text-align: center;">Address</td></tr>
      <tr><th>Address 1:</th><td><%= account.getAddress1() %></td></tr>
      <tr><th>Address 2:</th><td><%= account.getAddress2() != null ? account.getAddress2() : "" %></td></tr>
      <tr><th>City:</th><td><%= account.getCity() %></td></tr>
      <tr><th>State:</th><td><%= account.getState() %></td></tr>
      <tr><th>Zip:</th><td><%= account.getZip() %></td></tr>
      <tr><th>Country:</th><td><%= account.getCountry() %></td></tr>

      <tr><td colspan="2" style="font-weight: bold; text-align: center;">Preferences</td></tr>
      <tr><th>Language:</th><td><%= "en".equals(account.getLanguagePreference()) ? "English" : "Chinese" %></td></tr>
      <tr><th>Favorite Category:</th><td><%= account.getFavoriteCategoryId() %></td></tr>
      <tr><th>My List Option:</th><td><%= account.isListOption() ? "Enabled" : "Disabled" %></td></tr>
      <tr><th>Banner Option:</th><td><%= account.isBannerOption() ? "Enabled" : "Disabled" %></td></tr>
    </table>

    <div class="btn-container">
      <button onclick="toggleEditForm()">Edit Profile</button>
    </div>
  </div>

  <%-- 编辑表单区：新增用户名可输入+验证 --%>
  <div id="infoEdit" style="display: none;">
    <form action="${pageContext.request.contextPath}/updateAccount" method="post">
      <input type="hidden" id="currentUsername" value="<%= account.getUsername() %>">
      <table class="center-table">
        <tr><td colspan="2" style="font-weight: bold; text-align: center;">Basic Information</td></tr>
        <tr>
          <th>Username:</th>
          <td>
            <input type="text" id="editUsername" name="username" value="<%= account.getUsername() %>" required>
            <span id="usernameTip" style="margin-left:10px;"></span>
          </td>
        </tr>
        <tr><th>Email:</th><td><input type="email" name="email" value="<%= account.getEmail() %>" required></td></tr>
        <tr><th>First Name:</th><td><input type="text" name="firstName" value="<%= account.getFirstName() %>" required></td></tr>
        <tr><th>Last Name:</th><td><input type="text" name="lastName" value="<%= account.getLastName() %>" required></td></tr>
        <tr><th>Phone:</th><td><input type="text" name="phone" value="<%= account.getPhone() %>"></td></tr>
        <tr><td colspan="2" style="font-weight: bold; text-align: center;">Address</td></tr>
        <tr><th>Address 1:</th><td><input type="text" name="address1" value="<%= account.getAddress1() %>"></td></tr>
        <tr><th>Address 2:</th><td><input type="text" name="address2" value="<%= account.getAddress2() %>"></td></tr>
        <tr><th>City:</th><td><input type="text" name="city" value="<%= account.getCity() %>"></td></tr>
        <tr><th>State:</th><td><input type="text" name="state" value="<%= account.getState() %>"></td></tr>
        <tr><th>Zip:</th><td><input type="text" name="zip" value="<%= account.getZip() %>"></td></tr>
        <tr><th>Country:</th><td><input type="text" name="country" value="<%= account.getCountry() %>"></td></tr>
        <tr><td colspan="2" style="font-weight: bold; text-align: center;">Preferences</td></tr>
        <tr>
          <th>Language Preference:</th>
          <td>
            <select name="languagePreference">
              <option value="en" <%= "en".equals(account.getLanguagePreference()) ? "selected" : "" %>>English</option>
              <option value="zh" <%= "zh".equals(account.getLanguagePreference()) ? "selected" : "" %>>Chinese</option>
            </select>
          </td>
        </tr>
        <tr>
          <th>Favorite Category:</th>
          <td>
            <select name="favoriteCategoryId">
              <option value="DOGS" <%= "DOGS".equals(account.getFavoriteCategoryId()) ? "selected" : "" %>>Dogs</option>
              <option value="CATS" <%= "CATS".equals(account.getFavoriteCategoryId()) ? "selected" : "" %>>Cats</option>
              <option value="FISH" <%= "FISH".equals(account.getFavoriteCategoryId()) ? "selected" : "" %>>Fish</option>
              <option value="REPTILES" <%= "REPTILES".equals(account.getFavoriteCategoryId()) ? "selected" : "" %>>Reptiles</option>
              <option value="BIRDS" <%= "BIRDS".equals(account.getFavoriteCategoryId()) ? "selected" : "" %>>Birds</option>
            </select>
          </td>
        </tr>
        <tr><th>My List Option:</th><td><input type="checkbox" name="listOption" <%= account.isListOption() ? "checked" : "" %>></td></tr>
        <tr><th>Banner Option:</th><td><input type="checkbox" name="bannerOption" <%= account.isBannerOption() ? "checked" : "" %>></td></tr>
        <tr><td colspan="2" style="font-weight: bold; text-align: center;">Change Password (Optional)</td></tr>
        <tr><th>Old Password:</th><td><input type="password" name="oldPassword"></td></tr>
        <tr><th>New Password:</th><td><input type="password" name="newPassword"></td></tr>
        <tr>
          <td colspan="2" class="btn-container">
            <input type="submit" value="Save Changes">
            <button type="button" onclick="toggleEditForm()">Cancel</button>
          </td>
        </tr>
      </table>
    </form>
  </div>
</div>

<div id="Footer">
  <%@ include file="../common/bottom.jsp" %>
</div>
</body>
</html>