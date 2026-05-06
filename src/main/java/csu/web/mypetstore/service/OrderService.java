package csu.web.mypetstore.service;

import csu.web.mypetstore.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 订单服务接口
 * 继承 MyBatis Plus 的 IService，自动获得基础 CRUD 方法
 */
public interface OrderService extends IService<Order> {

    /**
     * 提交订单
     * @param order 订单对象
     */
    void placeOrder(Order order);

    /**
     * 根据用户名查询订单列表
     * @param username 用户名
     * @return 订单列表
     */
    List<Order> getOrdersByUsername(String username);

    /**
     * 根据订单ID和用户名查询单个订单（加用户名防止越权）
     * @param orderId 订单ID
     * @param username 用户名
     * @return 订单对象
     */
    Order getOrderById(String orderId, String username);
}