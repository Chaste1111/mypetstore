package csu.web.mypetstore.persistence;

import csu.web.mypetstore.domain.LineItem;
import csu.web.mypetstore.domain.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单数据访问接口
 * 继承 MyBatis Plus 的 BaseMapper，自动获得基础 CRUD 方法
 */
@Mapper
public interface OrderDao extends BaseMapper<Order> {

    /**
     * 插入订单
     */
    void insertOrder(Order order);

    /**
     * 插入订单项
     */
    void insertOrderItem(Order order);

    /**
     * 根据用户名查询订单列表
     */
    List<Order> getOrdersByUsername(String username);

    /**
     * 根据订单ID和用户名查询单个订单（加用户名防止越权）
     */
    Order getOrderById(String orderId, String username);

    /**
     * 根据订单ID删除订单项
     */
    void deleteLineItemsByOrderId(String orderId);

    /**
     * 根据订单ID查询订单项列表
     */
    List<LineItem> getLineItemsByOrderId(String orderId);
}