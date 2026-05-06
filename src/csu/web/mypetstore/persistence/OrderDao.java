package csu.web.mypetstore.persistence;

import csu.web.mypetstore.domain.Order;

import java.util.List;

public interface OrderDao {
    void insertOrder(Order order);
    void insertOrderItem(Order order);
    List<Order> getOrdersByUsername(String username);
    // 新增：根据订单ID和用户名查询单个订单（加用户名防止越权）
    Order getOrderById(String orderId, String username);
}