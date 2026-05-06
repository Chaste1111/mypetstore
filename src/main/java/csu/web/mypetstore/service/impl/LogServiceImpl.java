package csu.web.mypetstore.service.impl;

import csu.web.mypetstore.domain.Log;
import csu.web.mypetstore.persistence.LogDao;
import csu.web.mypetstore.service.LogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 日志服务实现类
 * 继承 MyBatis Plus 的 ServiceImpl，自动获得基础 CRUD 方法
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogDao, Log> implements LogService {

    /**
     * 保存日志记录
     * 使用 MyBatis Plus 内置的 save 方法
     */
    @Override
    public void saveLog(Log log) {
        // 使用 MyBatis Plus 内置的 save 方法
        baseMapper.insert(log);
    }
}