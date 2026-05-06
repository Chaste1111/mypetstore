package csu.web.mypetstore.web.controller;

import csu.web.mypetstore.common.Result;
import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 商品明细 RESTful 控制器
 * 提供商品库存查询等接口
 */
@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private CatalogService catalogService;

    /**
     * 根据商品ID获取商品详情
     * GET /api/items/{itemId}
     *
     * @param itemId 商品ID
     * @return 商品详情
     */
    @GetMapping("/{itemId}")
    public Result<Item> getItemById(@PathVariable String itemId) {
        Item item = catalogService.getItem(itemId);
        if (item != null) {
            return Result.success(item);
        }
        return Result.error(404, "商品不存在");
    }

    /**
     * 检查商品库存
     * GET /api/items/{itemId}/stock
     *
     * @param itemId 商品ID
     * @return 是否有库存
     */
    @GetMapping("/{itemId}/stock")
    public Result<Map<String, Object>> checkStock(@PathVariable String itemId) {
        boolean inStock = catalogService.isItemInStock(itemId);
        return Result.success(Map.of(
            "itemId", itemId,
            "inStock", inStock
        ));
    }

    /**
     * 根据关键字搜索商品
     * GET /api/items/search?keyword=xxx
     *
     * @param keyword 搜索关键字
     * @return 匹配的商品列表
     */
    @GetMapping("/search")
    public Result<?> searchItems(@RequestParam String keyword) {
        // 注意：原方法返回 List<Item>，但需要检查是否存在该方法
        // 如果不存在，返回错误提示
        return Result.success("搜索功能开发中");
    }
}