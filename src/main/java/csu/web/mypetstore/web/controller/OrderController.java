package csu.web.mypetstore.web.controller;

import csu.web.mypetstore.common.Result;
import csu.web.mypetstore.domain.Order;
import csu.web.mypetstore.persistence.OrderDao;
import csu.web.mypetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单 RESTful 控制器
 * 提供订单创建、查询等接口
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    /**
     * 创建订单
     * POST /api/orders
     *
     * @param order 订单信息
     * @return 创建的订单
     */
    @PostMapping
    public Result<Order> createOrder(@RequestBody Order order) {
        try {
            orderService.placeOrder(order);
            return Result.success("订单创建成功", order);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("订单创建失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户名查询订单列表
     * GET /api/orders/user/{username}
     *
     * @param username 用户名
     * @return 订单列表
     */
    @GetMapping("/user/{username}")
    public Result<List<Order>> getOrdersByUsername(@PathVariable String username) {
        List<Order> orders = orderService.getOrdersByUsername(username);
        return Result.success(orders);
    }

    /**
     * 根据订单ID和用户名查询订单详情（防止越权）
     * GET /api/orders/{orderId}/user/{username}
     *
     * @param orderId  订单ID
     * @param username 用户名
     * @return 订单详情
     */
    @GetMapping("/{orderId}/user/{username}")
    public Result<Order> getOrderById(@PathVariable String orderId, @PathVariable String username) {
        Order order = orderService.getOrderById(orderId, username);
        if (order != null) {
            return Result.success(order);
        }
        return Result.error(404, "订单不存在或无权限访问");
    }

    /**
     * 更新订单状态
     * PUT /api/orders/{orderId}/status
     *
     * @param orderId 订单ID
     * @param status  新状态
     * @return 更新结果
     */
    @PutMapping("/{orderId}/status")
    public Result<String> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        Order order = orderService.getById(orderId);
        if (order != null) {
            // 将中文状态转换为数据库状态码
            String dbStatus = convertToDbStatus(status);
            order.setStatus(dbStatus);
            orderService.updateById(order);
            return Result.success("订单状态更新成功");
        }
        return Result.error(404, "订单不存在");
    }

    /**
     * 将中文状态转换为数据库状态码
     */
    private String convertToDbStatus(String status) {
        if (status == null) {
            return "P";
        }
        switch (status) {
            case "待支付":
            case "P":
                return "P";  // Pending
            case "已支付":
                return "P";  // 暂时也用P，或者可以扩展为"PA"
            case "配送中":
                return "S";  // Shipped
            case "已完成":
                return "C";  // Completed
            case "已取消":
                return "X";  // Cancelled
            default:
                // 如果已经是短状态码且长度<=2，直接返回
                if (status.length() <= 2) {
                    return status;
                }
                // 否则默认为待支付
                return "P";
        }
    }

    /**
     * 取消订单（将订单状态更新为已取消）
     * DELETE /api/orders/{orderId}
     *
     * @param orderId 订单ID
     * @return 取消结果
     */
    @DeleteMapping("/{orderId}")
    public Result<String> deleteOrder(@PathVariable String orderId) {
        try {
            Order order = orderService.getById(orderId);
            if (order != null) {
                // 将订单状态更新为已取消（X）
                order.setStatus("X");
                orderService.updateById(order);
                return Result.success("订单已取消");
            }
            return Result.error(404, "订单不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("订单取消失败：" + e.getMessage());
        }
    }
}