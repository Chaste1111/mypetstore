package csu.web.mypetstore.service;

import csu.web.mypetstore.domain.Log;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 日志服务接口
 * 继承 MyBatis Plus 的 IService，自动获得基础 CRUD 方法
 */
public interface LogService extends IService<Log> {

    /**
     * 保存日志记录
     * @param log 日志对象
     */
    void saveLog(Log log);
}