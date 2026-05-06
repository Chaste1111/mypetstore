package csu.web.mypetstore.service;

import csu.web.mypetstore.domain.Log;
import csu.web.mypetstore.persistence.LogDao;
import csu.web.mypetstore.persistence.Impl.LogDaoImpl;

public class LogService {
    private final LogDao logDao = new LogDaoImpl();

    // 保存日志记录
    public void saveLog(Log log) {
        logDao.insertLog(log);
    }
}