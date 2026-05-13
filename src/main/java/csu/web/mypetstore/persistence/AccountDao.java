package csu.web.mypetstore.persistence;

import csu.web.mypetstore.domain.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户账户数据访问接口
 * 继承 MyBatis Plus 的 BaseMapper，自动获得基础 CRUD 方法
 * 数据库采用宽表设计，所有用户信息存储在 ACCOUNT 表中
 */
@Mapper
public interface AccountDao extends BaseMapper<Account> {

    /**
     * 根据用户名查询账户（复杂查询，需要自定义SQL）
     */
    Account getAccountByUsername(String username);

    /**
     * 根据用户名和密码查询账户（复杂查询，需要自定义SQL）
     */
    Account getAccountByUsernameAndPassword(Account account);

    /**
     * 插入账户（宽表设计，所有信息都在ACCOUNT表）
     */
    void insertAccount(Account account);

    /**
     * 更新账户信息（宽表设计）
     */
    void updateAccount(Account account);

    /**
     * 更新登录密码
     */
    void updateSignon(Account account);
}