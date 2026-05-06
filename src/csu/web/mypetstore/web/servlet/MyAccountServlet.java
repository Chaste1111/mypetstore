// MyAccountServlet.java
package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.Account;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MyAccountServlet extends HttpServlet {
    private static final String MY_ACCOUNT_FORM = "/WEB-INF/jsp/account/myAccount.jsp"; // 假设的用户中心页面

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("loginAccount");

        // 未登录则跳转登录页
        if (account == null) {
            req.setAttribute("signOnMsg", "Please log in first");
            req.getRequestDispatcher("/signOnForm").forward(req, resp);
            return;
        }

        // 携带用户信息到个人中心页面
        req.setAttribute("account", account);
        req.getRequestDispatcher(MY_ACCOUNT_FORM).forward(req, resp);
    }
}