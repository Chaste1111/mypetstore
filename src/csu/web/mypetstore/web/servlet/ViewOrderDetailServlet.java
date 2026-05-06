package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.domain.Order;
import csu.web.mypetstore.service.OrderService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ViewOrderDetailServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private static final String ORDER_DETAIL_JSP = "/WEB-INF/jsp/order/orderDetail.jsp";
    private static final String SIGN_ON_FORM = "/signOnForm";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            req.setAttribute("msg", "Please log in to view order details.");
            req.getRequestDispatcher(SIGN_ON_FORM).forward(req, resp);
            return;
        }

        // 获取订单ID（注意前端参数名是 orderId，避免拼写错误）
        String orderId = req.getParameter("orderId");
        if (orderId == null || orderId.trim().isEmpty()) {
            req.setAttribute("detailMsg", "Invalid order ID.");
            req.getRequestDispatcher("/viewOrders").forward(req, resp);
            return;
        }

        // 调用 Service 查询订单详情（需在 OrderService 中新增 getOrderById 方法）
        Order order = orderService.getOrderById(orderId, account.getUsername());
        if (order == null) {
            req.setAttribute("detailMsg", "Order not found.");
            req.getRequestDispatcher("/viewOrders").forward(req, resp);
            return;
        }

        req.setAttribute("order", order);
        req.getRequestDispatcher(ORDER_DETAIL_JSP).forward(req, resp);
    }
}