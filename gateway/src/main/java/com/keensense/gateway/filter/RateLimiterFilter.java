package com.keensense.gateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

/**
 * @Description 网关层限流
 * @Author fengsy
 * @Date 9/9/21
 */
public class RateLimiterFilter implements GlobalFilter {
    @Value("${filter.insert.limit}")
    private int permitsPerSecond;
    private static RateLimiter rateLimiter;

    @PostConstruct
    private void init() {
        rateLimiter = RateLimiter.create(permitsPerSecond);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        if (!rateLimiter.tryAcquire()) {
            String body = "Forbidden! Service visiting has reached the max limit!";
            return getVoidMono(exchange, body);
        }
        ServerHttpRequest build = mutate.build();
        return chain.filter(exchange.mutate().request(build).build());
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
}
