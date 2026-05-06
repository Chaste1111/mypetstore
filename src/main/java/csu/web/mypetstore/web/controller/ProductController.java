package csu.web.mypetstore.web.controller;

import csu.web.mypetstore.common.Result;
import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.domain.Product;
import csu.web.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品 RESTful 控制器
 * 提供产品查询、搜索等接口
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private CatalogService catalogService;

    /**
     * 获取所有产品列表
     * GET /api/products
     *
     * @return 产品列表
     */
    @GetMapping
    public Result<List<Product>> getAllProducts() {
        List<Product> products = catalogService.list();
        return Result.success(products);
    }

    /**
     * 根据产品ID获取产品详情
     * GET /api/products/{productId}
     *
     * @param productId 产品ID
     * @return 产品详情
     */
    @GetMapping("/{productId}")
    public Result<Product> getProductById(@PathVariable String productId) {
        Product product = catalogService.getProduct(productId);
        if (product != null) {
            return Result.success(product);
        }
        return Result.error(404, "产品不存在");
    }

    /**
     * 根据产品ID获取商品列表
     * GET /api/products/{productId}/items
     *
     * @param productId 产品ID
     * @return 商品列表
     */
    @GetMapping("/{productId}/items")
    public Result<List<Item>> getItemsByProduct(@PathVariable String productId) {
        List<Item> items = catalogService.getItemListByProduct(productId);
        return Result.success(items);
    }

    /**
     * 搜索产品
     * GET /api/products/search?keyword=xxx
     *
     * @param keyword 搜索关键字
     * @return 匹配的产品列表
     */
    @GetMapping("/search")
    public Result<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = catalogService.searchProductList(keyword);
        return Result.success(products);
    }

    /**
     * 产品名称自动补全
     * GET /api/products/autocomplete?keyword=xxx
     *
     * @param keyword 关键字
     * @return 匹配的产品名称列表
     */
    @GetMapping("/autocomplete")
    public Result<List<String>> autocomplete(@RequestParam String keyword) {
        List<String> names = catalogService.autoCompleteProductName(keyword);
        return Result.success(names);
    }
}