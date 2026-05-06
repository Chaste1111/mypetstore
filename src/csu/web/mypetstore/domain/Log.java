package csu.web.mypetstore.domain;

import java.util.Date;
import java.util.UUID;

public class Log {
    private String logId;       // 日志ID
    private String username;    // 操作用户
    private String operationType; // 操作类型
    private String operationContent; // 操作内容
    private Date operationTime; // 操作时间
    private String ipAddress;   // IP地址
    private String userAgent;   // 浏览器信息

    // 构造方法：自动生成UUID和操作时间
    public Log() {
        this.logId = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        this.operationTime = new Date();
    }

    // Getter和Setter方法
    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getOperationContent() { return operationContent; }
    public void setOperationContent(String operationContent) { this.operationContent = operationContent; }
    public Date getOperationTime() { return operationTime; }
    public void setOperationTime(Date operationTime) { this.operationTime = operationTime; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
}