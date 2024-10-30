package com.xiaoqiu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqiu.client.WorkResumeServiceFeign;
import com.xiaoqiu.common.SexEnum;
import com.xiaoqiu.common.ShowWhichName;
import com.xiaoqiu.mapper.UsersMapper;
import com.xiaoqiu.pojo.Users;
import com.xiaoqiu.service.IUsersService;
import com.xiaoqiu.utils.DesensitizationUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-23
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private WorkResumeServiceFeign workResumeServiceFeign;

    @Value("${user.default.config.face.path:}")
    private String defaultFacePath;

    @Override
    public Users queryUserByMobile(String mobile) {
        return usersMapper.selectOne(new QueryWrapper<Users>().eq("mobile", mobile));
    }

    @GlobalTransactional
    @Override
    public Users createUsers(String mobile) {
        Users user = getDefaultUsers(mobile);
        user.setMobile(mobile);
        usersMapper.insert(user);

        // 初始化简历
        workResumeServiceFeign.init(user.getId());
        // 模拟除0异常
        int a = 1 / 0;
        return user;
    }

    private Users getDefaultUsers(String mobile) {
        Users user = new Users();
        user.setNickname("用户" + DesensitizationUtil.commonDisplay(mobile));
        user.setRealName("用户" + DesensitizationUtil.commonDisplay(mobile));
        user.setShowWhichName(ShowWhichName.NICKNAME.getCode());
        user.setSex(SexEnum.UNKNOWN.getCode());
        user.setFace(defaultFacePath);
        user.setEmail("");
        user.setBirthday(LocalDate.now());
        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");
        user.setDistrict("");
        user.setDescription("这家伙很懒，什么都没留下~");
        user.setStartWorkDate(LocalDate.now());
        user.setPosition("初级工程师");
        user.setRole(1);
        user.setHrInWhichCompanyId("");
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        return user;
    }
}
