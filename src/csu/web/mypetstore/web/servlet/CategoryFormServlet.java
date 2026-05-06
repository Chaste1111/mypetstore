package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.Category;
import csu.web.mypetstore.domain.Product;
import csu.web.mypetstore.service.CatalogService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class CategoryFormServlet extends HttpServlet{

    private CatalogService catalogService;

    private static final String CATEGORY_FORM = "/WEB-INF/jsp/catalog/category.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryId = req.getParameter("categoryId");
        // 日志1：验证获取的categoryId是否正确
        //System.out.println("ServletcategoryId:" + categoryId);

        catalogService = new CatalogService();
        Category category = catalogService.getCategory(categoryId);
        List<Product> productList = catalogService.getProductListByCategory(categoryId);
        // 日志2：验证服务层返回的productList数据
        //System.out.println("size:" + productList.size());
        for(Product p : productList){
            //System.out.println("productId=" + p.getProductId() + ", name=" + p.getName());
        }

        HttpSession session = req.getSession();
        session.setAttribute("category", category);
        session.setAttribute("productList", productList);
        // 日志3：验证Session存储后的数据
        //System.out.println("SessionproductListsize:" + ((List<Product>)session.getAttribute("productList")).size());

        req.getRequestDispatcher(CATEGORY_FORM).forward(req, resp);
    }
}
