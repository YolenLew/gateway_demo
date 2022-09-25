/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.common.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lew.common.entity.MetaData;
import com.lew.common.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 模拟的业务类
 *
 * @author Yolen
 * @date 2022/9/18
 */
@Slf4j
public class SomeManager {
    public void aRun(Long id, List<String> data) {}

    public List<Integer> aListMethod(Long id, List<String> data) {
        return Lists.newArrayList();
    }

    public Map<String, Integer> aMapMethod(Long id, List<String> data) {
        return Maps.newHashMap();
    }

    public MetaData getClusterDetailById(String id, String clusterName) {
        try {
            TimeUnit.MILLISECONDS.sleep(80);
        } catch (InterruptedException e) {
            log.error("error when sleep: {}", e.getMessage(), e);
        }
        return MetaData.builder().id(id).clusterName(clusterName)
            .cpu(id + "_" + RandomStringUtils.randomAlphanumeric(5, 21))
            .memory(RandomStringUtils.randomAlphanumeric(5, 21)).build();
    }

    public List<String> getAllClusterData(String clusterName) {
        List<String> clusterDatas = CommonUtils.generateStrList();
        log.info("get cluster data of cluster [{}] with data size [{}]", clusterName, clusterDatas.size());
        return clusterDatas;
    }
}