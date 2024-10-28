package com.xiaoqiu.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqiu.base.Constant;
import com.xiaoqiu.bo.AdminBO;
import com.xiaoqiu.exception.GraceException;
import com.xiaoqiu.mapper.AdminMapper;
import com.xiaoqiu.pojo.Admin;
import com.xiaoqiu.resp.ResponseStatusEnum;
import com.xiaoqiu.service.IAdminService;
import com.xiaoqiu.utils.JwtUtils;
import com.xiaoqiu.utils.Md5Utils;
import com.xiaoqiu.vo.AdminVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * admin账户表，仅登录，不提供注册 服务实现类
 * @author 小秋
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public AdminVO info(String token) {
        String[] split = token.split("@");
        if (split.length < 2) {
            GraceException.display(ResponseStatusEnum.JWT_SIGNATURE_ERROR);
        }

        String adminStr = jwtUtils.checkJwtAndGetSubject(split[1]);
        Admin admin = JSONUtil.toBean(adminStr, Admin.class);
        return new AdminVO(admin.getUsername(), admin.getFace());
    }

    @Override
    public String login(AdminBO adminBO) {
        Admin admin = selectByUserName(adminBO.getUsername());
        if (admin == null) {
            GraceException.display(ResponseStatusEnum.ADMIN_NOT_EXIST);
        }

        String md5Str = Md5Utils.encrypt(adminBO.getPassword(), admin.getSlat());
        if (!md5Str.equalsIgnoreCase(admin.getPassword())) {
            GraceException.display(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
        }

        return jwtUtils.createJwtWithPrefix(JSONUtil.toJsonStr(admin), Constant.TOKEN_ADMIN_PREFIX);
    }

    @Override
    public Admin selectByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username));
    }
}