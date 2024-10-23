package com.xiaoqiu.controller;

import com.xiaoqiu.base.RedisPrefix;
import com.xiaoqiu.bo.GetSmsBo;
import com.xiaoqiu.bo.LoginSmsBo;
import com.xiaoqiu.resp.R;
import com.xiaoqiu.service.IPassPortService;
import com.xiaoqiu.vo.UsersVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaoqiu
 */
@RestController
@RequestMapping("/passport")
@Slf4j
public class PassportController extends RedisPrefix {


    @Autowired
    private IPassPortService passPortService;


    @PostMapping("/getSMSCode")
    public R<Void> getSmsCode(@Valid @RequestBody GetSmsBo smsBo, HttpServletRequest request) {
        passPortService.getSmsCode(smsBo, request);
        return R.ok();
    }

    @PostMapping("/login")
    public R<UsersVO> login(@Valid @RequestBody LoginSmsBo loginSmsBo,
                                 HttpServletRequest request) {
        return new R<>(passPortService.login(loginSmsBo, request));
    }

    @PostMapping("/logout")
    public R<Void> logout() {
        /*
            可以加黑名单机制，把token丢到黑名单，校验token前校验黑名单
            但是我偷懒，直接让前端删了token就完事，前端退出到登录页面，后端偷懒
         */
        return R.ok();
    }
}
