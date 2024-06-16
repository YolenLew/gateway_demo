/*
 * Copyright (c) 2024. This is my custom copyright information.2024-2024. All rights reserved.
 */

package com.lew.gateway.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yolen
 * @date 2024/3/28
 */
public class ThreadUtilTest {
    public static void main1(String[] args) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(2, 4, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3));
        for (int i = 0; i < 8; i++) {
            threadPoolExecutor.submit(() -> {
                System.out.println(Thread.currentThread().getName());
                return null;
            });
        }

        Thread.currentThread().join();
        System.out.println("end");
    }

    public static void main(String[] args) {
        double[] dp = new double[25]; // 创建一个足够大的数组来存储概率，直到24点
        dp[0] = 1; // 初始状态，点数为0的概率是100%

        // 更新dp数组，考虑所有可能的牌的点数
        for (int i = 1; i <= 24; i++) {
            for (int j = 1; j <= 13; j++) {
                if (i - j >= 0) {
                    dp[i] += dp[i - j] / 13.0; // 每张牌被抽中的概率是1/13
                }
            }
        }

        double winProbability = 0;
        // 计算获胜的概率，即点数总和在21到23之间的概率
        for (int i = 21; i <= 23; i++) {
            winProbability += dp[i];
        }

        System.out.println("获胜的概率是: " + winProbability);
    }
}
