package com.xiaoqiu.common;

import lombok.Getter;

/**
 * 对外展示名称枚举
 * @author xiaoqiu
 */
@Getter
public enum ShowWhichName {
    NICKNAME(1,"昵称"),
    REAL_NAME(2,"真实姓名");

    private final Integer code;
    private final String desc;

    ShowWhichName(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
