package com.xiaoqiu.resp;

import com.xiaoqiu.common.HttpStatusEnum;
import lombok.Getter;

/**
 * @author xiaoqiu
 */
@Getter
public class R<T> {
    /**
     * 标识返回状态
     */
    private Integer code;

    /**
     * 标识返回是否成功
     */
    private Boolean success;

    /**
     * 标识返回消息
     */
    private String message;

    /**
     * 标识返回内容
     */
    private T data;

    public R() {

    }

    public R(T data) {
        this.code = HttpStatusEnum.SUCCESS.getCode();
        this.success = true;
        this.message = HttpStatusEnum.SUCCESS.getMessage();
        this.data = data;
    }

    public R(Integer code, Boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回
     */
    public static <T> R<T> ok() {
        return new R<>(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回
     */
    public static <T> R<T> ok(T data) {
        return new R<>(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回
     */
    public static <T> R<T> ok(T data, String message) {
        return new R<>(HttpStatusEnum.SUCCESS.getCode(), true, message, data);
    }

    /**
     * 失败返回
     */
    public static <T> R<T> failed(Integer code, String message) {
        return new R<>(code, false, message, null);
    }

    /**
     * 失败返回
     */
    public static <T> R<T> failed(HttpStatusEnum httpStatusEnum) {
        return new R<>(httpStatusEnum.getCode(), false, httpStatusEnum.getMessage(), null);
    }

    /**
     * 失败返回
     */
    public static <T> R<T> failed(String message) {
        return new R<>(HttpStatusEnum.FAIL.getCode(), false, message, null);
    }

    /**
     * 失败返回
     */
    public static <T> R<T> failed(T data) {
        return new R<>(HttpStatusEnum.FAIL.getCode(), false, HttpStatusEnum.FAIL.getMessage(), data);
    }

    /**
     * 失败返回
     */
    public static <T> R<T> failed(String msg, T data) {
        return new R<>(HttpStatusEnum.FAIL.getCode(), false, msg, data);
    }
}