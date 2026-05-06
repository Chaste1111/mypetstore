package csu.web.mypetstore.persistence.Impl;

import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.domain.Product;
import csu.web.mypetstore.persistence.DBUtil;
import csu.web.mypetstore.persistence.ItemDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemDaoImpl implements ItemDao {

    private static final String getItemListByProductString =
            "SELECT I.ITEMID, LISTPRICE, UNITCOST, SUPPLIER AS supplierId, I.PRODUCTID AS productId, NAME AS name, DESCN AS description, CATEGORY AS categoryId, STATUS, ATTR1 AS attribute1, ATTR2 AS attribute2, ATTR3 AS attribute3, ATTR4 AS attribute4, ATTR5 AS attribute5 FROM ITEM I, PRODUCT P WHERE P.PRODUCTID = I.PRODUCTID AND I.PRODUCTID = ?";
    private static final String getItemString =
            "SELECT I.ITEMID, LISTPRICE, UNITCOST, SUPPLIER AS supplierId, I.PRODUCTID AS productId, NAME AS name, DESCN AS description, CATEGORY AS categoryId, STATUS, ATTR1 AS attribute1, ATTR2 AS attribute2, ATTR3 AS attribute3, ATTR4 AS attribute4, ATTR5 AS attribute5, QTY AS quantity FROM ITEM I, PRODUCT P, INVENTORY V WHERE P.PRODUCTID = I.PRODUCTID AND I.ITEMID = V.ITEMID AND I.ITEMID = ?";
    private static final String getInventoryQuantityString =
            "SELECT QTY AS value FROM INVENTORY WHERE ITEMID = ?";
    private static final String updateInventoryQuantityString =
            "UPDATE INVENTORY SET QTY = QTY - ? WHERE ITEMID = ?";
    // 新增：Item搜索SQL（关联Product表，匹配ItemId或商品名）
    private static final String SEARCH_ITEMS =
            "SELECT I.ITEMID, I.PRODUCTID, I.LISTPRICE, P.NAME AS productName " +
                    "FROM ITEM I JOIN PRODUCT P ON I.PRODUCTID = P.PRODUCTID " +
                    "WHERE LOWER(I.ITEMID) LIKE ? OR LOWER(P.NAME) LIKE ?";



    @Override
    public void updateInventoryQuantity(Map<String, Object> param) {
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(updateInventoryQuantityString);
            String itemId = param.keySet().iterator().next();
            Integer increment = (Integer) param.get(itemId);
            pStatement.setInt(1,increment.intValue());
            pStatement.setString(2,itemId);
            pStatement.executeUpdate();
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getInventoryQuantity(String itemId) {
        int result = -1;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getInventoryQuantityString);
            pStatement.setString(1,itemId);
            ResultSet resultSet = pStatement.executeQuery();
            if (resultSet.next()){
                result = resultSet.getInt(1);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Item> getItemListByProduct(String productId) {
        List<Item> itemList = new ArrayList<Item>();
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getItemListByProductString);
            pStatement.setString(1,productId);
            ResultSet resultSet = pStatement.executeQuery();
            while(resultSet.next()){
                Item item = new Item();
                item.setItemId(resultSet.getString(1));
                item.setListPrice(resultSet.getBigDecimal(2));
                item.setUnitCost(resultSet.getBigDecimal(3));
                item.setSupplierId(resultSet.getInt(4));
                Product product = new Product();
                product.setProductId(resultSet.getString(5));
                product.setName(resultSet.getString(6));
                product.setDescription(resultSet.getString(7));
                product.setCategoryId(resultSet.getString(8));
                item.setProduct(product);
                item.setStatus(resultSet.getString(9));
                item.setAttribute1(resultSet.getString(10));
                item.setAttribute2(resultSet.getString(11));
                item.setAttribute3(resultSet.getString(12));
                item.setAttribute4(resultSet.getString(13));
                item.setAttribute5(resultSet.getString(14));
                itemList.add(item);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemList;
    }

    @Override
    public Item getItem(String itemId) {
        Item item = null;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getItemString);
            pStatement.setString(1, itemId);
            ResultSet resultSet = pStatement.executeQuery();
            if (resultSet.next()) {
                item = new Item();
                item.setItemId(resultSet.getString(1));
                item.setListPrice(resultSet.getBigDecimal(2));
                item.setUnitCost(resultSet.getBigDecimal(3));
                item.setSupplierId(resultSet.getInt(4));

                Product product = new Product();
                product.setProductId(resultSet.getString(5));
                product.setName(resultSet.getString(6));
                product.setDescription(resultSet.getString(7));
                product.setCategoryId(resultSet.getString(8));
                item.setProduct(product);

                item.setStatus(resultSet.getString(9));
                item.setAttribute1(resultSet.getString(10));
                item.setAttribute2(resultSet.getString(11));
                item.setAttribute3(resultSet.getString(12));
                item.setAttribute4(resultSet.getString(13));
                item.setAttribute5(resultSet.getString(14));
                item.setQuantity(resultSet.getInt(15));
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    // 新增：搜索Item的实现方法
    @Override
    public List<Item> searchItems(String keyword) {
        List<Item> itemList = new ArrayList<>();
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(SEARCH_ITEMS);
            pstmt.setString(1, keyword); // 匹配ItemId
            pstmt.setString(2, keyword); // 匹配商品名
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Item item = new Item();
                item.setItemId(rs.getString("ITEMID"));
                item.setProductId(rs.getString("PRODUCTID"));
                item.setListPrice(rs.getBigDecimal("LISTPRICE"));
                // 关联Product对象（仅设置名称，满足搜索展示需求）
                Product product = new Product();
                product.setName(rs.getString("productName"));
                item.setProduct(product);
                itemList.add(item);
            }

            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(pstmt);
            DBUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }
}