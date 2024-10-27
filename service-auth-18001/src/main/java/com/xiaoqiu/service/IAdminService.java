package com.xiaoqiu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoqiu.bo.AdminBO;
import com.xiaoqiu.pojo.Admin;
import com.xiaoqiu.vo.AdminVO;

/**
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 服务类
 * @author 小秋
 */
public interface IAdminService extends IService<Admin> {
    /**
     * admin 登录
     */
    String login(AdminBO adminBO);

    Admin selectByUserName(String username);

    AdminVO info(String token);
}