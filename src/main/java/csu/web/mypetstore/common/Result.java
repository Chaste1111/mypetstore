package csu.web.mypetstore.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应类
 * 用于封装 RESTful API 的响应数据
 *
 * @param <T> 响应数据的泛型类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /**
     * 状态码
     * 200 - 成功
     * 400 - 请求参数错误
     * 401 - 未认证（未登录）
     * 403 - 未授权（无权限）
     * 500 - 服务器内部错误
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private long timestamp;

    /**
     * 创建成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null, System.currentTimeMillis());
    }

    /**
     * 创建成功响应（带数据）
     *
     * @param data 响应数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data, System.currentTimeMillis());
    }

    /**
     * 创建成功响应（带数据和消息）
     *
     * @param message 响应消息
     * @param data    响应数据
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data, System.currentTimeMillis());
    }

    /**
     * 创建失败响应
     *
     * @param message 错误消息
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null, System.currentTimeMillis());
    }

    /**
     * 创建失败响应（带状态码）
     *
     * @param code    状态码
     * @param message 错误消息
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null, System.currentTimeMillis());
    }

    /**
     * 创建参数错误响应
     *
     * @param message 错误消息
     */
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null, System.currentTimeMillis());
    }

    /**
     * 创建未认证响应
     */
    public static <T> Result<T> unauthorized() {
        return new Result<>(401, "未登录，请先登录", null, System.currentTimeMillis());
    }

    /**
     * 创建未授权响应
     */
    public static <T> Result<T> forbidden() {
        return new Result<>(403, "无权限访问", null, System.currentTimeMillis());
    }

    /**
     * 创建自定义响应
     *
     * @param code    状态码
     * @param message 响应消息
     * @param data    响应数据
     */
    public static <T> Result<T> of(int code, String message, T data) {
        return new Result<>(code, message, data, System.currentTimeMillis());
    }
}