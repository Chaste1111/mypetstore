package csu.web.mypetstore.util;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.domain.Log;
import csu.web.mypetstore.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 日志工具类
 * 使用 Spring 依赖注入获取 LogService
 */
@Component
public class LogUtil {

    private static LogService logService;

    @Autowired
    public void setLogService(LogService logService) {
        LogUtil.logService = logService;
    }

    /**
     * 通用日志记录方法
     *
     * @param request           HTTP请求对象
     * @param operationType     操作类型
     * @param operationContent  操作内容
     */
    public static void recordLog(HttpServletRequest request, String operationType, String operationContent) {
        if (logService == null) {
            return; // LogService 未注入，跳过日志记录
        }

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            return; // 未登录用户不记录日志
        }

        Log log = new Log();
        log.setUsername(account.getUsername());
        log.setOperationType(operationType);
        log.setOperationContent(operationContent);
        log.setIpAddress(getIpAddress(request));
        log.setUserAgent(request.getHeader("User-Agent"));

        logService.saveLog(log);
    }

    /**
     * 获取用户真实IP地址
     *
     * @param request HTTP请求对象
     * @return IP地址
     */
    private static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    // 常用操作类型常量
    public static final String OP_LOGIN = "LOGIN";
    public static final String OP_BROWSE_PRODUCT = "BROWSE_PRODUCT";
    public static final String OP_BROWSE_ITEM = "BROWSE_ITEM";
    public static final String OP_ADD_CART = "ADD_CART";
    public static final String OP_REMOVE_CART = "REMOVE_CART";
    public static final String OP_UPDATE_CART = "UPDATE_CART";
    public static final String OP_PLACE_ORDER = "PLACE_ORDER";
    public static final String OP_VIEW_ORDER = "VIEW_ORDER";
}