package csu.web.mypetstore.persistence;

import csu.web.mypetstore.domain.Log;

public interface LogDao {
    // 插入日志记录
    void insertLog(Log log);
}