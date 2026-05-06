package csu.web.mypetstore.web.servlet;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.service.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CheckUsernameServlet extends HttpServlet {
    private AccountService accountService = new AccountService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String username = request.getParameter("username");
        String currentUsername = request.getParameter("currentUsername");

        //判断用户名是否存在
        boolean exists = false;
        if(username !=null && !username.trim().isEmpty()){
            Account account = accountService.getAccountByUsername(username);
            if(account != null){
                if(currentUsername == null || !currentUsername.isEmpty()){
                    //注册场景：查到即存在
                    exists = true;
                }else{
                    //修改场景：排除当前用户名
                    exists = !username.equals(currentUsername);
                }
            }
        }
        PrintWriter out = response.getWriter();
        out.write("{\"exists\":" + exists+ "}");
        out.flush();
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
