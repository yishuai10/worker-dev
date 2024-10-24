package com.xiaoqiu.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.xiaoqiu.common.ResponseStatusEnum;
import com.xiaoqiu.resp.R;
import com.xiaoqiu.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author xiaoqiu
 */
@Slf4j
@Component
@Order(0)
public class SecurityFilterJwt implements GlobalFilter {

    private static final int JWT_TOKEN_SPLIT_LENGTH = 2;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${jwt.security.exclude-urls}")
    private List<String> excludeUrls;

    /**
     * 路径匹配的规则器
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 校验请求是否需要过滤
        String url = exchange.getRequest().getURI().getPath();
        if (CollectionUtil.isNotEmpty(excludeUrls)) {
            for (String excludeUrl : excludeUrls) {
                if (antPathMatcher.matchStart(excludeUrl, url)) {
                    // 如果匹配到，则直接放行，表示当前的请求url是不需要被拦截校验的
                    return chain.filter(exchange);
                }
            }
        }

        // 判断header中是否有token，对用户请求进行判断拦截
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(token)) {
            return renderErrorMsg(exchange, ResponseStatusEnum.UN_LOGIN);
        }

        // 判空header中的令牌
        String[] tokenArr = token.split(JwtUtils.AT);
        if (tokenArr.length < JWT_TOKEN_SPLIT_LENGTH) {
            return renderErrorMsg(exchange, ResponseStatusEnum.UN_LOGIN);
        }

        return dealJwt(tokenArr[1], exchange, chain);
    }

    public Mono<Void> dealJwt(String jwt, ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            jwtUtils.checkJwtAndGetSubject(jwt);
            return chain.filter(exchange);
        } catch (ExpiredJwtException e) {
            return renderErrorMsg(exchange, ResponseStatusEnum.JWT_EXPIRE_ERROR);
        } catch (Exception e) {
            return renderErrorMsg(exchange, ResponseStatusEnum.JWT_SIGNATURE_ERROR);
        }
    }



    /**
     * 在网关层不会被全局异常捕获，重新包装并且返回错误信息
     */
    public Mono<Void> renderErrorMsg(ServerWebExchange exchange,
                                     ResponseStatusEnum statusEnum) {
        // 1. 获得response
        ServerHttpResponse response = exchange.getResponse();

        // 2. 构建jsonResult
        R<ResponseStatusEnum> jsonResult = R.response(statusEnum);

        // 3. 修改response的code为500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        // 4. 设定header类型
        if (!response.getHeaders().containsKey(HttpHeaders.CONTENT_TYPE)) {
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE);
        }

        // 5. 转换json并且向response中写入数据
        String resultJson = JSONUtil.toJsonStr(jsonResult);
        DataBuffer dataBuffer = response.bufferFactory().wrap(resultJson.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }
}
