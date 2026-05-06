package csu.web.mypetstore.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * 操作日志实体类
 * 对应数据库表：LOG
 */
@Data
@TableName("LOG")
public class Log {

    /**
     * 日志ID，主键
     */
    @TableId(value = "LOGID", type = IdType.INPUT)
    private String logId;

    /**
     * 操作用户
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 操作类型
     */
    @TableField("OPERATIONTYPE")
    private String operationType;

    /**
     * 操作内容
     */
    @TableField("OPERATIONCONTENT")
    private String operationContent;

    /**
     * 操作时间
     */
    @TableField("OPERATIONTIME")
    private Date operationTime;

    /**
     * IP地址
     */
    @TableField("IPADDRESS")
    private String ipAddress;

    /**
     * 浏览器信息
     */
    @TableField("USERAGENT")
    private String userAgent;

    /**
     * 构造方法：自动生成UUID和操作时间
     */
    public Log() {
        this.logId = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        this.operationTime = new Date();
    }
}