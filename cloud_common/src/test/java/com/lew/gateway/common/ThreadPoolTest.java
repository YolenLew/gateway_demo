
/*
 * Copyright (c) 2025. This is my custom copyright information.2025-2025. All rights reserved.
 */

package com.lew.gateway.common;

import com.lew.LogDebugReject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yolen
 * @date 2025-12-17
 */
public class ThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        LogDebugReject.debugReject();
        CloseableHttpClient httpClient = getHttpClient();
        int code = get(httpClient, "https://www.example.com");
        System.out.println("请求结果：" + code);

        testConcurrentRequest();
    }
    public static void testConcurrentRequest() throws InterruptedException {
        System.out.println("开始请求");
        // 开始计时
        long startTime = System.currentTimeMillis();
        CloseableHttpClient httpClient = getHttpClient();
        // 创建包含100个线程的线程池，发起1000个并发请求，获取请求结果的状态码，请求地址是https://www.example.com
        ThreadPoolExecutor executor =
            new ThreadPoolExecutor(100, 100, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        // 结果收集到并发列表中去
        LinkedBlockingQueue<Integer> resultList = new LinkedBlockingQueue<>();
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                // 发起请求，获取请求结果
                int code = get(httpClient, "https://www.example.com");
                resultList.add(code);
            });
        }
        // 关闭线程池，不再接受新任务
        executor.shutdown();
        // 等待所有任务完成（设置合理的超时时间）
        if (!executor.awaitTermination(120, TimeUnit.SECONDS)) {
            // 如果超时，可以强制关闭
            System.out.println("部分任务超时，强制关闭线程池");
            executor.shutdownNow();
        }

        // 停止计时
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime) / 1000 + "秒，请求结果：" + resultList);
        System.out.println("请求结束");
    }

    // 编写HttpClient通用Get方法
    public static int get(CloseableHttpClient httpClient, String url) {
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
            // 获取请求结果
            return response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 构造包含通用请求池配置的httpClient
    public static CloseableHttpClient getHttpClient() {
        // 禁用https认证
        return HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().build()).build();
    }
}
