package com.xiaoqiu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoqiu
 */
@RestController
@RequestMapping("/user")
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "user hello";
    }
}
