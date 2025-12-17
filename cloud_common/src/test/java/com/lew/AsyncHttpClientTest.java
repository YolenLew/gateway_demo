/*
 * Copyright (c) 2025. This is my custom copyright information.2025-2025. All rights reserved.
 */

package com.lew;

import org.asynchttpclient.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncHttpClientTest {
    // 在创建 AsyncHttpClient 之前设置这些系统属性
    static {
        // 完全禁用所有日志
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");
        System.setProperty("io.netty.leakDetection.level", "DISABLED");
        System.setProperty("org.asynchttpclient.debug", "false");
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        // 禁用 DNS 缓存（避免 DNS 相关日志）
        java.security.Security.setProperty("networkaddress.cache.ttl", "0");
        java.security.Security.setProperty("networkaddress.cache.negative.ttl", "0");
    }

    public static void main(String[] args) throws Exception {
        LogDebugReject.debugReject();
        long startTime = System.currentTimeMillis();

        int totalRequests = 1000;
        AtomicInteger completed = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(totalRequests);

        // 配置AsyncHttpClient
        DefaultAsyncHttpClientConfig config = Dsl.config()
                .setMaxConnections(2000)
                .setMaxConnectionsPerHost(1000)
            .setHandshakeTimeout(60000)
//                .setConnectTimeout(5000)
//                .setRequestTimeout(10000)
//                .setReadTimeout(10000)
                .setPooledConnectionIdleTimeout(1000)
                .setThreadPoolName("http-client")
                .setIoThreadsCount(Runtime.getRuntime().availableProcessors() * 2)
                .build();

        try (AsyncHttpClient client = Dsl.asyncHttpClient(config)) {
            for (int i = 0; i < totalRequests; i++) {
                BoundRequestBuilder request = client.prepareGet("https://www.example.com");

                request.execute(new AsyncCompletionHandler<Integer>() {
                    @Override
                    public Integer onCompleted(Response response) {
                        completed.incrementAndGet();
                        latch.countDown();
                        return response.getStatusCode();
                    }

                    @Override
                    public void onThrowable(Throwable t) {
                        System.out.println("ERROR: " + t.getMessage());
                        // 静默处理错误
                        latch.countDown();
                    }
                });
            }

            // 等待所有请求完成，设置超时时间
            boolean completedInTime = latch.await(600, TimeUnit.SECONDS);

            if (!completedInTime) {
                System.out.println("Timeout! Completed: " + completed.get() + "/" + totalRequests);
            } else {
                long endTime = System.currentTimeMillis();
                System.out.println("Completed " + completed.get() + " requests in " +
                                 (endTime - startTime) + "ms");
            }
        }
    }
}