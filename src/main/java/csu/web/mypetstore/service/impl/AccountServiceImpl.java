package csu.web.mypetstore.service.impl;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.persistence.AccountDao;
import csu.web.mypetstore.service.AccountService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户账户服务实现类
 * 继承 MyBatis Plus 的 ServiceImpl，自动获得基础 CRUD 方法
 * 数据库采用宽表设计，所有用户信息存储在 ACCOUNT 表中
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountDao, Account> implements AccountService {

    @Autowired
    private AccountDao accountDao;

    /**
     * 根据用户名和密码查询账户
     */
    @Override
    public Account getAccount(String username, String password) {
        return accountDao.getAccountByUsernameAndPassword(
            Account.builder().username(username).password(password).build()
        );
    }

    /**
     * 通过用户名查询账号（AJAX验证使用）
     * 使用 LambdaQueryWrapper 提高代码可读性
     */
    @Override
    public Account getAccountByUsername(String username) {
        // 使用 LambdaQueryWrapper 构建查询条件，类型安全且可读性高
        return baseMapper.selectOne(
            new LambdaQueryWrapper<Account>()
                .eq(Account::getUsername, username)
        );
    }

    /**
     * 用户注册
     * 使用 @Transactional 保证事务一致性
     * 宽表设计：所有信息都在ACCOUNT表中
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerAccount(Account account) {
        // 1. 使用 LambdaQueryWrapper 检查用户名是否已存在
        Account existingAccount = baseMapper.selectOne(
            new LambdaQueryWrapper<Account>()
                .eq(Account::getUsername, account.getUsername())
        );
        
        if (existingAccount != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 2. 插入账户信息（宽表设计，只有ACCOUNT表）
        accountDao.insertAccount(account);
    }

    /**
     * 更新用户信息（不含密码）
     * 使用 @Transactional 保证事务一致性
     * 宽表设计：只有ACCOUNT表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAccountInfo(Account account) {
        accountDao.updateAccount(account);
    }

    /**
     * 单独更新密码（需验证原密码）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSignon(String username, String oldPassword, String newPassword) {
        // 验证原密码
        Account account = getAccount(username, oldPassword);
        if (account == null) {
            return false; // 原密码错误
        }
        
        // 更新密码
        Account updateAccount = new Account();
        updateAccount.setUsername(username);
        updateAccount.setPassword(newPassword);
        accountDao.updateSignon(updateAccount);
        return true;
    }
}