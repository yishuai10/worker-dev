package com.xiaoqiu.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.xiaoqiu.base.RedisPrefix;
import com.xiaoqiu.dto.SaasQrTokenDto;
import com.xiaoqiu.exception.XiaoQiuException;
import com.xiaoqiu.pojo.Users;
import com.xiaoqiu.resp.ResponseStatusEnum;
import com.xiaoqiu.service.ISaasPassPortService;
import com.xiaoqiu.service.IUsersService;
import com.xiaoqiu.utils.JwtUtils;
import com.xiaoqiu.utils.RedisOperator;
import com.xiaoqiu.vo.SaasUserVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.xiaoqiu.base.RedisPrefix.SAAS_PLATFORM_LOGIN_TOKEN;

/**
 * @author xiaoqiu
 */
@Service
public class SaasPassPortServiceImpl implements ISaasPassPortService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisOperator redis;
    @Autowired
    private IUsersService usersService;

    @Override
    public String getQrToken() {
        String qrToken = UUID.randomUUID().toString();
        // 把生成的token存入redis， 0为未扫码， 1为扫码成功
        SaasQrTokenDto tokenDto = SaasQrTokenDto.builder().status(0).build();
        redis.set(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken, JSONUtil.toJsonStr(tokenDto), 5 * 60);
        return qrToken;
    }

    @Override
    public String scanCode(String qrToken, HttpServletRequest request) {
        // 判空 - qrToken
        if (StringUtils.isBlank(qrToken)) {
            throw new XiaoQiuException(ResponseStatusEnum.FAILED);
        }

        // 从redis中获得并且判断qrToken是否有效
        String redisQrToken = redis.get(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken);
        if (StringUtils.isBlank(redisQrToken)) {
            throw new XiaoQiuException(ResponseStatusEnum.FAILED);
        }

        SaasQrTokenDto tokenDto = JSONUtil.toBean(redisQrToken, SaasQrTokenDto.class);
        if (tokenDto.getStatus() < 0) {
            throw new XiaoQiuException(ResponseStatusEnum.FAILED);
        }

        // 从header中获得用户id和jwt令牌
        String headerUserId = request.getHeader("appUserId");
        String headerUserToken = request.getHeader("appUserToken");

        // 判空 - headerUserId + headerUserToken
        if (StringUtils.isBlank(headerUserId) || StringUtils.isBlank(headerUserToken)) {
            throw new XiaoQiuException(ResponseStatusEnum.HR_TICKET_INVALID);
        }

        // 对JWT进行校验
        String userJson = jwtUtils.checkJwtAndGetSubject(headerUserToken.split("@")[1]);
        if (StringUtils.isBlank(userJson)) {
            throw new XiaoQiuException(ResponseStatusEnum.HR_TICKET_INVALID);
        }

        // 生成预登录token
        String preToken = UUID.randomUUID().toString();
        // redis写入标记，当前qrToken需要被读取并且失效覆盖，网页端标记二维码已被扫
        tokenDto.setStatus(1);
        tokenDto.setPreToken(preToken);
        redis.set(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken, JSONUtil.toJsonStr(tokenDto), 5*60);
        return preToken;
    }

    @Override
    public List<Object> codeHasBeenRead(String qrToken) {
        String readStr = redis.get(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken);
        if (StringUtils.isBlank(readStr)) {
            return Collections.emptyList();
        }

        List<Object> list = new ArrayList<>();
        SaasQrTokenDto tokenDto = JSONUtil.toBean(readStr, SaasQrTokenDto.class);
        list.add(tokenDto.getStatus());
        list.add(tokenDto.getPreToken());
        return list;
    }

    @Override
    public void goQrLogin(String userId, String qrToken, String preToken) {
        String preTokenRedisArr = redis.get(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken);
        if (StringUtils.isBlank(preTokenRedisArr)) {
            throw new XiaoQiuException(ResponseStatusEnum.FAILED);
        }

        SaasQrTokenDto tokenDto = JSONUtil.toBean(preTokenRedisArr, SaasQrTokenDto.class);
        if (Objects.equals(tokenDto.getPreToken(), preToken)) {
            // 根据用户id获得用户信息
            Users hrUser = usersService.getById(userId);
            if (hrUser == null) {
                throw new XiaoQiuException(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
            }

            // 存入用户信息到redis中，因为H5在未登录的情况下，拿不到用户id，所以暂存用户信息到redis。
            // *如果使用websocket是可以直接通信H5获得用户id，则无此问题
            redis.set(RedisPrefix.REDIS_SAAS_USER_INFO + ":temp:" + preToken, JSONUtil.toJsonStr(hrUser), 5 * 60);
        }
    }

    @Override
    public String checkLogin(String preToken) {
        if (StringUtils.isBlank(preToken)) {
            return StringUtils.EMPTY;
        }

        // 获得用户临时信息
        String userJson = redis.get(RedisPrefix.REDIS_SAAS_USER_INFO + ":temp:" + preToken);
        if (StringUtils.isBlank(userJson)) {
            throw new XiaoQiuException(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }

        // 确认执行登录后，生成saas用户的token，并且长期有效
        String saasUserToken = jwtUtils.createJwtWithPrefix(userJson, RedisPrefix.TOKEN_SAAS_PREFIX);
        redis.set(RedisPrefix.REDIS_SAAS_USER_INFO + ":" + saasUserToken, userJson, 7 * 24 * 60 * 60);
        return saasUserToken;
    }

    @Override
    public SaasUserVO info(String token) {
        String userJson = redis.get(RedisPrefix.REDIS_SAAS_USER_INFO + ":" + token);
        if (StringUtils.isBlank(userJson)) {
            throw new XiaoQiuException(ResponseStatusEnum.FAILED);
        }

        Users users = JSONUtil.toBean(userJson, Users.class);
        SaasUserVO saasUserVO = new SaasUserVO();
        BeanUtils.copyProperties(users, saasUserVO);
        return saasUserVO;
    }
}
