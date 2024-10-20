package com.xiaoqiu.service.impl;

import com.xiaoqiu.pojo.Job;
import com.xiaoqiu.mapper.JobMapper;
import com.xiaoqiu.service.JobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * HR发布的职位表 服务实现类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-20
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

}
