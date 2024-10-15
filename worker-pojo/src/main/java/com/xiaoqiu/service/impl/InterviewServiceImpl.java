package com.xiaoqiu.service.impl;

import com.xiaoqiu.pojo.Interview;
import com.xiaoqiu.mapper.InterviewMapper;
import com.xiaoqiu.service.InterviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 面试邀约表
本表为次表，可做冗余，可以用mongo或者es替代 服务实现类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-15
 */
@Service
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview> implements InterviewService {

}
