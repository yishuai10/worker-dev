package com.xiaoqiu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoqiu.bo.CreateAdminBO;
import com.xiaoqiu.bo.ResetPwdBO;
import com.xiaoqiu.exception.GraceException;
import com.xiaoqiu.mapper.AdminMapper;
import com.xiaoqiu.pojo.Admin;
import com.xiaoqiu.resp.ResponseStatusEnum;
import com.xiaoqiu.service.IAdminService;
import com.xiaoqiu.utils.Md5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 服务实现类
 * </p>
 *
 * @author xiaoqiu
 * @since 2022-09-04
 */
@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void createAdmin(CreateAdminBO createAdminBO) {
        Admin admin = selectByUserName(createAdminBO.getUsername());
        if (admin == null) {
            GraceException.display(ResponseStatusEnum.ADMIN_NOT_EXIST);
        }

        // 创建账号
        Admin newAdmin = new Admin();
        BeanUtils.copyProperties(createAdminBO, newAdmin);

        // 生成随机数字或者英文字母结合的盐
        String slat = (int)((Math.random() * 9 + 1) * 100000) + "";
        String pwd = Md5Utils.encrypt(createAdminBO.getPassword(), slat);
        newAdmin.setPassword(pwd);
        newAdmin.setSlat(slat);

        newAdmin.setCreateTime(LocalDateTime.now());
        newAdmin.setUpdatedTime(LocalDateTime.now());
        adminMapper.insert(newAdmin);
    }

    @Override
    public PageInfo<Admin> getAdminList(String accountName, Integer pageNo, Integer pageSize) {
        try(Page<Admin> ignored = PageHelper.startPage(pageNo, pageSize)) {
            List<Admin> adminList = selectLikeUserName(accountName);
            return new PageInfo<>(adminList);
        }
    }

    @Override
    public List<Admin> selectLikeUserName(String username) {
        return adminMapper.selectList(new QueryWrapper<Admin>().like("username", username));
    }

    @Override
    public void deleteAdmin(String username) {
        adminMapper.delete(new QueryWrapper<Admin>().eq("username", username));
    }

    @Override
    public void resetPwd(ResetPwdBO resetPwdBO) {
        if (!Objects.equals(resetPwdBO.getPassword(), resetPwdBO.getRePassword())) {
            GraceException.display(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            return;
        }

        // 生成加密盐
        String slat = (int)((Math.random() * 9 + 1) * 100000) + "";
        String pwd = Md5Utils.encrypt(resetPwdBO.getPassword(), slat);
        Admin admin = new Admin();
        admin.setId(resetPwdBO.getAdminId());
        admin.setPassword(pwd);
        adminMapper.update(admin, new QueryWrapper<Admin>().eq("id", resetPwdBO.getAdminId()));

    }

    private Admin selectByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username));
    }
}
