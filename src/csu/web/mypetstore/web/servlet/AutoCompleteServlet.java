package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.Category;
import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.domain.Product;
import csu.web.mypetstore.service.CatalogService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutoCompleteServlet extends HttpServlet {
    private final CatalogService catalogService = new CatalogService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        String keyword = req.getParameter("keyword");

        if (keyword == null || keyword.trim().isEmpty()) {
            resp.getWriter().write("[]");
            return;
        }
        keyword = keyword.trim();

        Set<String> allSuggestions = new HashSet<>();

        try {
            // 使用与SearchServlet相同的搜索逻辑
            List<Category> categories = catalogService.searchCategories(keyword);
            List<Product> products = catalogService.searchProductList(keyword);
            List<Item> items = catalogService.searchItems(keyword);

            // 1. 分类名 - 只有当关键词长度>=2时才显示分类
            if (keyword.length() >= 2) {
                for (Category c : categories) {
                    if (isValidCategory(c)) {
                        allSuggestions.add("Category：" + c.getName());
                    }
                }
            }

            // 2. 商品名（关联分类名）
            for (Product p : products) {
                if (p != null && p.getName() != null && !p.getName().trim().isEmpty()) {
                    Category productCategory = catalogService.getCategory(p.getCategoryId());
                    if (isValidCategory(productCategory)) {
                        allSuggestions.add(productCategory.getName() + "：" + p.getName());
                    }
                }
            }

            // 3. 库存项商品名（关联分类名）
            for (Item i : items) {
                if (i != null) {
                    Product itemProduct = i.getProduct();
                    if (itemProduct != null && itemProduct.getName() != null && !itemProduct.getName().trim().isEmpty()) {
                        Category itemCategory = catalogService.getCategory(itemProduct.getCategoryId());
                        if (isValidCategory(itemCategory)) {
                            allSuggestions.add(itemCategory.getName() + "：" + itemProduct.getName());
                        }
                    }
                }
            }

            // 转换为List并排序
            List<String> suggestionsList = new ArrayList<>(allSuggestions);

            // 智能排序：让匹配度更高的结果排在前面
            String finalKeyword = keyword;
            suggestionsList.sort((a, b) -> {
                // 提取商品名称部分（冒号后面的部分）
                String aName = a.split("：").length > 1 ? a.split("：")[1].toLowerCase() : a.toLowerCase();
                String bName = b.split("：").length > 1 ? b.split("：")[1].toLowerCase() : b.toLowerCase();
                String keywordLower = finalKeyword.toLowerCase();

                // 优先显示前缀匹配的结果
                boolean aStartsWith = aName.startsWith(keywordLower);
                boolean bStartsWith = bName.startsWith(keywordLower);

                if (aStartsWith && !bStartsWith) {
                    return -1;
                } else if (!aStartsWith && bStartsWith) {
                    return 1;
                }

                // 其次按字母顺序排序
                return a.compareTo(b);
            });

            // 限制数量 - 增加到15个，确保能显示更多结果
            int maxSuggestions = 15;
            if (suggestionsList.size() > maxSuggestions) {
                suggestionsList = suggestionsList.subList(0, maxSuggestions);
            }

            System.out.println("关键词: '" + keyword + "', 返回建议数量: " + suggestionsList.size());

            // 构建JSON
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < suggestionsList.size(); i++) {
                String suggestion = suggestionsList.get(i);
                suggestion = suggestion.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t");
                json.append("\"").append(suggestion).append("\"");
                if (i < suggestionsList.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            resp.getWriter().write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("[]");
        }
    }

    private boolean isValidCategory(Category category) {
        return category != null &&
                category.getName() != null &&
                !category.getName().trim().isEmpty() &&
                !"Unknown".equalsIgnoreCase(category.getName()) &&
                !"未知".equalsIgnoreCase(category.getName());
    }
}