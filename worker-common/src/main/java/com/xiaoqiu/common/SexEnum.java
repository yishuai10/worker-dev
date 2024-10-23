package com.xiaoqiu.common;

import lombok.Getter;

/**
 *  性别枚举
 *  @Author: xiaoqiu
 */
@Getter
public enum SexEnum {

    MEN(1, "男"),
    WOMEN(2, "女"),
    UNKNOWN(3, "未知");

    private final int code;
    private final String desc;

    private SexEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
