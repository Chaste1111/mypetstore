package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.domain.Order;
import csu.web.mypetstore.service.OrderService;
import csu.web.mypetstore.util.LogUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ViewOrdersServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("loginAccount");

        if (account == null) {
            req.setAttribute("msg", "Please log in to view your orders.");
            req.getRequestDispatcher("/signOnForm").forward(req, resp);
            return;
        }

        List<Order> orders = orderService.getOrdersByUsername(account.getUsername());
        req.setAttribute("orders", orders);
        // 记录查看订单日志
        LogUtil.recordLog(req, LogUtil.OP_VIEW_ORDER, "查看个人订单列表，订单总数：" + orders.size());
        req.getRequestDispatcher("/WEB-INF/jsp/order/orderList.jsp").forward(req, resp);
    }
}