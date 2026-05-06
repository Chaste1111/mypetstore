package csu.web.mypetstore.service.impl;

import csu.web.mypetstore.domain.Category;
import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.domain.Product;
import csu.web.mypetstore.persistence.CategoryDao;
import csu.web.mypetstore.persistence.ItemDao;
import csu.web.mypetstore.persistence.ProductDao;
import csu.web.mypetstore.service.CatalogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品目录服务实现类
 * 继承 MyBatis Plus 的 ServiceImpl，自动获得基础 CRUD 方法
 */
@Service
public class CatalogServiceImpl extends ServiceImpl<ProductDao, Product> implements CatalogService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ItemDao itemDao;

    /**
     * 获取所有商品分类列表
     */
    @Override
    public List<Category> getCategoryList() {
        return categoryDao.getCategoryList();
    }

    /**
     * 根据分类ID获取分类详情
     * 使用 LambdaQueryWrapper 构建查询条件
     */
    @Override
    public Category getCategory(String categoryId) {
        return categoryDao.getCategory(categoryId);
    }

    /**
     * 根据产品ID获取产品详情
     * 使用 MyBatis Plus 内置方法
     */
    @Override
    public Product getProduct(String productId) {
        return baseMapper.selectById(productId);
    }

    /**
     * 根据分类ID获取产品列表
     */
    @Override
    public List<Product> getProductListByCategory(String categoryId) {
        return productDao.getProductListByCategory(categoryId);
    }

    /**
     * 根据关键字搜索产品
     */
    @Override
    public List<Product> searchProductList(String keyword) {
        return productDao.searchProductList(keyword.toLowerCase());
    }

    /**
     * 根据产品ID获取商品列表
     */
    @Override
    public List<Item> getItemListByProduct(String productId) {
        return itemDao.getItemListByProduct(productId);
    }

    /**
     * 根据商品ID获取商品详情
     */
    @Override
    public Item getItem(String itemId) {
        return itemDao.getItem(itemId);
    }

    /**
     * 检查商品是否有库存
     */
    @Override
    public boolean isItemInStock(String itemId) {
        return itemDao.getInventoryQuantity(itemId) > 0;
    }

    /**
     * 根据关键字自动补全产品名称
     * 使用 LambdaQueryWrapper 构建模糊查询条件
     */
    @Override
    public List<String> autoCompleteProductName(String keyword) {
        // 使用 LambdaQueryWrapper 构建模糊查询
        List<Product> products = baseMapper.selectList(
            new LambdaQueryWrapper<Product>()
                .like(Product::getName, keyword)
                .select(Product::getName)
        );
        
        // 去重并返回名称列表
        Set<String> nameSet = new HashSet<>();
        for (Product product : products) {
            nameSet.add(product.getName());
        }
        
        return new ArrayList<>(nameSet);
    }
}