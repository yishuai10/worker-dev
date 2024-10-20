package com.xiaoqiu.service.impl;

import com.xiaoqiu.pojo.Users;
import com.xiaoqiu.mapper.UsersMapper;
import com.xiaoqiu.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-20
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
