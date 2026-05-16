package csu.web.mypetstore.service.impl;

import csu.web.mypetstore.domain.LineItem;
import csu.web.mypetstore.domain.Order;
import csu.web.mypetstore.persistence.OrderDao;
import csu.web.mypetstore.service.CartService;
import csu.web.mypetstore.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务实现类
 * 继承 MyBatis Plus 的 ServiceImpl，自动获得基础 CRUD 方法
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CartService cartService;

    /**
     * 提交订单
     * 使用 @Transactional 保证订单和订单项的原子性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void placeOrder(Order order) {
        // 验证并设置用户名（必需字段）
        if (order.getUsername() == null || order.getUsername().isEmpty()) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 生成订单ID
        if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
            order.setOrderId(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        }
        
        // 设置订单日期（如果未设置）
        if (order.getOrderDate() == null) {
            order.setOrderDate(new Date());
        }
        
        // 设置默认状态为待支付
        if (order.getStatus() == null || order.getStatus().isEmpty()) {
            order.setStatus("P");
        }
        
        System.out.println("DEBUG OrderServiceImpl: Inserting order with username=" + order.getUsername() + ", orderId=" + order.getOrderId());
        
        // 插入订单主表
        orderDao.insertOrder(order);
        // 插入订单项
        orderDao.insertOrderItem(order);
        
        // 订单提交成功后，从购物车中移除已下单的商品
        removeOrderedItemsFromCart(order);
    }

    /**
     * 根据用户名查询订单列表
     * 使用 LambdaQueryWrapper 构建查询条件
     */
    @Override
    public List<Order> getOrdersByUsername(String username) {
        List<Order> orders = orderDao.getOrdersByUsername(username);
        // 为每个订单加载订单项
        for (Order order : orders) {
            List<LineItem> lineItems = orderDao.getLineItemsByOrderId(order.getOrderId());
            order.setLineItems(lineItems);
        }
        return orders;
    }

    /**
     * 根据订单ID和用户名查询单个订单（加用户名防止越权）
     */
    @Override
    public Order getOrderById(String orderId, String username) {
        Order order = orderDao.getOrderById(orderId, username);
        if (order != null) {
            // 加载订单项
            List<LineItem> lineItems = orderDao.getLineItemsByOrderId(orderId);
            order.setLineItems(lineItems);
        }
        return order;
    }

    /**
     * 从购物车中移除已下单的商品
     * @param order 订单对象
     */
    private void removeOrderedItemsFromCart(Order order) {
        if (order == null || order.getLineItems() == null || order.getLineItems().isEmpty()) {
            return;
        }

        String userId = order.getUsername();
        List<LineItem> lineItems = order.getLineItems();

        // 遍历订单项，逐个从购物车中移除
        for (LineItem lineItem : lineItems) {
            try {
                cartService.removeCartItem(userId, lineItem.getItemId());
                System.out.println("DEBUG: Removed item " + lineItem.getItemId() + " from cart for user " + userId);
            } catch (Exception e) {
                // 记录错误但不影响订单提交流程
                System.err.println("WARNING: Failed to remove item " + lineItem.getItemId() + " from cart: " + e.getMessage());
            }
        }
    }
}