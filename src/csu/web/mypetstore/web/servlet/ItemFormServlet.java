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

public class ItemFormServlet extends HttpServlet {

    private CatalogService catalogService;

    private static final String ITEM_FORM = "/WEB-INF/jsp/catalog/item.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemId = req.getParameter("itemId");
        catalogService = new CatalogService();
        Item item = catalogService.getItem(itemId);
        Product product = item.getProduct();

        HttpSession session = req.getSession();
        session.setAttribute("product",product);
        session.setAttribute("item",item);
        // 记录浏览商品详情日志
        LogUtil.recordLog(req, LogUtil.OP_BROWSE_ITEM,
                "浏览商品详情，商品ID：" + itemId + "，商品名称：" + (product != null ? product.getName() : "未知"));
        req.getRequestDispatcher(ITEM_FORM).forward(req,resp);
    }
}
