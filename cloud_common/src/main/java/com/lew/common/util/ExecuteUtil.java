/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.common.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分批调用接口工具 原文链接：https://blog.csdn.net/w605283073/article/details/101399427
 *
 * @author Yolen
 * @date 2022/9/18
 */
public class ExecuteUtil {
    /**
     * 执行分批调用无返回值的消费函数
     *
     * @param dataList 数据源
     * @param size 每批次执行数据大小
     * @param consumer 消费型函数
     * @param <T> 泛型
     */
    public static <T> void partitionRun(List<T> dataList, int size, Consumer<List<T>> consumer) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        Preconditions.checkArgument(size > 0, "size must not be a minus");
        Lists.partition(dataList, size).forEach(consumer);
    }

    /**
     * 执行分批调用带返回值的消费函数
     *
     * @param dataList 数据源
     * @param size 每批次执行数据大小
     * @param function 方法型函数
     * @param <T> 入参集合泛型
     * @param <V> 返参集合泛型
     * @return 分批执行结果
     */
    public static <T, V> List<V> partitionCall2List(List<T> dataList, int size, Function<List<T>, List<V>> function) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        Preconditions.checkArgument(size > 0, "size must not be a minus");

        // Stream.reduce()合并流的元素
        return Lists.partition(dataList, size).stream().map(function).filter(Objects::nonNull)
            .reduce(Lists.newArrayList(), (resultList1, resultList2) -> {
                resultList1.addAll(resultList2);
                return resultList1;
            });
    }

    /**
     * 异步执行分批调用带返回值的消费函数
     *
     * @param dataList 数据源
     * @param size 每批次执行数据大小
     * @param function 方法型函数
     * @param executorService 线程池
     * @param <T> 入参集合泛型
     * @param <V> 返参集合泛型
     * @return 分批执行结果
     */
    public static <T, V> List<V> partitionCall2ListAsync(List<T> dataList, int size, ExecutorService executorService,
        Function<List<T>, List<V>> function) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        Preconditions.checkArgument(size > 0, "size must not be a minus");

        List<CompletableFuture<List<V>>> completableFutures = Lists.partition(dataList, size).stream().map(eachList -> {
            if (Objects.isNull(executorService)) {
                return CompletableFuture.supplyAsync(() -> function.apply(eachList));
            }
            return CompletableFuture.supplyAsync(() -> function.apply(eachList), executorService);
        }).collect(Collectors.toList());

        try {
            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return completableFutures.stream().map(CompletableFuture::join).filter(CollectionUtils::isNotEmpty)
            .reduce(Lists.newArrayList(), ((list1, list2) -> {
                List<V> resultList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(list1)) {
                    resultList.addAll(list1);
                }

                if (CollectionUtils.isNotEmpty(list2)) {
                    resultList.addAll(list2);
                }

                return resultList;
            }));
    }

    public static <T, V> Map<T, V> partitionCall2Map(List<T> dataList, int size,
        Function<List<T>, Map<T, V>> function) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyMap();
        }
        Preconditions.checkArgument(size > 0, "size must not be a minus");

        return Lists.partition(dataList, size).stream().map(function).filter(Objects::nonNull).reduce(Maps.newHashMap(),
            (resultMap1, resultMap2) -> {
                resultMap1.putAll(resultMap2);
                return resultMap1;
            });
    }

}
