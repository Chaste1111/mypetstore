package csu.web.mypetstore.persistence.Impl;

import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.domain.LineItem;
import csu.web.mypetstore.domain.Order;
import csu.web.mypetstore.persistence.DBUtil;
import csu.web.mypetstore.persistence.ItemDao;
import csu.web.mypetstore.persistence.OrderDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    // 修改后SQL（将NOW()改为?，接收Java传递的时间）
    private static final String INSERT_ORDER = "INSERT INTO ORDERS " +
            "(ORDERID, USERID, ORDERDATE, SHIPADDR1, SHIPADDR2, SHIPCITY, SHIPSTATE, SHIPZIP, SHIPCOUNTRY, " +
            "BILLADDR1, BILLADDR2, BILLCITY, BILLSTATE, BILLZIP, BILLCOUNTRY, TOTALPRICE, PAYMENTTYPE, STATUS) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PENDING')";
    // 插入订单项 SQL
    private static final String INSERT_ORDER_ITEM = "INSERT INTO LINEITEM " +
            "(ORDERID, LINENUM, ITEMID, QUANTITY, UNITPRICE) VALUES (?, ?, ?, ?, ?)";

    // 查询用户订单 SQL
    private static final String GET_ORDERS_BY_USERNAME = "SELECT * FROM ORDERS WHERE USERID = ? ORDER BY ORDERDATE DESC";

    // 查询订单项 SQL
    private static final String GET_LINE_ITEMS_BY_ORDER = "SELECT * FROM LINEITEM WHERE ORDERID = ?";

    private static final String GET_ORDER_BY_ID = "SELECT * FROM ORDERS WHERE ORDERID = ? AND USERID = ?";


    @Override
    public void insertOrder(Order order) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_ORDER)) {
            pstmt.setString(1, order.getOrderId());
            pstmt.setString(2, order.getUsername());
            // 新增：传递Java层生成的实时时间（转换为数据库Timestamp类型）
            pstmt.setTimestamp(3, new java.sql.Timestamp(order.getOrderDate().getTime()));
            // 以下为原有参数（注意参数索引从3开始后移1位，需对应SQL顺序）
            pstmt.setString(4, order.getShippingAddress1());
            pstmt.setString(5, order.getShippingAddress2());
            pstmt.setString(6, order.getShippingCity());
            pstmt.setString(7, order.getShippingState());
            pstmt.setString(8, order.getShippingZip());
            pstmt.setString(9, order.getShippingCountry());
            pstmt.setString(10, order.getBillingAddress1());
            pstmt.setString(11, order.getBillingAddress2());
            pstmt.setString(12, order.getBillingCity());
            pstmt.setString(13, order.getBillingState());
            pstmt.setString(14, order.getBillingZip());
            pstmt.setString(15, order.getBillingCountry());
            pstmt.setBigDecimal(16, order.getTotalPrice());
            pstmt.setString(17, order.getPaymentType());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertOrderItem(Order order) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_ORDER_ITEM)) {
            for (LineItem item : order.getLineItems()) {
                pstmt.setString(1, order.getOrderId());
                pstmt.setInt(2, item.getLineNumber());
                pstmt.setString(3, item.getItem().getItemId());
                pstmt.setInt(4, item.getQuantity());
                pstmt.setBigDecimal(5, item.getUnitPrice());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> getOrdersByUsername(String username) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_ORDERS_BY_USERNAME)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getString("ORDERID"));
                order.setUsername(rs.getString("USERID"));
                order.setOrderDate(rs.getTimestamp("ORDERDATE"));
                order.setShippingAddress1(rs.getString("SHIPADDR1"));
                order.setShippingAddress2(rs.getString("SHIPADDR2"));
                order.setShippingCity(rs.getString("SHIPCITY"));
                order.setShippingState(rs.getString("SHIPSTATE"));
                order.setShippingZip(rs.getString("SHIPZIP"));
                order.setShippingCountry(rs.getString("SHIPCOUNTRY"));
                order.setBillingAddress1(rs.getString("BILLADDR1"));
                order.setBillingAddress2(rs.getString("BILLADDR2"));
                order.setBillingCity(rs.getString("BILLCITY"));
                order.setBillingState(rs.getString("BILLSTATE"));
                order.setBillingZip(rs.getString("BILLZIP"));
                order.setBillingCountry(rs.getString("BILLCOUNTRY"));
                order.setTotalPrice(rs.getBigDecimal("TOTALPRICE"));
                order.setPaymentType(rs.getString("PAYMENTTYPE"));
                order.setStatus(rs.getString("STATUS"));

                // 关联订单项
                List<LineItem> lineItems = getLineItemsByOrderId(order.getOrderId(), conn);
                order.setLineItems(lineItems);
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    // 新增：查询单个订单（包含订单项）
    @Override
    public Order getOrderById(String orderId, String username) {
        Order order = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_ORDER_BY_ID)) {
            pstmt.setString(1, orderId);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                order = new Order();
                // 封装订单主表数据
                order.setOrderId(rs.getString("ORDERID"));
                order.setUsername(rs.getString("USERID"));
                order.setOrderDate(rs.getTimestamp("ORDERDATE"));
                order.setShippingAddress1(rs.getString("SHIPADDR1"));
                order.setShippingAddress2(rs.getString("SHIPADDR2"));
                order.setShippingCity(rs.getString("SHIPCITY"));
                order.setShippingState(rs.getString("SHIPSTATE"));
                order.setShippingZip(rs.getString("SHIPZIP"));
                order.setShippingCountry(rs.getString("SHIPCOUNTRY"));
                order.setBillingAddress1(rs.getString("BILLADDR1"));
                order.setBillingAddress2(rs.getString("BILLADDR2"));
                order.setBillingCity(rs.getString("BILLCITY"));
                order.setBillingState(rs.getString("BILLSTATE"));
                order.setBillingZip(rs.getString("BILLZIP"));
                order.setBillingCountry(rs.getString("BILLCOUNTRY"));
                order.setTotalPrice(rs.getBigDecimal("TOTALPRICE"));
                order.setPaymentType(rs.getString("PAYMENTTYPE"));
                order.setStatus(rs.getString("STATUS"));

                // 关联查询订单项并封装
                List<LineItem> lineItems = getLineItemsByOrderId(orderId, conn);
                order.setLineItems(lineItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    // 私有方法：查询订单关联的商品项
    private List<LineItem> getLineItemsByOrderId(String orderId, Connection conn) throws Exception {
        List<LineItem> lineItems = new ArrayList<>();
        PreparedStatement pstmt = conn.prepareStatement(GET_LINE_ITEMS_BY_ORDER);
        pstmt.setString(1, orderId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            LineItem item = new LineItem();
            item.setLineNumber(rs.getInt("LINENUM"));
            item.setQuantity(rs.getInt("QUANTITY"));
            item.setUnitPrice(rs.getBigDecimal("UNITPRICE"));

            // 关联Item对象（需从ItemDao查询，此处简化示例）
            ItemDaoImpl itemDao = new ItemDaoImpl();
            Item productItem = itemDao.getItem(rs.getString("ITEMID"));
            item.setItem(productItem);

            lineItems.add(item);
        }
        return lineItems;
    }
}