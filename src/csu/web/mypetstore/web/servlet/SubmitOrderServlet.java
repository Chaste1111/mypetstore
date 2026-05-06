package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.*;
import csu.web.mypetstore.service.CartService;
import csu.web.mypetstore.service.CatalogService;
import csu.web.mypetstore.persistence.Impl.ItemDaoImpl;
import csu.web.mypetstore.persistence.ItemDao;
import csu.web.mypetstore.service.OrderService;
import csu.web.mypetstore.util.LogUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SubmitOrderServlet extends HttpServlet {
    private static final String NEW_ORDER_JSP = "/WEB-INF/jsp/order/newOrder.jsp";
    private static final String ORDER_CONFIRM_JSP = "/WEB-INF/jsp/order/orderConfirmation.jsp";
    private static final String SIGN_ON_FORM = "/signOnForm";
    private CartService cartService = new CartService();
    private CatalogService catalogService = new CatalogService();
    private OrderService orderService = new OrderService(); // 注入OrderService
    private ItemDao itemDao = new ItemDaoImpl();

    // 订单主表插入SQL
    private static final String INSERT_ORDER = "INSERT INTO ORDERS " +
            "(ORDERID, USERID, ORDERDATE, SHIPADDR1, SHIPADDR2, SHIPCITY, SHIPSTATE, SHIPZIP, SHIPCOUNTRY, " +
            "BILLADDR1, BILLADDR2, BILLCITY, BILLSTATE, BILLZIP, BILLCOUNTRY, TOTALPRICE, PAYMENTTYPE, STATUS) " +
            "VALUES (?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PENDING')";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Account loginAccount = (Account) session.getAttribute("loginAccount");
        Cart cart = cartService.getCartByUserId(loginAccount.getUsername());

        try {
            // 1. 校验登录和购物车（原有逻辑保留）
            if (loginAccount == null || cart == null || cart.getNumberOfItems() == 0) {
                req.setAttribute("checkoutMsg", "Please log in and add items to cart first!");
                req.getRequestDispatcher(NEW_ORDER_JSP).forward(req, resp);
                return;
            }
            // 2. 校验库存（原有逻辑保留）
            boolean stockEnough = true;
            for (CartItem cartItem : cart.getCartItemList()) {
                String itemId = cartItem.getItem().getItemId();
                int buyQuantity = cartItem.getQuantity();
                int stockQuantity = itemDao.getInventoryQuantity(itemId);
                if (stockQuantity < buyQuantity) {
                    req.setAttribute("checkoutMsg", "Item " + itemId + " is out of stock! Remaining: " + stockQuantity);
                    stockEnough = false;
                    break;
                }
            }
            if (!stockEnough) {
                req.getRequestDispatcher(NEW_ORDER_JSP).forward(req, resp);
                return;
            }

            // 3. 封装Order对象：设置实时创建时间（核心修复）
            Order order = new Order();
            order.setOrderId(UUID.randomUUID().toString().replace("-", "").substring(0, 20)); // 订单ID
            order.setUsername(loginAccount.getUsername()); // 用户名
            order.setOrderDate(new Date()); // 关键：Java层生成实时时间（而非依赖数据库NOW()）
            order.setTotalPrice(cart.getSubTotal()); // 订单总价（从购物车获取）
            order.setPaymentType(req.getParameter("paymentType")); // 支付方式
            order.setStatus("PENDING"); // 订单状态

            // 4. 生成唯一订单ID
            String orderId = UUID.randomUUID().toString().replace("-", "").substring(0, 20);

            // 4. 封装收货地址（从表单获取，原有逻辑保留）
            order.setShippingAddress1(req.getParameter("shippingAddress1"));
            order.setShippingAddress2(req.getParameter("shippingAddress2"));
            order.setShippingCity(req.getParameter("shippingCity"));
            order.setShippingState(req.getParameter("shippingState"));
            order.setShippingZip(req.getParameter("shippingZip"));
            order.setShippingCountry(req.getParameter("shippingCountry"));
            // 账单地址默认与收货地址一致（可根据需求扩展）
            order.setBillingAddress1(req.getParameter("shippingAddress1"));
            order.setBillingAddress2(req.getParameter("shippingAddress2"));
            order.setBillingCity(req.getParameter("shippingCity"));
            order.setBillingState(req.getParameter("shippingState"));
            order.setBillingZip(req.getParameter("shippingZip"));
            order.setBillingCountry(req.getParameter("shippingCountry"));

            // 5. 封装订单项（CartItem转LineItem）
            List<LineItem> lineItems = new ArrayList<>();
            int lineNum = 1; // 订单项行号
            for (CartItem cartItem : cart.getCartItemList()) {
                LineItem lineItem = new LineItem();
                lineItem.setLineNumber(lineNum++);
                lineItem.setItem(cartItem.getItem());
                lineItem.setQuantity(cartItem.getQuantity());
                lineItem.setUnitPrice(cartItem.getItem().getListPrice());
                lineItems.add(lineItem);
            }
            order.setLineItems(lineItems);

            // 6. 调用Service提交订单（复用已有逻辑，避免原生JDBC）
            orderService.placeOrder(order);

            // 7. 清空购物车+跳转确认页
            cartService.clearCart(loginAccount.getUsername());
            session.removeAttribute("cart");
            // 记录下单日志
            LogUtil.recordLog(req, LogUtil.OP_PLACE_ORDER,
                    "创建订单成功，订单ID：" + order.getOrderId() + "，订单金额：" + order.getTotalPrice() + "，商品数量：" + order.getLineItems().size());
            req.setAttribute("order", order);
            req.setAttribute("checkoutMsg", "Order placed successfully! Your Order ID: " + order.getOrderId());
            req.getRequestDispatcher(ORDER_CONFIRM_JSP).forward(req, resp); // 跳确认页而非原页面

        } catch (Exception e) {
            req.setAttribute("checkoutMsg", "Failed to place order: " + e.getMessage());
            e.printStackTrace();
            req.getRequestDispatcher(NEW_ORDER_JSP).forward(req, resp);
        }
    }
}