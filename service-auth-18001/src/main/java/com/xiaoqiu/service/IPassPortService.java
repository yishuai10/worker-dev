package com.xiaoqiu.service;

import com.xiaoqiu.bo.GetSmsBo;
import com.xiaoqiu.bo.LoginSmsBo;
import com.xiaoqiu.vo.UsersVO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author xiaoqiu
 */
public interface IPassPortService {

    /**
     * 获取短信验证码
     * @param smsBo 手机号登信息
     * @param request servlet请求
     */
    void getSmsCode(GetSmsBo smsBo, HttpServletRequest request);

    /**
     * 登录
     * @param loginSmsBo 登录信息
     * @param request servlet请求
     * @return 用户信息
     */
    UsersVO login(LoginSmsBo loginSmsBo, HttpServletRequest request);
}
