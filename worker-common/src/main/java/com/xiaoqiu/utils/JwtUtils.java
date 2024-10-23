package com.xiaoqiu.utils;


import cn.hutool.core.codec.Base64;
import com.xiaoqiu.exception.GraceException;
import com.xiaoqiu.common.ResponseStatusEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author xiaoqiu
 */
@Component
@Slf4j
public class JwtUtils {

    public static final String AT = "@";

    @Value("${jwt.key:m6rf9xow56mragngrjxco4nu6kbz4v50}")
    public String jwtKey;

    /**
     * 创建带前缀的jwt
     * @param body 内容
     * @param expireTimes 过期时间 单位s
     * @param prefix 前缀
     * @return token
     */
    public String createJwtWithPrefix(String body, Long expireTimes, String prefix) {
        if (expireTimes == null) {
            GraceException.display(ResponseStatusEnum.SYSTEM_NO_EXPIRE_ERROR);
        }

        return prefix + AT + createJwt(body, expireTimes);
    }

    public String createJwtWithPrefix(String body, String prefix) {
        return prefix + AT + createJwt(body);
    }

    public String createJwt(String body) {
        return doCreateJwt(body, null);
    }

    public String createJwt(String body, Long expireTimes) {
        if (expireTimes == null) {
            GraceException.display(ResponseStatusEnum.SYSTEM_NO_EXPIRE_ERROR);
        }

        return doCreateJwt(body, expireTimes);
    }

    public String doCreateJwt(String body, Long expireTimes) {
        // 1. 对秘钥进行base64编码
        String base64 = Base64.encode(jwtKey);
        // 2. 对base64生成一个秘钥的对象
        SecretKey secretKey = Keys.hmacShaKeyFor(base64.getBytes());
        return expireTimes == null ? generatorJwt(body, secretKey) : generatorJwt(body, expireTimes, secretKey);
    }

    public String generatorJwt(String body, SecretKey secretKey) {
        return Jwts.builder()
                .setSubject(body)
                .signWith(secretKey)
                .compact();
    }

    public String generatorJwt(String body, Long expireTimes, SecretKey secretKey) {
        // 定义过期时间
        Date expireDate = new Date(System.currentTimeMillis() + expireTimes);
        return Jwts.builder()
                .setSubject(body)
                .signWith(secretKey)
                .setExpiration(expireDate)
                .compact();
    }

    public String checkJwtAndGetSubject(String pendingJwt) {
        // 1. 对秘钥进行base64编码
        String base64 = Base64.encode(jwtKey);
        // 2. 对base64生成一个秘钥的对象
        SecretKey secretKey = Keys.hmacShaKeyFor(base64.getBytes());
        // 3. 校验jwt
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();       // 构造解析器
        // 解析成功，可以获得Claims，从而去get相关的数据，如果此处抛出异常，则说明解析不通过，也就是token失效或者被篡改
        Jws<Claims> jws = jwtParser.parseClaimsJws(pendingJwt);
        return jws.getBody().getSubject();
    }

}
