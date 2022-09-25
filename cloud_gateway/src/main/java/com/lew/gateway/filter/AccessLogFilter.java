/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author Yolen
 * @date 2022/2/9
 */
@Slf4j
@Configuration
public class AccessLogFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("AccessLogFilter log with AsyncLoggerContextSelector selected: {}", AsyncLoggerContextSelector.isSelected());
        return chain.filter(exchange);
    }
}
