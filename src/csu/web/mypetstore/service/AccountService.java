package csu.web.mypetstore.service;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.persistence.AccountDao;
import csu.web.mypetstore.persistence.Impl.AccountDaoImpl;

public class AccountService {

    private AccountDao accountDao;

    public AccountService(){
        this.accountDao = new AccountDaoImpl();
    }

    public Account getAccount(String username,String password){
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return accountDao.getAccountByUsernameAndPassword(account);
    }

    //通过用户名查询账号（AJAX验证使用）
    public Account getAccountByUsername(String username){
        return accountDao.getAccountByUsername(username);
    }

    // 添加注册方法
    public void registerAccount(Account account) {
        // 1. 检查用户名是否已存在
        if (accountDao.getAccountByUsername(account.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        // 2. 插入账户信息（事务处理可根据实际需求添加）
        accountDao.insertAccount(account);
        accountDao.insertProfile(account);
        accountDao.insertSignon(account);
    }

    // 更新用户信息（不含密码）
    public void updateAccountInfo(Account account) {
        accountDao.updateAccount(account);
        accountDao.updateProfile(account);
    }

    // 单独更新密码（需验证原密码）
    public boolean updateSignon(String username, String oldPassword, String newPassword) {
        Account account = getAccount(username, oldPassword); // 验证原密码
        if (account == null) {
            return false; // 原密码错误
        }
        account.setPassword(newPassword);
        accountDao.updateSignon(account); // 调用DAO的updateSignon
        return true;
    }
}


