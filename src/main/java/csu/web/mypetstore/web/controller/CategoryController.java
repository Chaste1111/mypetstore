package csu.web.mypetstore.web.controller;

import csu.web.mypetstore.common.Result;
import csu.web.mypetstore.domain.Category;
import csu.web.mypetstore.domain.Product;
import csu.web.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类 RESTful 控制器
 * 提供商品分类查询相关接口
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CatalogService catalogService;

    /**
     * 获取所有分类列表
     * GET /api/categories
     *
     * @return 分类列表
     */
    @GetMapping
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = catalogService.getCategoryList();
        return Result.success(categories);
    }

    /**
     * 根据分类ID获取分类详情
     * GET /api/categories/{categoryId}
     *
     * @param categoryId 分类ID
     * @return 分类详情
     */
    @GetMapping("/{categoryId}")
    public Result<Category> getCategoryById(@PathVariable String categoryId) {
        Category category = catalogService.getCategory(categoryId);
        if (category != null) {
            return Result.success(category);
        }
        return Result.error(404, "分类不存在");
    }

    /**
     * 根据分类ID获取产品列表
     * GET /api/categories/{categoryId}/products
     *
     * @param categoryId 分类ID
     * @return 产品列表
     */
    @GetMapping("/{categoryId}/products")
    public Result<List<Product>> getProductsByCategory(@PathVariable String categoryId) {
        List<Product> products = catalogService.getProductListByCategory(categoryId);
        return Result.success(products);
    }
}