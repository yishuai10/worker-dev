package com.xiaoqiu.config;

import com.xiaoqiu.intercept.SmsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xiaoqiu
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 在springboot容器中放入拦截器
     */
    @Bean
    public SmsInterceptor smsInterceptor() {
        return new SmsInterceptor();
    }

    /**
     * 注册拦截器，并且拦截指定的路由，否则不生效
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(smsInterceptor())
                .addPathPatterns("/passport/getSMSCode");
    }
}
