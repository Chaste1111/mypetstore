package csu.web.mypetstore.persistence;

import csu.web.mypetstore.domain.Item;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 商品明细数据访问接口
 * 继承 MyBatis Plus 的 BaseMapper，自动获得基础 CRUD 方法
 */
@Mapper
public interface ItemDao extends BaseMapper<Item> {

    /**
     * 更新库存数量
     */
    void updateInventoryQuantity(Map<String, Object> param);

    /**
     * 获取库存数量
     */
    int getInventoryQuantity(String itemId);

    /**
     * 根据产品ID获取商品列表
     */
    List<Item> getItemListByProduct(String productId);

    /**
     * 根据商品ID查询商品详情
     */
    Item getItem(String itemId);

    /**
     * 根据关键字搜索商品
     */
    List<Item> searchItems(String keyword);
}