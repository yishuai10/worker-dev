package com.xiaoqiu.exception;


import com.xiaoqiu.resp.ResponseStatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 * @author xiaoqiu
 */
@Setter
@Getter
public class XiaoQiuException extends RuntimeException{

    private ResponseStatusEnum responseStatusEnum;

    public XiaoQiuException(ResponseStatusEnum responseStatusEnum) {
        super("状态码：" + responseStatusEnum.status() + "，信息：" + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }

}
