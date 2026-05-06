package csu.web.mypetstore.service;

import csu.web.mypetstore.domain.Category;
import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.domain.Product;
import csu.web.mypetstore.persistence.CategoryDao;
import csu.web.mypetstore.persistence.Impl.CategoryDaoImpl;
import csu.web.mypetstore.persistence.Impl.ItemDaoImpl;
import csu.web.mypetstore.persistence.Impl.ProductDaoImpl;
import csu.web.mypetstore.persistence.ItemDao;
import csu.web.mypetstore.persistence.ProductDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CatalogService {
    private CategoryDao categoryDao;
    private ProductDao productDao;
    private ItemDao itemDao;

    public CatalogService(){
        this.categoryDao = new CategoryDaoImpl();
        this.productDao = new ProductDaoImpl();
        this.itemDao = new ItemDaoImpl();
    }

    // 原有方法保持不变
    public List<Category> getCategoryList() {
        return categoryDao.getCategoryList();
    }

    public Category getCategory(String categoryId) {
        return categoryDao.getCategory(categoryId);
    }

    public Product getProduct(String productId) {
        return productDao.getProduct(productId);
    }

    public List<Product> getProductListByCategory(String categoryId) {
        return productDao.getProductListByCategory(categoryId);
    }

    public List<Product> searchProductList(String keyword) {
        return productDao.searchProductList("%" + keyword.toLowerCase() + "%");
    }

    public List<Item> getItemListByProduct(String productId) {
        return itemDao.getItemListByProduct(productId);
    }

    public Item getItem(String itemId) {
        return itemDao.getItem(itemId);
    }

    public boolean isItemInStock(String itemId) {
        return itemDao.getInventoryQuantity(itemId) > 0;
    }


    // 新增：搜索分类（匹配分类名或描述）
    public List<Category> searchCategories(String keyword) {
        // 转为小写并添加模糊查询通配符，与商品搜索逻辑保持一致
        return categoryDao.searchCategories("%" + keyword.toLowerCase() + "%");
    }

    // 新增：搜索库存项（匹配itemId或商品名）
    public List<Item> searchItems(String keyword) {
        return itemDao.searchItems("%" + keyword.toLowerCase() + "%");
    }

}