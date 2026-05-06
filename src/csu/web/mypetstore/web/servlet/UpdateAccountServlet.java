package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.persistence.AccountDao;
import csu.web.mypetstore.persistence.Impl.AccountDaoImpl;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UpdateAccountServlet extends HttpServlet {
    private static final String UPDATE_FORM = "/WEB-INF/jsp/account/updateAccount.jsp";
    private static final String MAIN_FORM = "/WEB-INF/jsp/catalog/main.jsp";
    private AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("loginAccount");

        if (account == null) {
            req.setAttribute("signOnMsg", "Please log in first");
            req.getRequestDispatcher("/signOnForm").forward(req, resp);
            return;
        }

        req.setAttribute("account", account); // 传递用户信息到JSP
        req.getRequestDispatcher("/WEB-INF/jsp/account/myAccount.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("loginAccount");

        if (account == null) {
            req.setAttribute("signOnMsg", "Session expired, please log in again");
            req.getRequestDispatcher("/signOnForm").forward(req, resp);
            return;
        }

        // 1. 更新基本信息
        account.setEmail(req.getParameter("email") != null ? req.getParameter("email") : account.getEmail());
        account.setFirstName(req.getParameter("firstName") != null ? req.getParameter("firstName") : account.getFirstName());
        account.setLastName(req.getParameter("lastName") != null ? req.getParameter("lastName") : account.getLastName());
        account.setAddress1(req.getParameter("address1") != null ? req.getParameter("address1") : account.getAddress1());
        account.setAddress2(req.getParameter("address2") != null ? req.getParameter("address2") : account.getAddress2());
        account.setCity(req.getParameter("city") != null ? req.getParameter("city") : account.getCity());
        account.setState(req.getParameter("state") != null ? req.getParameter("state") : account.getState());
        account.setZip(req.getParameter("zip") != null ? req.getParameter("zip") : account.getZip());
        account.setCountry(req.getParameter("country") != null ? req.getParameter("country") : account.getCountry());
        account.setPhone(req.getParameter("phone") != null ? req.getParameter("phone") : account.getPhone());
        account.setLanguagePreference(req.getParameter("languagePreference") != null ? req.getParameter("languagePreference") : account.getLanguagePreference());
        account.setFavoriteCategoryId(req.getParameter("favoriteCategoryId") != null ? req.getParameter("favoriteCategoryId") : account.getFavoriteCategoryId());
        account.setListOption("on".equals(req.getParameter("listOption")));
        account.setBannerOption("on".equals(req.getParameter("bannerOption")));


        // 2. 处理密码更新
        String oldPwd = req.getParameter("oldPassword");
        String newPwd = req.getParameter("newPassword");
        String updateMsg = null;

        try {
            if (oldPwd != null && !oldPwd.trim().isEmpty() && newPwd != null && !newPwd.trim().isEmpty()) {
                boolean passwordUpdated = accountService.updateSignon(account.getUsername(), oldPwd, newPwd);
                if (!passwordUpdated) {
                    updateMsg = "Old password is incorrect";
                } else {
                    account.setPassword(newPwd);
                    updateMsg = "Password updated successfully";
                }
            }

            if (updateMsg == null || updateMsg.contains("successfully")) {
                accountService.updateAccountInfo(account);
                session.setAttribute("loginAccount", account); // 修正Session属性名
                updateMsg = updateMsg == null ? "Profile updated successfully" : updateMsg + ", profile updated successfully";
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateMsg = "Failed to update profile: " + e.getMessage();
        }

        req.setAttribute("updateMsg", updateMsg); // 提示信息
        req.setAttribute("account", account); // 更新后的用户信息
        req.getRequestDispatcher("/WEB-INF/jsp/account/myAccount.jsp").forward(req, resp);
    }
}