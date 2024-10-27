package com.xiaoqiu.service;


import com.github.pagehelper.PageInfo;
import com.xiaoqiu.bo.CreateAdminBO;
import com.xiaoqiu.bo.ResetPwdBO;
import com.xiaoqiu.pojo.Admin;

import java.util.List;

/**
 * <p>
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 服务类
 * </p>
 *
 * @author xiaoqiu
 * @since 2022-09-04
 */
public interface IAdminService {

    /**
     * 创建admin账号
     * @param createAdminBO
     */
    public void createAdmin(CreateAdminBO createAdminBO);

    /**
     * 查询admin列表
     */
    PageInfo<Admin> getAdminList(String accountName, Integer pageNo, Integer pageSize);

    List<Admin> selectLikeUserName(String username);

    /**
     * 删除admin账号
     * @param username
     */
    public void deleteAdmin(String username);

    /**
     * 重置密码
     */
    void resetPwd(ResetPwdBO resetPwdBO);
}
