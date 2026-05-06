package csu.web.mypetstore.service;

import csu.web.mypetstore.domain.Category;
import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.domain.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 商品目录服务接口
 * 继承 MyBatis Plus 的 IService，自动获得基础 CRUD 方法
 */
public interface CatalogService extends IService<Product> {

    /**
     * 获取所有商品分类列表
     * @return 分类列表
     */
    List<Category> getCategoryList();

    /**
     * 根据分类ID获取分类详情
     * @param categoryId 分类ID
     * @return 分类对象
     */
    Category getCategory(String categoryId);

    /**
     * 根据产品ID获取产品详情
     * @param productId 产品ID
     * @return 产品对象
     */
    Product getProduct(String productId);

    /**
     * 根据分类ID获取产品列表
     * @param categoryId 分类ID
     * @return 产品列表
     */
    List<Product> getProductListByCategory(String categoryId);

    /**
     * 根据关键字搜索产品
     * @param keyword 搜索关键字
     * @return 产品列表
     */
    List<Product> searchProductList(String keyword);

    /**
     * 根据产品ID获取商品列表
     * @param productId 产品ID
     * @return 商品列表
     */
    List<Item> getItemListByProduct(String productId);

    /**
     * 根据商品ID获取商品详情
     * @param itemId 商品ID
     * @return 商品对象
     */
    Item getItem(String itemId);

    /**
     * 检查商品是否有库存
     * @param itemId 商品ID
     * @return 是否有库存
     */
    boolean isItemInStock(String itemId);

    /**
     * 根据关键字自动补全产品名称
     * @param keyword 关键字
     * @return 匹配的产品名称列表
     */
    List<String> autoCompleteProductName(String keyword);
}