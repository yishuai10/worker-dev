package com.xiaoqiu.controller;

import com.xiaoqiu.base.RedisPrefix;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xiaoqiu.resp.R;
import com.xiaoqiu.utils.IPUtil;
import com.xiaoqiu.utils.RedisOperator;
import com.xiaoqiu.utils.SMSUtils;

@RestController
@RequestMapping("/passport")
@Slf4j
public class PassportController extends RedisPrefix {

    @Autowired
    private SMSUtils smsUtils;
    @Autowired
    private RedisOperator redisOperator;

    @PostMapping("/getSMSCode")
    public R<Void> getSMSCode(String mobile, HttpServletRequest request) {

        if (StringUtils.isBlank(mobile)) {
            return R.failed("手机号不能为空");
        }

        String userIp = IPUtil.getRequestIp(request);
        // 限制用户只能在60s以内获得一次验证码
        redisOperator.setnx60s(MOBILE_SMSCODE + ":" + userIp, mobile);

        String code = (int)((Math.random() * 9 + 1) * 100000) + "";
        smsUtils.sendSMS(mobile, code);
        log.info("手机号为： {}, 验证码为：{}", mobile, code);

        // 把验证码存入到redis，用于后续的注册登录进行校验
        redisOperator.set(MOBILE_SMSCODE + ":" + mobile, code, 30 * 60);
        return R.ok();
    }
}
