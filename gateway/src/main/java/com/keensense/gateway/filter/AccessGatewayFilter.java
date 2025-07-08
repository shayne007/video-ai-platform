package com.keensense.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

/**
 * 
 * @description:
 * @author: luowei
 * @createDate:2019年5月8日 下午4:54:17
 * @company:
 */
//@Configuration
@Slf4j
public class AccessGatewayFilter implements GlobalFilter {

    @Value("${gate.ignore.startWith}")
    private String startWith;
    
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
//    	RoutePredicateHandlerMapping
//    	FilteringWebHandler
//    	GatewayFilter
        log.info("check token and permission....");
        ServerHttpRequest request = serverWebExchange.getRequest();
        String requestUri = request.getPath().pathWithinApplication().value();
        ServerHttpRequest.Builder mutate = request.mutate();
        // 不进行拦截的地址
        if (isStartWith(requestUri)) {
            ServerHttpRequest build = mutate.build();
            return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());
        }
//      授权认证 默认不经过授权
        /*
        boolean authToke = true;
        if(!authToke) {
        	String body = "Forbidden! Does not has Permission!";
        	return getVoidMono(serverWebExchange, body);
        }
        */
        // 申请客户端密钥头
        ServerHttpRequest build = mutate.build();
        return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());

    }

    /**
     * 网关抛异常
     *
     * @param body
     */
    @NotNull
    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, String body) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }
    
    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        boolean flag = false;
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s)) {
                return true;
            }
        }
        return flag;
    }


}
