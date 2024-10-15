package com.xiaoqiu.service;

import com.xiaoqiu.pojo.Interview;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 面试邀约表
本表为次表，可做冗余，可以用mongo或者es替代 服务类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-15
 */
public interface InterviewService extends IService<Interview> {

}
