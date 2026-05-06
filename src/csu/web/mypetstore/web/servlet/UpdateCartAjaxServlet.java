package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.domain.Cart;
import csu.web.mypetstore.domain.CartItem;
import csu.web.mypetstore.service.CartService;
import csu.web.mypetstore.util.LogUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class UpdateCartAjaxServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CartService cartService = new CartService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        // 初始化响应，包含单个商品总价字段
        String result = "{\"success\":false, \"message\":\"\", \"totalItems\":0, \"subTotal\":0, \"itemTotal\":0}";

        try {
            // 1. 获取请求参数
            String itemId = req.getParameter("itemId");
            int quantity = Integer.parseInt(req.getParameter("quantity"));

            // 2. 校验登录状态
            HttpSession session = req.getSession();
            Account loginAccount = (Account) session.getAttribute("loginAccount");
            if (loginAccount == null) {
                result = "{\"success\":false, \"message\":\"Please log in first\", \"itemTotal\":0}";
                out.write(result);
                return;
            }

            // 3. 校验参数合法性
            if (itemId == null || itemId.trim().isEmpty() || quantity < 0) {
                result = "{\"success\":false, \"message\":\"Invalid parameters\", \"itemTotal\":0}";
                out.write(result);
                return;
            }

            // 4. 更新购物车数量
            cartService.updateCartItemQuantity(loginAccount.getUsername(), itemId, quantity);

            // 5. 获取更新后的购物车信息
            Cart cart = cartService.getCartByUserId(loginAccount.getUsername());
            session.setAttribute("cart", cart);

            // 6. 获取当前商品的单个总价（处理商品被删除的情况）
            BigDecimal itemTotal = BigDecimal.ZERO;
            CartItem updatedItem = cart.getItemMap().get(itemId); // 从购物车中获取更新后的商品项
            if (updatedItem != null) {
                itemTotal = updatedItem.getTotal(); // CartItem的getTotal()已封装单个商品总价计算逻辑
            }

            // 7. 构建成功响应（新增itemTotal字段）
            result = String.format(
                    "{\"success\":true, \"message\":\"Cart updated\", " +
                            "\"totalItems\":%d, \"subTotal\":%.2f, " +
                            "\"itemTotal\":%.2f}", // 单个商品总价
                    cart.getNumberOfItems(),
                    cart.getSubTotal().doubleValue(),
                    itemTotal.doubleValue()
            );

            // 记录日志（与系统其他购物车操作保持一致）
            LogUtil.recordLog(req, LogUtil.OP_UPDATE_CART,
                    "AJAX更新购物车：商品ID=" + itemId + "，新数量=" + quantity + "，单个总价=" + itemTotal);

        } catch (NumberFormatException e) {
            result = "{\"success\":false, \"message\":\"Invalid quantity format\", \"itemTotal\":0}";
        } catch (Exception e) {
            // 转义异常信息中的引号，避免JSON格式错误
            String errorMsg = e.getMessage().replace("\"", "\\\"");
            result = "{\"success\":false, \"message\":\"" + errorMsg + "\", \"itemTotal\":0}";
        } finally {
            out.write(result);
            out.close(); // 确保资源释放
        }
    }
}