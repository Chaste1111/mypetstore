package csu.web.mypetstore.persistence;

import csu.web.mypetstore.domain.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志数据访问接口
 * 继承 MyBatis Plus 的 BaseMapper，自动获得基础 CRUD 方法
 */
@Mapper
public interface LogDao extends BaseMapper<Log> {

    /**
     * 插入日志记录
     */
    void insertLog(Log log);
}