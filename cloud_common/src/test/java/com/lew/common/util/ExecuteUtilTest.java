/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.common.util;

import com.google.common.base.Stopwatch;
import com.lew.common.service.SomeManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.jeasy.random.EasyRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

/**
 * 版权声明：本文为CSDN博主「明明如月学长」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/w605283073/article/details/101399427
 *
 * @author Yolen
 * @date 2022/9/18
 */
@Slf4j @RunWith(PowerMockRunner.class) public class ExecuteUtilTest {
    private EasyRandom easyRandom = new EasyRandom();

    @Mock private SomeManager someManager;

    /**
     * 测试数据
     */
    private List<String> mockDataList;

    private int total = 30;

    private AtomicInteger atomicInteger;

    @Before public void init() {
        // 构造30条数据
        mockDataList = easyRandom.objects(String.class, total).collect(Collectors.toList());
        log.info("mockDataList: {}", mockDataList);
    }

    @Test public void testARunPartition() {
        // mock aRun数据打桩
        PowerMockito.doNothing().when(someManager).aRun(anyLong(), any());

        // 每批 10 个
        ExecuteUtil.partitionRun(mockDataList, 10, eachList -> someManager.aRun(1L, eachList));

        //验证执行了 3 次
        Mockito.verify(someManager, new Times(3)).aRun(anyLong(), any());
    }

    @Test public void testCallReturnMapPartition() {
        // mock  每次调用返回条数
        // 注意：
        // 如果仅调用doReturn一次，那么每次返回都是key相同的Map，
        // 如果需要不覆盖，则doReturn次数和 invocations 相同
        int eachReturnSize = 3;
        PowerMockito.doReturn(mockMap(eachReturnSize)).doReturn(mockMap(eachReturnSize)).when(someManager)
            .aMapMethod(anyLong(), any());

        // 每批
        int size = 16;
        Map<String, Integer> resultMap =
            ExecuteUtil.partitionCall2Map(mockDataList, size, eachList -> someManager.aMapMethod(2L, eachList));

        //验证执行次数
        int invocations = 2;
        Mockito.verify(someManager, new Times(invocations)).aMapMethod(anyLong(), any());

        // 正好几轮
        int turns = total % size == 0 ? (total / size) : (total / size + 1);
        assertEquals(turns * eachReturnSize, resultMap.size());
    }

    private Map<String, Integer> mockMap(int size) {
        Map<String, Integer> result = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            // 极力保证key不重复
            result.put(easyRandom.nextObject(String.class) + RandomUtils.nextInt(), easyRandom.nextInt());
        }
        return result;
    }

    @Test public void testCallReturnListPartition() {
        // mock  每次调用返回条数(注意每次调用都是这2个)
        int eachReturnSize = 2;
        PowerMockito.doReturn(easyRandom.objects(String.class, eachReturnSize).collect(Collectors.toList()))
            .when(someManager).aListMethod(anyLong(), any());

        // 分批执行
        int size = 4;
        List<Integer> resultList =
            ExecuteUtil.partitionCall2List(mockDataList, size, eachList -> someManager.aListMethod(2L, eachList));

        //验证执行次数
        int invocations = 8;
        Mockito.verify(someManager, new Times(invocations)).aListMethod(anyLong(), any());

        // 正好几轮
        int turns = total % size == 0 ? (total / size) : (total / size + 1);
        assertEquals(turns * eachReturnSize, resultList.size());
    }

    @Test public void testCallReturnListPartitionSync() {
        total = 300;
        mockDataList = easyRandom.objects(String.class, total).collect(Collectors.toList());
        atomicInteger = new AtomicInteger(0);

        // 同步分批执行
        Stopwatch stopwatch = Stopwatch.createStarted();
        int size = 2;
        List<Integer> resultList =
            ExecuteUtil.partitionCall2List(mockDataList, size, eachList -> someCall(2L, eachList));
        Stopwatch stop = stopwatch.stop();
        log.info("执行时间: {} 秒", stop.elapsed(TimeUnit.SECONDS));
        assertEquals(total, resultList.size());

        // 正好几轮 执行时间: 301 秒 共调用了150次
        int turns = total % size == 0 ? (total / size) : (total / size + 1);
        log.info("共调用了{}次", turns);
        assertEquals(turns, atomicInteger.get());

    }

    @Test public void testCallReturnListPartitionAsync() {
        total = 300;
        atomicInteger = new AtomicInteger(0);
        mockDataList = easyRandom.objects(String.class, total).collect(Collectors.toList());

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 异步分批执行
        Stopwatch stopwatch = Stopwatch.createStarted();
        int size = 2;
        List<Integer> resultList = ExecuteUtil
            .partitionCall2ListAsync(mockDataList, size, executorService, eachList -> someCall(2L, eachList));
        Stopwatch stop = stopwatch.stop();
        log.info("执行时间: {} 秒", stop.elapsed(TimeUnit.SECONDS));
        assertEquals(total, resultList.size());

        // 正好几轮 执行时间: 30 秒 共调用了150次
        int turns = total % size == 0 ? (total / size) : (total / size + 1);
        log.info("共调用了{}次", turns);
        assertEquals(turns, atomicInteger.get());

        // 顺序也一致
        for (int i = 0; i < mockDataList.size(); i++) {
            assertEquals((Integer)mockDataList.get(i).length(), resultList.get(i));
        }
    }

    /**
     * 模拟一次调用
     */
    private List<Integer> someCall(Long id, List<String> strList) {
        log.info("current turns-->{}, id: {}, strList.size：{}", atomicInteger.incrementAndGet(), id, strList.size());
        try {
            TimeUnit.SECONDS.sleep(2L);
        } catch (InterruptedException e) {
            log.error("someCall error: {}", e.getMessage(), e);
        }
        return strList.stream().map(String::length).collect(Collectors.toList());
    }
}