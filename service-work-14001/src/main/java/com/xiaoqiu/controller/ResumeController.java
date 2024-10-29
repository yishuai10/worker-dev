package com.xiaoqiu.controller;

import com.xiaoqiu.resp.R;
import com.xiaoqiu.service.IResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaoqiu
 */
@Slf4j
@RestController
@RequestMapping("resume")
public class ResumeController {

    @Autowired
    private IResumeService resumeService;

    @Value("${server.port}")
    private String port;

    /**
     * 测试负载均衡
     */
    @GetMapping("test")
    public R<String> test() {
        return R.ok(port);
    }

    /**
     * 初始化用户简历
     */
    @PostMapping("init")
    public R<Void> init(@RequestParam("userId") String userId) {
        resumeService.initResume(userId);
        return R.ok();
    }

}
