<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/top.jsp"%>

<%
    // 从请求域获取账户信息和提示信息
    csu.web.mypetstore.domain.Account account = (csu.web.mypetstore.domain.Account) request.getAttribute("account");
    String updateMsg = (String) request.getAttribute("updateMsg");
%>

<div id="Catalog">
    <h2>Update Your Profile</h2>

    <% if (updateMsg != null) { %>
    <p><font color="<%= updateMsg.contains("successfully") ? "green" : "red" %>"><%= updateMsg %></font></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/updateAccount" method="post">
        <table>
            <!-- 基本信息 -->
            <tr><td colspan="2"><h3>Basic Information</h3></td></tr>
            <tr>
                <td>Username:</td>
                <td>
                    <input type="text" value="<%= account.getUsername() %>" disabled>
                    <input type="hidden" name="username" value="<%= account.getUsername() %>">
                </td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><input type="email" name="email" value="<%= account.getEmail() %>" required></td>
            </tr>
            <tr>
                <td>First Name:</td>
                <td><input type="text" name="firstName" value="<%= account.getFirstName() %>" required></td>
            </tr>
            <tr>
                <td>Last Name:</td>
                <td><input type="text" name="lastName" value="<%= account.getLastName() %>" required></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><input type="text" name="phone" value="<%= account.getPhone() %>"></td>
            </tr>

            <!-- 地址信息 -->
            <tr><td colspan="2"><h3>Address</h3></td></tr>
            <tr>
                <td>Address 1:</td>
                <td><input type="text" name="address1" value="<%= account.getAddress1() %>"></td>
            </tr>
            <tr>
                <td>Address 2:</td>
                <td><input type="text" name="address2" value="<%= account.getAddress2() %>"></td>
            </tr>
            <tr>
                <td>City:</td>
                <td><input type="text" name="city" value="<%= account.getCity() %>"></td>
            </tr>
            <tr>
                <td>State:</td>
                <td><input type="text" name="state" value="<%= account.getState() %>"></td>
            </tr>
            <tr>
                <td>Zip:</td>
                <td><input type="text" name="zip" value="<%= account.getZip() %>"></td>
            </tr>
            <tr>
                <td>Country:</td>
                <td><input type="text" name="country" value="<%= account.getCountry() %>"></td>
            </tr>

            <!-- 偏好设置 -->
            <tr><td colspan="2"><h3>Preferences</h3></td></tr>
            <tr>
                <td>Language Preference:</td>
                <td>
                    <select name="languagePreference">
                        <option value="en" <%= account.getLanguagePreference().equals("en") ? "selected" : "" %>>English</option>
                        <option value="zh" <%= account.getLanguagePreference().equals("zh") ? "selected" : "" %>>Chinese</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Favorite Category:</td>
                <td>
                    <select name="favoriteCategoryId">
                        <option value="DOGS" <%= account.getFavoriteCategoryId().equals("DOGS") ? "selected" : "" %>>Dogs</option>
                        <option value="CATS" <%= account.getFavoriteCategoryId().equals("CATS") ? "selected" : "" %>>Cats</option>
                        <option value="FISH" <%= account.getFavoriteCategoryId().equals("FISH") ? "selected" : "" %>>Fish</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>My List Option:</td>
                <td><input type="checkbox" name="listOption" <%= account.isListOption() ? "checked" : "" %>></td>
            </tr>
            <tr>
                <td>Banner Option:</td>
                <td><input type="checkbox" name="bannerOption" <%= account.isBannerOption() ? "checked" : "" %>></td>
            </tr>

            <!-- 密码更新 -->
            <tr><td colspan="2"><h3>Change Password (Optional)</h3></td></tr>
            <tr>
                <td>Old Password:</td>
                <td><input type="password" name="oldPassword"></td>
            </tr>
            <tr>
                <td>New Password:</td>
                <td><input type="password" name="newPassword"></td>
            </tr>

            <tr>
                <td colspan="2">
                    <input type="submit" value="Save Changes">
                    <a href="<%= request.getContextPath() %>/main" class="Button">Cancel</a>
                </td>
            </tr>
        </table>
    </form>
</div>

<%-- 新增AJAX验证逻辑 --%>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script>
    $(function() {
        $("#editUsername").blur(function() {
            let username = $(this).val().trim();
            let currentUsername = $("#currentUsername").val();
            $("#usernameTip").html("");

            if (username === "") {
                $("#usernameTip").html("<font color='red'>Username cannot be empty!</font>");
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
                    $("#usernameTip").html("<font color='orange'>Verification failed, please try again!</font>");
                }
            });
        });
    });
</script>

<%@ include file="../common/bottom.jsp"%>