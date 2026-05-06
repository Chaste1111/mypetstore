package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.Item;
import csu.web.mypetstore.domain.Product;
import csu.web.mypetstore.service.CatalogService;
import csu.web.mypetstore.util.LogUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ProductFormServlet extends HttpServlet {

    private CatalogService catalogService;

    private static final String PRODUCT_FORM = "/WEB-INF/jsp/catalog/product.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("productId");

        // 打印接收的productId
        System.out.println("ProductFormServlet received productId: " + productId);

        catalogService = new CatalogService();
        Product product = catalogService.getProduct(productId);
        catalogService.getItemListByProduct(productId);
        List<Item> itemList = catalogService.getItemListByProduct(productId);


        if (product != null) {
            System.out.println("Product found: productId=" + product.getProductId() + ", name=" + product.getName());
        } else {
            System.out.println("Warning: No Product found for productId=" + productId);
        }

        if (itemList != null && !itemList.isEmpty()) {
            System.out.println("Item list found, size=" + itemList.size());
            Item firstItem = itemList.get(0);
            System.out.println("First Item: itemId=" + firstItem.getItemId() + ", listPrice=" + firstItem.getListPrice());
        } else {
            System.out.println("Warning: No Item list found for productId=" + productId);
        }

        HttpSession session = req.getSession();
        session.setAttribute("product", product);
        session.setAttribute("itemList", itemList);
        // 记录浏览商品日志
        LogUtil.recordLog(req, LogUtil.OP_BROWSE_PRODUCT,
                "浏览商品，商品ID：" + productId + "，商品名称：" + (product != null ? product.getName() : "未知"));
        req.getRequestDispatcher(PRODUCT_FORM).forward(req, resp);
    }
}
