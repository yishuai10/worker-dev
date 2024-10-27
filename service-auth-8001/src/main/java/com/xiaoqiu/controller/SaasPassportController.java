package com.xiaoqiu.controller;


import com.xiaoqiu.resp.R;
import com.xiaoqiu.service.ISaasPassPortService;
import com.xiaoqiu.vo.SaasUserVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 企业登录
 * @author xiaoqiu
 */
@RestController
@RequestMapping("/saas")
@Slf4j
public class SaasPassportController {

    @Autowired
    private ISaasPassPortService saasPassPortService;

    /**
     * 获得二维码token令牌
     */
    @PostMapping("/getQRToken")
    public R<String> getQrToken() {
        return R.ok(saasPassPortService.getQrToken());
    }

    /**
     * 2. 手机端使用HR角色进行扫码操作
     */
    @PostMapping("/scanCode")
    public R<String> scanCode(String qrToken, HttpServletRequest request) {
        return R.ok(saasPassPortService.scanCode(qrToken, request));
    }

    /**
     * 3. SAAS网页端每隔一段时间（3秒）定时查询qrToken是否被读取，用于页面的展示标记判断
     * 前端处理：限制用户在页面不操作而频繁发起调用：【页面失效，请刷新后再执行扫描登录！】
     * 注：如果使用websocket或者netty，可以在app扫描之后，在上一个接口，直接通信浏览器（H5）进行页面扫码的状态标记
     */
    @PostMapping("/codeHasBeenRead")
    public R<List<Object>> codeHasBeenRead(String qrToken) {
        return R.ok(saasPassPortService.codeHasBeenRead(qrToken));
    }

    /**
     * 4. 手机端点击却登录，携带preToken与后端进行判断，如果校验ok则登录成功
     * 注：如果使用websocket或者netty，可以在此直接通信H5进行页面的跳转
     */
    @PostMapping("goQRLogin")
    public R<Void> goQrLogin(String userId, String qrToken, String preToken) {
        saasPassPortService.goQrLogin(userId, qrToken, preToken);
        return R.ok();
    }

    /**
     * 5. 页面登录跳转
     */
    @PostMapping("checkLogin")
    public R<String> checkLogin(String preToken) {
        return R.ok(saasPassPortService.checkLogin(preToken));
    }

    @GetMapping("info")
    public R<SaasUserVO> info(String token) {
        return R.ok(saasPassPortService.info(token));
    }

    @PostMapping("logout")
    public R<Void> logout(String token) {
        return R.ok();
    }
}
