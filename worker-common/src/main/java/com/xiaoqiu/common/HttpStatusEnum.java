package com.xiaoqiu.common;

import lombok.Getter;

@Getter
public enum HttpStatusEnum {
    SUCCESS(200, "成功"),
    FAIL(500, "失败");

    public final int code;
    public final String message;

    HttpStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
