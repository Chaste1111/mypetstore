package csu.web.mypetstore.persistence.Impl;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.persistence.AccountDao;
import csu.web.mypetstore.persistence.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDaoImpl implements AccountDao {

    private static final String UPDATE_ACCOUNT = "UPDATE ACCOUNT SET EMAIL=?, FIRSTNAME=?, LASTNAME=?, ADDR1=?, ADDR2=?, CITY=?, STATE=?, ZIP=?, COUNTRY=?, PHONE=? WHERE USERID=?";

    private static final String UPDATE_PROFILE = "UPDATE PROFILE SET LANGPREF=?, FAVCATEGORY=?, MYLISTOPT=?, BANNEROPT=? WHERE USERID=?";

    private static final String UPDATE_SIGNON = "UPDATE signon SET password = ? WHERE username = ?";

    private static final String INSERT_ACCOUNT = "INSERT INTO ACCOUNT (USERID, EMAIL, FIRSTNAME, LASTNAME, STATUS, ADDR1, ADDR2, CITY, STATE, ZIP, COUNTRY, PHONE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_PROFILE = "INSERT INTO PROFILE (USERID, LANGPREF, FAVCATEGORY, MYLISTOPT, BANNEROPT) VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_SIGNON = "INSERT INTO SIGNON (USERNAME, PASSWORD) VALUES (?, ?)";

    private static final String GET_ACCOUNT_BY_USERNAME_AND_PASSWORD = "SELECT " +
                    "SIGNON.USERNAME, " +
                    "ACCOUNT.EMAIL, " +
                    "ACCOUNT.FIRSTNAME, " +
                    "ACCOUNT.LASTNAME, " +
                    "ACCOUNT.STATUS, " +
                    "ACCOUNT.ADDR1 AS address1, " +
                    "ACCOUNT.ADDR2 AS address2, " +
                    "ACCOUNT.CITY, " +
                    "ACCOUNT.STATE, " +
                    "ACCOUNT.ZIP, " +
                    "ACCOUNT.COUNTRY, " +
                    "ACCOUNT.PHONE, " +
                    "PROFILE.LANGPREF AS languagePreference, " +
                    "PROFILE.FAVCATEGORY AS favoriteCategoryId, " +
                    "PROFILE.MYLISTOPT AS listOption, " +
                    "PROFILE.BANNEROPT AS bannerOption, " +
                    "BANNERDATA.BANNERNAME " +
                    "FROM ACCOUNT, PROFILE, SIGNON, BANNERDATA " +
                    "WHERE ACCOUNT.USERID = ? " +
                    "AND SIGNON.PASSWORD = ? " +
                    "AND SIGNON.USERNAME = ACCOUNT.USERID " +
                    "AND PROFILE.USERID = ACCOUNT.USERID " +
                    "AND PROFILE.FAVCATEGORY = BANNERDATA.FAVCATEGORY";
    @Override
    public Account getAccountByUsername(String username) {
        Account account = null;
        String sql = "SELECT USERID FROM ACCOUNT WHERE USERID = ?";
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                account = new Account();
                account.setUsername(rs.getString("USERID"));
            }
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(pstmt);
            DBUtil.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public Account getAccountByUsernameAndPassword(Account account) {
        Account accountResult = null;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_USERNAME_AND_PASSWORD);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                accountResult = this.resultSetToAccount(resultSet);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountResult;
    }

    private Account resultSetToAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setUsername(resultSet.getString("USERNAME"));
        account.setEmail(resultSet.getString("EMAIL"));
        account.setFirstName(resultSet.getString("FIRSTNAME"));
        account.setLastName(resultSet.getString("LASTNAME"));
        account.setStatus(resultSet.getString("STATUS"));
        account.setAddress1(resultSet.getString("address1"));
        account.setAddress2(resultSet.getString("address2"));
        account.setCity(resultSet.getString("CITY"));
        account.setState(resultSet.getString("STATE"));
        account.setZip(resultSet.getString("ZIP"));
        account.setCountry(resultSet.getString("COUNTRY"));
        account.setPhone(resultSet.getString("PHONE"));
        account.setLanguagePreference(resultSet.getString("languagePreference"));
        account.setFavoriteCategoryId(resultSet.getString("favoriteCategoryId"));
        account.setListOption(resultSet.getBoolean("listOption"));
        account.setBannerOption(resultSet.getBoolean("bannerOption"));
        account.setBannerName(resultSet.getString("BANNERNAME"));
        return account;
    }


    @Override
    public void insertAccount(Account account) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(INSERT_ACCOUNT);
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getEmail());
            pstmt.setString(3, account.getFirstName());
            pstmt.setString(4, account.getLastName());
            pstmt.setString(5, account.getStatus());
            pstmt.setString(6, account.getAddress1());
            pstmt.setString(7, account.getAddress2());
            pstmt.setString(8, account.getCity());
            pstmt.setString(9, account.getState());
            pstmt.setString(10, account.getZip());
            pstmt.setString(11, account.getCountry());
            pstmt.setString(12, account.getPhone());
            pstmt.executeUpdate();

            DBUtil.closePreparedStatement(pstmt);
            DBUtil.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertProfile(Account account) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(INSERT_PROFILE);
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getLanguagePreference());
            pstmt.setString(3, account.getFavoriteCategoryId());
            pstmt.setBoolean(4, account.isListOption());
            pstmt.setBoolean(5, account.isBannerOption());
            pstmt.executeUpdate();

            DBUtil.closePreparedStatement(pstmt);
            DBUtil.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertSignon(Account account) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(INSERT_SIGNON);
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            pstmt.executeUpdate();

            DBUtil.closePreparedStatement(pstmt);
            DBUtil.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAccount(Account account) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 关闭自动提交

            PreparedStatement pstmt = conn.prepareStatement(UPDATE_ACCOUNT);
            pstmt.setString(1, account.getEmail());
            pstmt.setString(2, account.getFirstName());
            pstmt.setString(3, account.getLastName());
            pstmt.setString(4, account.getAddress1());
            pstmt.setString(5, account.getAddress2());
            pstmt.setString(6, account.getCity());
            pstmt.setString(7, account.getState());
            pstmt.setString(8, account.getZip());
            pstmt.setString(9, account.getCountry());
            pstmt.setString(10, account.getPhone());
            pstmt.setString(11, account.getUsername());
            pstmt.executeUpdate();

            conn.commit(); // 提交事务
            DBUtil.closePreparedStatement(pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // 异常回滚
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                DBUtil.closeConnection(conn);
            }
        }
    }

    @Override
    public void updateProfile(Account account) {
        Connection conn = null;
        try {
            // 获取数据库连接
            conn = DBUtil.getConnection();
            // 关闭自动提交，手动管理事务
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(UPDATE_PROFILE);
            pstmt.setString(1, account.getLanguagePreference());
            pstmt.setString(2, account.getFavoriteCategoryId());
            pstmt.setBoolean(3, account.isListOption());
            pstmt.setBoolean(4, account.isBannerOption());
            pstmt.setString(5, account.getUsername());

            // 执行更新并获取影响行数（用于调试）
            int rows = pstmt.executeUpdate();
            System.out.println("updateProfile 影响行数：" + rows);

            // 手动提交事务（关键：确保修改写入数据库）
            conn.commit();

            // 关闭PreparedStatement
            DBUtil.closePreparedStatement(pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
            // 发生异常时回滚事务，避免部分更新导致数据不一致
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("updateProfile 事务回滚");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // 确保连接最终关闭
            if (conn != null) {
                DBUtil.closeConnection(conn);
            }
        }
    }

    @Override
    public void updateSignon(Account account) {
        System.out.println("开始执行 updateSignon，用户名：" + account.getUsername() + "，新密码：" + account.getPassword());
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            // 关闭自动提交，手动控制事务
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(UPDATE_SIGNON);
            pstmt.setString(1, account.getPassword());
            pstmt.setString(2, account.getUsername());
            int rows = pstmt.executeUpdate();
            System.out.println("updateSignon 影响行数：" + rows);

            // 手动提交事务
            conn.commit();

            DBUtil.closePreparedStatement(pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
            // 发生异常时回滚事务
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // 关闭连接
            if (conn != null) {
                DBUtil.closeConnection(conn);
            }
        }
    }

}
