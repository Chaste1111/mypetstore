package csu.web.mypetstore.persistence.Impl;

import csu.web.mypetstore.domain.Log;
import csu.web.mypetstore.persistence.DBUtil;
import csu.web.mypetstore.persistence.LogDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class LogDaoImpl implements LogDao {
    // 插入日志SQL
    private static final String INSERT_LOG = "INSERT INTO user_operation_log " +
            "(log_id, username, operation_type, operation_content, operation_time, ip_address, user_agent) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void insertLog(Log log) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_LOG)) {
            pstmt.setString(1, log.getLogId());
            pstmt.setString(2, log.getUsername());
            pstmt.setString(3, log.getOperationType());
            pstmt.setString(4, log.getOperationContent());
            pstmt.setTimestamp(5, new Timestamp(log.getOperationTime().getTime()));
            pstmt.setString(6, log.getIpAddress());
            pstmt.setString(7, log.getUserAgent());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}