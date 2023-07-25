/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.cache;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.lew.common.entity.ClusterData;
import com.lew.common.entity.MetaData;
import com.lew.common.service.SomeManager;
import com.lew.common.thread.RejectedPolicyWithReport;
import com.lew.common.util.CommonUtils;
import com.lew.common.util.ExecuteUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yolen
 * @date 2022/9/24
 */
@Slf4j
@Component
public class MetaDataCache implements ApplicationListener<ContextRefreshedEvent> {
    private static final ExecutorService COMMON_POOL;

    private static final ScheduledExecutorService SCHEDULE_POOL;

    private static final List<String> CLUSTER_NAMES;

    private static final ConcurrentHashMap<String, ClusterData> METACACHE_MAP;

    private static final SomeManager SOME_MANAGER;

    static {
        int processors = Runtime.getRuntime().availableProcessors();
        COMMON_POOL = new ThreadPoolExecutor(processors * 2, processors * 4, 30L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), ThreadFactoryBuilder.create().setNamePrefix("Common-Pool-").build(),
            new RejectedPolicyWithReport());
        SCHEDULE_POOL =
            new ScheduledThreadPoolExecutor(1, ThreadFactoryBuilder.create().setNamePrefix("Schedule-Pool-").build());
        log.info("init common pool: [{}], schedule pool: [{}]", COMMON_POOL, SCHEDULE_POOL);

        METACACHE_MAP = new ConcurrentHashMap<>();
        CLUSTER_NAMES = CommonUtils.generateStrList(8);
        SOME_MANAGER = new SomeManager();
    }

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 开启定时任务().注意：springboot和以springboot为基础开发的项目，无需判断event.getApplicationContext().getParent()是不是null
        SCHEDULE_POOL.scheduleWithFixedDelay(MetaDataCache::refresh, 0, 5, TimeUnit.MINUTES);
    }

    private static void refresh() {
        // 同步刷新测试
        // syncRefresh();

        // 异步刷新测试
        asyncRefresh();
    }

    private static void syncRefresh() {
        log.info("start to sync refresh data for cluster: {}", CLUSTER_NAMES);
        Stopwatch stopwatch = Stopwatch.createStarted();

        for (String clusterName : CLUSTER_NAMES) {
            ClusterData clusterData = new ClusterData();
            METACACHE_MAP.put(clusterName, clusterData);
            // 查询集群数据
            List<String> allClusterData = SOME_MANAGER.getAllClusterData(clusterName);
            // 查询集群中主机的详情并缓存
            for (String clusterId : allClusterData) {
                MetaData clusterDetail = SOME_MANAGER.getClusterDetailById(clusterId, clusterName);
                clusterData.getCpuList().add(clusterDetail.getCpu());
                clusterData.getMemList().add(clusterDetail.getMemory());
            }
        }

        Stopwatch stop = stopwatch.stop();
        log.info("cost time [{}] seconds and get map size [{}]", stop.elapsed(TimeUnit.SECONDS), METACACHE_MAP.size());
    }

    private static void asyncRefresh() {
        log.info("start to async refresh data for cluster: {}", CLUSTER_NAMES);
        Stopwatch stopwatch = Stopwatch.createStarted();

        for (String clusterName : CLUSTER_NAMES) {
            ClusterData clusterData = new ClusterData();
            METACACHE_MAP.put(clusterName, clusterData);
            // 查询集群数据
            List<String> allClusterData = SOME_MANAGER.getAllClusterData(clusterName);
            // 异步执行
            List<MetaData> metaDataList = ExecuteUtil.partitionCall2ListAsync(allClusterData, 10, COMMON_POOL,
                eachList -> getMetaBatch(eachList, clusterName));
            metaDataList.forEach(metaData -> {
                ClusterData clusterDataCache = METACACHE_MAP.get(metaData.getClusterName());
                if (!METACACHE_MAP.containsKey(metaData.getClusterName())) {
                    clusterDataCache = new ClusterData();
                    METACACHE_MAP.put(metaData.getClusterName(), clusterDataCache);
                }
                clusterDataCache.getMemList().add(metaData.getMemory());
                clusterDataCache.getCpuList().add(metaData.getCpu());
            });
        }

        Stopwatch stop = stopwatch.stop();
        log.info("cost time [{}] seconds and get map size [{}]", stop.elapsed(TimeUnit.SECONDS), METACACHE_MAP.size());
    }

    private static List<MetaData> getMetaBatch(List<String> clusters, String clusterName) {
        List<MetaData> metaDataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(clusters)) {
            for (String clusterDatum : clusters) {
                MetaData metaData = SOME_MANAGER.getClusterDetailById(clusterDatum, clusterName);
                metaDataList.add(metaData);
            }
        }
        return metaDataList;
    }

}
