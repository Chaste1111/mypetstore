package csu.web.mypetstore.service;

import csu.web.mypetstore.domain.Order;
import csu.web.mypetstore.persistence.Impl.OrderDaoImpl;
import csu.web.mypetstore.persistence.OrderDao;

import java.util.List;

public class OrderService {
    private final OrderDao orderDao = new OrderDaoImpl();

    public void placeOrder(Order order) {
        orderDao.insertOrder(order);
        orderDao.insertOrderItem(order);
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderDao.getOrdersByUsername(username);
    }

    public Order getOrderById(String orderId, String username) {
        // 调用 OrderDaoImpl 新增的 getOrderById 方法，查询订单及关联的订单项
        return orderDao.getOrderById(orderId, username);
    }
}