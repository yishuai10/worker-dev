package com.xiaoqiu.service;

import com.xiaoqiu.vo.SaasUserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author xiaoqiu
 */
public interface ISaasPassPortService {

    /**
     * 获得二维码token令牌
     */
    String getQrToken();

    /**
     * 扫码登录
     */
    String scanCode(String qrToken, HttpServletRequest request);

    /**
     * 判断二维码是否被扫描
     */
    List<Object> codeHasBeenRead(String qrToken);

    /**
     * 扫码登录跳转
     */
    void goQrLogin(String userId, String qrToken, String preToken);

    /**
     * 页面扫码登录校验
     */
    String checkLogin(String preToken);

    /**
     * 用户信息
     */
    SaasUserVO info(String token);
}
