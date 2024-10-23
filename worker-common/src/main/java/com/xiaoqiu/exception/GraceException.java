package com.xiaoqiu.exception;

import com.xiaoqiu.resp.ResponseStatusEnum;

/**
 * 优雅的处理异常，统一进行封装
 */
public class GraceException {

    public static void display(ResponseStatusEnum responseStatusEnum) {
        throw new XiaoQiuException(responseStatusEnum);
    }
}
