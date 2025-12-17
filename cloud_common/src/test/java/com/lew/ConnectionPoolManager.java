/*
 * Copyright (c) 2025. This is my custom copyright information.2025-2025. All rights reserved.
 */

package com.lew;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ConnectionPoolManager {

    private final AsyncHttpClient client;
    private final ExecutorService executor;
    private final Semaphore semaphore; // 控制并发数

    public ConnectionPoolManager(int maxConcurrentRequests) {
        // 创建高容量连接池配置
        DefaultAsyncHttpClientConfig config = Dsl.config()
                .setMaxConnections(2000)                // 总连接数
                .setMaxConnectionsPerHost(1000)         // 每主机连接数
                .setConnectTimeout(3000)
                .setRequestTimeout(5000)
                .setReadTimeout(5000)
                .setPooledConnectionIdleTimeout(30000)
                .setConnectionTtl(120000)
                .setIoThreadsCount(200)                 // 更多IO线程
                .setKeepAlive(true)
                .setTcpNoDelay(true)
                .setSoReuseAddress(true)
                .setSoLinger(0)
                .setAcquireFreeChannelTimeout(5000)
                .setUseNativeTransport(false)
                .setFollowRedirect(true)                // 允许重定向
                .setMaxRedirects(5)                     // 最大重定向次数
                .setUserAgent("High-Performance-Test/1.0")
                .build();

        this.client = Dsl.asyncHttpClient(config);
        this.executor = Executors.newFixedThreadPool(maxConcurrentRequests);
        this.semaphore = new Semaphore(maxConcurrentRequests);
    }

    public CompletableFuture<Integer> executeRequest(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                semaphore.acquire(); // 控制并发

                BoundRequestBuilder request = client.prepareGet(url)
                        .addHeader("Accept-Encoding", "gzip, deflate")
                        .addHeader("Accept-Language", "en-US,en;q=0.9")
                        .addHeader("Cache-Control", "no-cache");

                return request.execute()
                        .toCompletableFuture()
                        .thenApply(Response::getStatusCode)
                        .exceptionally(e -> {
                            // System.err.println("Request failed: " + e.getMessage());
                            return -1;
                        })
                        .get(10, TimeUnit.SECONDS);

            } catch (Exception e) {
                return -1;
            } finally {
                semaphore.release();
            }
        }, executor);
    }

    public void executeBatch(List<String> urls) throws Exception {
        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        AtomicInteger completed = new AtomicInteger(0);
        int total = urls.size();

        System.out.println("Starting batch of " + total + " requests");
        long startTime = System.currentTimeMillis();

        for (String url : urls) {
            CompletableFuture<Integer> future = executeRequest(url)
                    .thenApply(statusCode -> {
                        int count = completed.incrementAndGet();
                        if (count % 100 == 0) {
                            System.out.printf("Progress: %d/%d (%.1f%%)%n",
                                    count, total, (count * 100.0 / total));
                        }
                        return statusCode;
                    });
            futures.add(future);
        }

        // 等待所有完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));

        allFutures.get(120, TimeUnit.SECONDS); // 2分钟超时

        long endTime = System.currentTimeMillis();

        // 统计结果
        List<Integer> results = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        return -1;
                    }
                })
                .collect(Collectors.toList());

        long successCount = results.stream().filter(code -> code == 200).count();

        System.out.println("\n=============== RESULTS ===============");
        System.out.println("Total requests: " + total);
        System.out.println("Successful: " + successCount);
        System.out.println("Failed: " + (total - successCount));
        System.out.println("Total time: " + (endTime - startTime) + "ms");
        System.out.printf("Requests per second: %.2f%n",
                (total * 1000.0) / (endTime - startTime));
        System.out.println("========================================");
    }

    public void close() throws Exception {
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        client.close();
    }

    public static void main(String[] args) throws Exception {
        // 准备URL列表
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            urls.add("https://httpbin.org/get?request=" + i);
        }

        // 使用连接池管理器
        ConnectionPoolManager manager = new ConnectionPoolManager(200); // 200并发

        try {
            manager.executeBatch(urls);
        } finally {
            manager.close();
        }
    }
}