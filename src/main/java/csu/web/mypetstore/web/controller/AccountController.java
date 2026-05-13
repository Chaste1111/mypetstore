package csu.web.mypetstore.web.controller;

import csu.web.mypetstore.common.Result;
import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户账户 RESTful 控制器
 * 提供用户注册、登录、信息管理等接口
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 用户注册
     * POST /api/accounts/register
     *
     * @param account 用户账户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Account> register(@RequestBody Account account) {
        try {
            accountService.registerAccount(account);
            return Result.success("注册成功", account);
        } catch (RuntimeException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 用户登录
     * POST /api/accounts/login
     *
     * @param account 包含用户名和密码的账户对象
     * @return 登录用户信息
     */
    @PostMapping("/login")
    public Result<Account> login(@RequestBody Account account) {
        try {
            Account user = accountService.getAccount(account.getUsername(), account.getPassword());
            if (user != null) {
                return Result.success("登录成功", user);
            }
            return Result.badRequest("用户名或密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("登录失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户名查询用户
     * GET /api/accounts/{username}
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/{username}")
    public Result<Account> getAccountByUsername(@PathVariable String username) {
        Account account = accountService.getAccountByUsername(username);
        if (account != null) {
            return Result.success(account);
        }
        return Result.error(404, "用户不存在");
    }

    /**
     * 检查用户名是否存在（AJAX验证）
     * GET /api/accounts/check-username/{username}
     *
     * @param username 用户名
     * @return 是否存在
     */
    @GetMapping("/check-username/{username}")
    public Result<Boolean> checkUsername(@PathVariable String username) {
        Account account = accountService.getAccountByUsername(username);
        return Result.success(account != null);
    }

    /**
     * 更新用户信息（不含密码）
     * PUT /api/accounts/{username}
     *
     * @param username 用户名
     * @param account  用户更新信息
     * @return 更新结果
     */
    @PutMapping("/{username}")
    public Result<Account> updateAccount(@PathVariable String username, @RequestBody Account account) {
        account.setUsername(username);
        accountService.updateAccountInfo(account);
        return Result.success("更新成功", account);
    }

    /**
     * 更新密码
     * PUT /api/accounts/{username}/password
     *
     * @param username    用户名
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 更新结果
     */
    @PutMapping("/{username}/password")
    public Result<String> updatePassword(
            @PathVariable String username,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        boolean success = accountService.updateSignon(username, oldPassword, newPassword);
        if (success) {
            return Result.success("密码更新成功");
        }
        return Result.badRequest("原密码错误");
    }
}