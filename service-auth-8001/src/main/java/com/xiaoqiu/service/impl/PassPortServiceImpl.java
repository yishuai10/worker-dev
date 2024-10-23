package com.xiaoqiu.service.impl;


import cn.hutool.json.JSONUtil;
import com.xiaoqiu.base.RedisPrefix;
import com.xiaoqiu.bo.GetSmsBo;
import com.xiaoqiu.bo.LoginSmsBo;
import com.xiaoqiu.exception.XiaoQiuException;
import com.xiaoqiu.model.Constant;
import com.xiaoqiu.pojo.Users;
import com.xiaoqiu.service.IPassPortService;
import com.xiaoqiu.service.IUsersService;
import com.xiaoqiu.utils.IPUtil;
import com.xiaoqiu.utils.JwtUtils;
import com.xiaoqiu.utils.RedisOperator;
import com.xiaoqiu.utils.SMSUtils;
import com.xiaoqiu.vo.UsersVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.xiaoqiu.common.ResponseStatusEnum.SMS_CODE_ERROR;

/**
 * @author xiaoqiu
 */
@Slf4j
@Service
public class PassPortServiceImpl implements IPassPortService {
    @Autowired
    private SMSUtils smsUtils;
    @Autowired
    private RedisOperator redis;
    @Autowired
    private IUsersService usersService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void getSmsCode(GetSmsBo smsBo, HttpServletRequest request) {
        String userIp = IPUtil.getRequestIp(request);
        // 限制用户只能在60s以内获得一次验证码
        redis.setnx60s(RedisPrefix.MOBILE_SMSCODE + ":" + userIp, smsBo.getMobile());

        String code = (int)((Math.random() * 9 + 1) * 100000) + "";
        smsUtils.sendSMS(smsBo.getMobile(), code);
        log.info("手机号为： {}, 验证码为：{}", smsBo, code);
        // 把验证码存入到redis，用于后续的注册登录进行校验
        redis.set(RedisPrefix.MOBILE_SMSCODE + ":" + smsBo.getMobile(), code, 3 * 60);
    }

    @Override
    public UsersVO login(LoginSmsBo loginSmsBo, HttpServletRequest request) {
        String mobile = loginSmsBo.getMobile();
        String code = loginSmsBo.getSmsCode();
        String redisCode = redis.get(RedisPrefix.MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(code)) {
            throw new XiaoQiuException(SMS_CODE_ERROR);
        }

        // 2. 根据mobile查询数据库，判断用户是否存在
        Users user = usersService.queryUserByMobile(mobile);
        if (user == null) {
            // 2.1 如果查询的用户为空，则表示没有注册过，则需要注册信息入库
            user = usersService.createUsers(mobile);
        }

        // 3. 创建token
        String jwt = jwtUtils.createJwtWithPrefix(JSONUtil.toJsonStr(user), Constant.ONE_DAY, Constant.USER_TOKEN_APP_PREFIX);

        // 4. 用户登录注册以后，删除redis中的短信验证码
        redis.del(RedisPrefix.MOBILE_SMSCODE + ":" + mobile);
        // 5. 返回用户的信息给前端
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserToken(jwt);
        return usersVO;
    }
}
