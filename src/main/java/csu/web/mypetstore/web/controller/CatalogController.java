package csu.web.mypetstore.web.controller;

import csu.web.mypetstore.common.Result;
import csu.web.mypetstore.domain.Category;
import csu.web.mypetstore.domain.Product;
import csu.web.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品目录 RESTful 控制器
 * 提供商品分类和产品查询相关接口
 */
@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    /**
     * 获取商品列表
     * GET /api/catalog/products
     *
     * @return 产品列表
     */
    @GetMapping("/products")
    public Result<List<Product>> getAllProducts() {
        List<Product> products = catalogService.list();
        return Result.success(products);
    }

    /**
     * 获取所有分类列表
     * GET /api/catalog/categories
     *
     * @return 分类列表
     */
    @GetMapping("/categories")
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = catalogService.getCategoryList();
        return Result.success(categories);
    }

    /**
     * 根据分类ID获取产品列表
     * GET /api/catalog/categories/{categoryId}/products
     *
     * @param categoryId 分类ID
     * @return 产品列表
     */
    @GetMapping("/categories/{categoryId}/products")
    public Result<List<Product>> getProductsByCategory(@PathVariable String categoryId) {
        List<Product> products = catalogService.getProductListByCategory(categoryId);
        return Result.success(products);
    }
}