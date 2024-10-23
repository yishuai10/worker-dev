package com.xiaoqiu.intercept;

import com.xiaoqiu.base.RedisPrefix;
import com.xiaoqiu.exception.GraceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import com.xiaoqiu.common.ResponseStatusEnum;
import com.xiaoqiu.utils.IPUtil;
import com.xiaoqiu.utils.RedisOperator;


@Slf4j
public class SMSInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        // 获得用户ip
        String userIp = IPUtil.getRequestIp(request);
        boolean ipExist = redisOperator.keyIsExist(RedisPrefix.MOBILE_SMSCODE + ":" + userIp);
        if (ipExist) {
            log.error("短信发送频率太高了！");
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }

        return true;
    }
}
