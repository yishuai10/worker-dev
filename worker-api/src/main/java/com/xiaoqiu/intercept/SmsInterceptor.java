package com.xiaoqiu.intercept;

import com.xiaoqiu.base.Constant;
import com.xiaoqiu.exception.GraceException;
import com.xiaoqiu.resp.ResponseStatusEnum;
import com.xiaoqiu.utils.IPUtil;
import com.xiaoqiu.utils.RedisOperator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * @author xiaoqiu
 */
@Slf4j
public class SmsInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        // 获得用户ip
        String userIp = IPUtil.getRequestIp(request);
        boolean ipExist = redisOperator.keyIsExist(Constant.MOBILE_SMSCODE + ":" + userIp);
        if (ipExist) {
            log.error("短信发送频率太高了！");
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }

        return true;
    }
}
