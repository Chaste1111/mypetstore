package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.domain.Cart;
import csu.web.mypetstore.service.CartService;
import csu.web.mypetstore.util.LogUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

public class UpdateCartServlet extends HttpServlet {
    private static final String CART_JSP = "/WEB-INF/jsp/cart/cart.jsp";
    private static final String SIGN_ON_FORM = "/signOnForm";
    private CartService cartService = new CartService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Account loginAccount = (Account) session.getAttribute("loginAccount");

        // 1. 校验登录状态
        if (loginAccount == null) {
            req.setAttribute("signOnMsg", "Please log in to update cart");
            req.getRequestDispatcher(SIGN_ON_FORM).forward(req, resp);
            return;
        }

        // 2. 获取所有商品ID及对应的数量（表单参数名是itemId，值是quantity）
        Enumeration<String> paramNames = req.getParameterNames();
        boolean updateSuccess = true;
        String errorMsg = "";

        while (paramNames.hasMoreElements()) {
            String itemId = paramNames.nextElement();
            try {
                // 3. 解析数量（负数或零表示删除）
                int quantity = Integer.parseInt(req.getParameter(itemId).trim());
                // 4. 调用CartService更新数据库中的购物车项
                cartService.updateCartItemQuantity(loginAccount.getUsername(), itemId, quantity);
            } catch (NumberFormatException e) {
                updateSuccess = false;
                errorMsg = "Invalid quantity for item: " + itemId;
                e.printStackTrace();
                break;
            } catch (Exception e) {
                updateSuccess = false;
                errorMsg = "Failed to update item " + itemId + ": " + e.getMessage();
                e.printStackTrace();
                break;
            }
        }

        // 5. 加载最新购物车并更新Session
        Cart cart = cartService.getCartByUserId(loginAccount.getUsername());
        // 更新购物车成功后（UpdateCartServlet的doPost方法中）
        session.setAttribute("cart", cart);
        // 记录更新购物车日志（遍历更新的商品）

        StringBuilder content = new StringBuilder("更新购物车商品数量：");
        while (paramNames.hasMoreElements()) {
            String itemId = paramNames.nextElement();
            int quantity = Integer.parseInt(req.getParameter(itemId).trim());
            content.append("商品ID：").append(itemId).append("，新数量：").append(quantity).append("；");
        }
        LogUtil.recordLog(req, LogUtil.OP_UPDATE_CART, content.toString());


        // 6. 设置提示信息并跳转
        if (updateSuccess) {
            req.setAttribute("cartMsg", "Cart updated successfully");
        } else {
            req.setAttribute("cartMsg", errorMsg);
        }
        req.getRequestDispatcher(CART_JSP).forward(req, resp);
    }
}