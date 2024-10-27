package com.xiaoqiu.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoqiu
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Auth Service~~~";
    }

}
