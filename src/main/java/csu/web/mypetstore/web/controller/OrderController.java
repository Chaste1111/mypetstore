package csu.web.mypetstore.web.controller;

import csu.web.mypetstore.common.Result;
import csu.web.mypetstore.domain.Order;
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
            order.setStatus(status);
            orderService.updateById(order);
            return Result.success("订单状态更新成功");
        }
        return Result.error(404, "订单不存在");
    }

    /**
     * 删除订单
     * DELETE /api/orders/{orderId}
     *
     * @param orderId 订单ID
     * @return 删除结果
     */
    @DeleteMapping("/{orderId}")
    public Result<String> deleteOrder(@PathVariable String orderId) {
        boolean success = orderService.removeById(orderId);
        if (success) {
            return Result.success("订单删除成功");
        }
        return Result.error(404, "订单不存在");
    }
}