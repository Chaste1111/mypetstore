package csu.web.mypetstore.service;

import csu.web.mypetstore.domain.Account;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户账户服务接口
 * 继承 MyBatis Plus 的 IService，自动获得基础 CRUD 方法
 */
public interface AccountService extends IService<Account> {

    /**
     * 根据用户名和密码查询账户
     * @param username 用户名
     * @param password 密码
     * @return 账户对象，如果不存在返回 null
     */
    Account getAccount(String username, String password);

    /**
     * 通过用户名查询账号（AJAX验证使用）
     * @param username 用户名
     * @return 账户对象，如果不存在返回 null
     */
    Account getAccountByUsername(String username);

    /**
     * 用户注册
     * @param account 账户对象
     * @throws RuntimeException 如果用户名已存在
     */
    void registerAccount(Account account);

    /**
     * 更新用户信息（不含密码）
     * @param account 账户对象
     */
    void updateAccountInfo(Account account);

    /**
     * 单独更新密码（需验证原密码）
     * @param username 用户名
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 更新是否成功
     */
    boolean updateSignon(String username, String oldPassword, String newPassword);
}