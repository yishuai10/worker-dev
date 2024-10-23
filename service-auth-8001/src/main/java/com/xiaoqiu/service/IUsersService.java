package com.xiaoqiu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqiu.pojo.Users;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-23
 */
public interface IUsersService extends IService<Users> {

    Users queryUserByMobile(String mobile);

    @Transactional
    Users createUsers(String mobile);
}
