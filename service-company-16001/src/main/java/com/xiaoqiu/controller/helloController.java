package com.xiaoqiu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class helloController {

    @GetMapping("/hello")
    public String hello(){
        return "user company";
    }
}
