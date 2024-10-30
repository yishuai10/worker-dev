package com.xiaoqiu.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqiu.client.WorkResumeServiceFeign;
import com.xiaoqiu.common.HttpStatusEnum;
import com.xiaoqiu.common.SexEnum;
import com.xiaoqiu.common.ShowWhichName;
import com.xiaoqiu.exception.GraceException;
import com.xiaoqiu.mapper.UsersMapper;
import com.xiaoqiu.pojo.Users;
import com.xiaoqiu.resp.R;
import com.xiaoqiu.resp.ResponseStatusEnum;
import com.xiaoqiu.service.IUsersService;
import com.xiaoqiu.utils.DesensitizationUtil;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        R<Void> r = workResumeServiceFeign.init(user.getId());
        if (r.getStatus() != HttpStatusEnum.SUCCESS.getCode()) {
            // 如果调用状态不是200，则手动回滚全局事务
            String xid = RootContext.getXID();
            if (StringUtils.isNotBlank(xid)) {
                try {
                    GlobalTransactionContext.reload(xid).rollback();
                } catch (TransactionException e) {
                    log.error("createUsers, 回滚全局事务失败！ mobile: {}", mobile, e);
                } finally {
                    GraceException.display(ResponseStatusEnum.USER_REGISTER_ERROR);
                }
            }
        }

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
