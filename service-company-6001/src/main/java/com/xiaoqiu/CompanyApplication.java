package com.xiaoqiu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan(basePackages = "com.xiaoqiu.mapper")
@EnableDiscoveryClient
public class CompanyApplication {
    public static void main( String[] args ) {
        SpringApplication.run(CompanyApplication.class, args);
    }
}
