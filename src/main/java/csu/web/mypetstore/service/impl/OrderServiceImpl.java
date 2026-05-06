package csu.web.mypetstore.service.impl;

import csu.web.mypetstore.domain.Order;
import csu.web.mypetstore.persistence.OrderDao;
import csu.web.mypetstore.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单服务实现类
 * 继承 MyBatis Plus 的 ServiceImpl，自动获得基础 CRUD 方法
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService {

    @Autowired
    private OrderDao orderDao;

    /**
     * 提交订单
     * 使用 @Transactional 保证订单和订单项的原子性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void placeOrder(Order order) {
        // 插入订单主表
        orderDao.insertOrder(order);
        // 插入订单项
        orderDao.insertOrderItem(order);
    }

    /**
     * 根据用户名查询订单列表
     * 使用 LambdaQueryWrapper 构建查询条件
     */
    @Override
    public List<Order> getOrdersByUsername(String username) {
        return orderDao.getOrdersByUsername(username);
    }

    /**
     * 根据订单ID和用户名查询单个订单（加用户名防止越权）
     */
    @Override
    public Order getOrderById(String orderId, String username) {
        return orderDao.getOrderById(orderId, username);
    }
}