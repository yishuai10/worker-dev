package com.xiaoqiu.controller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *
 * @author xiaoqiu
 */
@SpringBootApplication
@MapperScan(basePackages = "com.xiaoqiu.mapper")
@EnableDiscoveryClient
public class WorkApplication {
    public static void main( String[] args ) {
        SpringApplication.run(WorkApplication.class, args);
    }
}
