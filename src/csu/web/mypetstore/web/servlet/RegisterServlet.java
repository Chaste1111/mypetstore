package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.service.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    private static final String REGISTER_FORM = "/WEB-INF/jsp/account/register.jsp";
    private static final String SIGN_ON_FORM = "/WEB-INF/jsp/account/signon.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 跳转到注册页面
        req.getRequestDispatcher(REGISTER_FORM).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Account account = new Account();
        // 从表单获取数据
        account.setUsername(req.getParameter("username"));
        account.setPassword(req.getParameter("password"));
        account.setEmail(req.getParameter("email"));
        account.setFirstName(req.getParameter("firstName"));
        account.setLastName(req.getParameter("lastName"));
        account.setAddress1(req.getParameter("address1"));
        account.setAddress2(req.getParameter("address2"));
        account.setCity(req.getParameter("city"));
        account.setState(req.getParameter("state"));
        account.setZip(req.getParameter("zip"));
        account.setCountry(req.getParameter("country"));
        account.setPhone(req.getParameter("phone"));
        // 设置默认值
        account.setStatus("OK");
        account.setLanguagePreference("en");
        account.setFavoriteCategoryId("DOGS");
        account.setListOption(true);
        account.setBannerOption(true);

        // 数据验证
        String msg = validate(account);
        if (msg != null) {
            req.setAttribute("registerMsg", msg);
            req.getRequestDispatcher(REGISTER_FORM).forward(req, resp);
            return;
        }

        // 调用服务层注册
        AccountService service = new AccountService();
        try {
            service.registerAccount(account);
            // 注册成功，跳转到登录页并提示
            req.setAttribute("signOnMsg", "注册成功，请登录");
            req.getRequestDispatcher(SIGN_ON_FORM).forward(req, resp);
        } catch (RuntimeException e) {
            req.setAttribute("registerMsg", e.getMessage());
            req.getRequestDispatcher(REGISTER_FORM).forward(req, resp);
        }
    }

    // 表单验证
    private String validate(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return "用户名不能为空";
        }
        if (account.getPassword() == null || account.getPassword().trim().isEmpty()) {
            return "密码不能为空";
        }
        if (account.getEmail() == null || account.getEmail().trim().isEmpty()) {
            return "邮箱不能为空";
        }
        if (account.getFirstName() == null || account.getFirstName().trim().isEmpty()) {
            return "名字不能为空";
        }
        if (account.getLastName() == null || account.getLastName().trim().isEmpty()) {
            return "姓氏不能为空";
        }
        return null;
    }
}