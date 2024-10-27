package com.xiaoqiu.controller;

import com.xiaoqiu.bo.AdminBO;
import com.xiaoqiu.resp.R;
import com.xiaoqiu.service.IAdminService;
import com.xiaoqiu.utils.JwtUtils;
import com.xiaoqiu.vo.AdminVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaoqiu
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("login")
    public R<String> login(@Valid @RequestBody AdminBO adminBO) {
        return R.ok(adminService.login(adminBO));
    }

    @GetMapping("info")
    public R<AdminVO> info(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return R.ok(adminService.info(token));
    }

    @PostMapping("logout")
    public R<Void> logout() {
        return R.ok();
    }
}