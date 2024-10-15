package com.xiaoqiu.service.impl;

import com.xiaoqiu.pojo.Admin;
import com.xiaoqiu.mapper.AdminMapper;
import com.xiaoqiu.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 服务实现类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-15
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
