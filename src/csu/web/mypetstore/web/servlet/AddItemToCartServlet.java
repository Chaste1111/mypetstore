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

public class AddItemToCartServlet extends HttpServlet {
    private static final String CART_JSP = "/WEB-INF/jsp/cart/cart.jsp";
    private static final String SIGN_ON_FORM = "/signOnForm";
    private CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取请求参数（商品ID）
        String itemId = req.getParameter("workingItemId");
        if (itemId == null || itemId.trim().isEmpty()) {
            req.setAttribute("cartMsg", "Invalid item ID");
            req.getRequestDispatcher(CART_JSP).forward(req, resp);
            return;
        }

        // 2. 校验用户登录状态
        HttpSession session = req.getSession();
        Account loginAccount = (Account) session.getAttribute("loginAccount");
        if (loginAccount == null) {
            req.setAttribute("signOnMsg", "Please log in to add items to cart");
            req.getRequestDispatcher(SIGN_ON_FORM).forward(req, resp);
            return;
        }

        // 3. 调用CartService添加商品到数据库购物车
        try {
            cartService.addItemToCart(loginAccount.getUsername(), itemId);
            // 4. 从数据库加载最新购物车并更新Session
            Cart cart = cartService.getCartByUserId(loginAccount.getUsername());
            session.setAttribute("cart", cart);
            // 记录添加购物车日志
            LogUtil.recordLog(req, LogUtil.OP_ADD_CART,
                    "添加商品到购物车，商品ID：" + itemId + "，商品名称：" + cartService.getCartByUserId(loginAccount.getUsername()).getItemMap().get(itemId).getItem().getProduct().getName());
            req.setAttribute("cartMsg", "Item added to cart successfully");
        } catch (Exception e) {
            req.setAttribute("cartMsg", "Failed to add item: " + e.getMessage());
            e.printStackTrace();
        }

        // 5. 跳转回购物车页面
        req.getRequestDispatcher(CART_JSP).forward(req, resp);
    }
}