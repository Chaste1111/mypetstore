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
import java.util.List;

public class SearchServlet extends HttpServlet {
    private final CatalogService catalogService = new CatalogService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取搜索关键词（处理中文乱码）
        req.setCharacterEncoding("UTF-8");
        String keyword = req.getParameter("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/mainForm");
            return;
        }
        keyword = keyword.trim();

        // 2. 调用Service层查询三类数据（复用你原有CatalogService的方法）
        List<Category> categories = catalogService.searchCategories(keyword);
        List<Product> products = catalogService.searchProductList(keyword); // 你原有方法
        List<Item> items = catalogService.searchItems(keyword);

        // 3. 把结果存入请求域，转发到结果页面
        req.setAttribute("categories", categories);
        req.setAttribute("products", products);
        req.setAttribute("items", items);
        req.getRequestDispatcher("/WEB-INF/jsp/search/searchResult.jsp").forward(req, resp);
    }

    // 支持POST请求（兼容表单提交）
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}