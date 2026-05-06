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

public class RemoveCartItemServlet extends HttpServlet {
    private static final String CART_JSP = "/WEB-INF/jsp/cart/cart.jsp";
    private static final String SIGN_ON_FORM = "/signOnForm";
    private CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取要删除的商品ID
        String itemId = req.getParameter("workingItemId");
        if (itemId == null || itemId.trim().isEmpty()) {
            req.setAttribute("cartMsg", "Invalid item ID");
            req.getRequestDispatcher(CART_JSP).forward(req, resp);
            return;
        }

        // 2. 校验登录状态
        HttpSession session = req.getSession();
        Account loginAccount = (Account) session.getAttribute("loginAccount");
        if (loginAccount == null) {
            req.setAttribute("signOnMsg", "Please log in to remove items");
            req.getRequestDispatcher(SIGN_ON_FORM).forward(req, resp);
            return;
        }

        // 3. 调用CartService从数据库删除购物车项
        try {
            cartService.removeCartItem(loginAccount.getUsername(), itemId);
            // 4. 加载最新购物车并更新Session
            Cart cart = cartService.getCartByUserId(loginAccount.getUsername());
            session.setAttribute("cart", cart);
            // 移除购物车成功后（RemoveCartItemServlet的doGet方法中）
            session.setAttribute("cart", cart);
// 记录移除购物车日志
            LogUtil.recordLog(req, LogUtil.OP_REMOVE_CART, "从购物车移除商品，商品ID：" + itemId);
            req.setAttribute("cartMsg", "Item removed from cart");
        } catch (Exception e) {
            req.setAttribute("cartMsg", "Failed to remove item: " + e.getMessage());
            e.printStackTrace();
        }

        // 5. 跳转回购物车页面
        req.getRequestDispatcher(CART_JSP).forward(req, resp);
    }
}