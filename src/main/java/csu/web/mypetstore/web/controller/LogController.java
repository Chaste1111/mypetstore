package csu.web.mypetstore.web.controller;

import csu.web.mypetstore.common.Result;
import csu.web.mypetstore.domain.Log;
import csu.web.mypetstore.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 日志 RESTful 控制器
 * 提供日志记录相关接口
 */
@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 记录操作日志
     * POST /api/logs
     *
     * @param log 日志信息
     * @return 记录结果
     */
    @PostMapping
    public Result<String> saveLog(@RequestBody Log log) {
        logService.saveLog(log);
        return Result.success("日志记录成功");
    }
}