/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.gateway.controller;

import com.lew.common.entity.CommonResult;
import com.lew.gateway.model.Teacher;
import com.lew.gateway.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.StopWatch;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author Yolen
 * @date 2023/7/27
 */
@Slf4j
@Validated
@RestController
@RequestMapping(value = "/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping(value = "/batchInsert")
    public CommonResult<String> batchInsert() {
        log.info("begin to batch insert teacher data...");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("batchInsert");

        // 每次批量插入2000条记录，提高插入效率
        int batchSize = 2000;
        List<Teacher> list = new LinkedList<>();
        for (int i = 0; i < 1000101; i++) {
            Teacher teacher = new Teacher();
            teacher.setName(UUID.randomUUID().toString().replace("-", ""));
            teacher.setDailySalary(RandomUtils.nextDouble(6888, 18888));
            teacher.setWorkYear(String.valueOf(RandomUtils.nextInt(1, 20)));
            list.add(teacher);

            if (CollectionUtils.isNotEmpty(list) && list.size() % batchSize == 0) {
//                teacherService.saveBatch(list);
                log.info("has batchInsert size: {}", i);
                list.clear();
            }
        }

        stopWatch.stop();
        log.info("end to batch insert teacher data,total cost: {} ms.", stopWatch.getTotalTimeMillis());
        return CommonResult.success("ok");
    }

    @PostMapping(value = "/list")
    public CommonResult<List<Teacher>> list(){
        return CommonResult.success(Collections.emptyList());
    }

    @PostMapping(value = "/stream")
    public CommonResult<List<Teacher>> stream(){
        return CommonResult.success(teacherService.selectByCursor());
    }
}
