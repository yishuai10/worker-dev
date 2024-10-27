package com.xiaoqiu.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author xiaoqiu
 */
@Slf4j
@Component
@Aspect
public class ServiceLogAspect {
    @Around("execution(* com.xiaoqiu.service..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 计时器
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("time-count");
        Object proceed = joinPoint.proceed();
        stopWatch.stop();
        log.info(stopWatch.shortSummary());
        log.info("执行方法：{} 执行时间，耗费了{}毫秒", stopWatch.prettyPrint(), stopWatch.getTotalTimeMillis());
        return proceed;
    }
}