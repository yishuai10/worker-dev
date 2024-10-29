package com.xiaoqiu.client;


import com.xiaoqiu.resp.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 告诉springCloud容器，本接口为调用远程服务的service（声明式客户端远程调用）
 * @author xiaoqiu
 */
@FeignClient("work-service")
public interface WorkResumeServiceFeign {

    /**
     * 初始化简历
     * @param userId 用户Id
     * @return 状态
     */
    @PostMapping("/resume/init")
    public R<Void> init(@RequestParam("userId") String userId);

}
