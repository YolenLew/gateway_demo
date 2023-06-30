/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.dao;

import com.lew.gateway.model.Student;
import com.lew.gateway.model.enums.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Yolen
 * @date 2022/7/7
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:config/application.yml")
@Slf4j
public class StudentDaoTest {
    @Autowired
    private StudentDao studentDao;

    private Student student;

    @Before
    public void setup() {
        student = new Student();
        student.setName("test_name");
        student.setAvgScore(1.0);
        student.setRoleType(RoleType.CHARACTER);
    }

    @Test
    public void testInsertStudent() {
        int maxInsertNum = 1;
        student.setName(generateUUID());
        int insertResult = studentDao.checkAndCreateStu(student, maxInsertNum);
        log.info("insertResult: {}", insertResult);
        Assert.assertEquals(1L, insertResult);
        int secondResult = studentDao.checkAndCreateStu(student, maxInsertNum);
        Assert.assertEquals(0L, secondResult);
    }

    @Test
    public void testBatchInsertStudent() {
        int maxInsertNum = 5;
        student.setName(generateUUID());
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("processors: {}", processors);
        ExecutorService executor = Executors.newFixedThreadPool(processors);

        Supplier<Integer> insertStuFuntion = () -> {
            student.setId(ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE));
            student.setAvgScore(ThreadLocalRandom.current().nextDouble());
            int insertResult = studentDao.checkAndCreateStu(student, maxInsertNum);
            log.info("{} insertResult: {}", Thread.currentThread().getName(), insertResult);
            return insertResult;
        };

        int threadNum = 100;
        final LinkedList<Integer> resultList = Stream.generate(() -> CompletableFuture.supplyAsync(insertStuFuntion, executor)).limit(threadNum).map(CompletableFuture::join).collect(Collectors.toCollection(LinkedList::new));

        for (int i = 0; i < maxInsertNum; i++) {
            Assert.assertEquals(1, resultList.get(i).intValue());
        }

        for (int i = maxInsertNum; i < threadNum; i++) {
            Assert.assertEquals(0, resultList.get(i).intValue());
        }

    }

    @Test
    public void testBatchInsertStudentWithRoleType() {
        int maxInsertNum = 5;
        student.setName(generateUUID());
        student.setRoleType(RoleType.MONITOR);
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("processors: {}", processors);
        ExecutorService executor = Executors.newFixedThreadPool(processors);

        Supplier<Integer> insertStuFuntion = () -> {
            student.setId(ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE));
            student.setAvgScore(ThreadLocalRandom.current().nextDouble());
            int insertResult = studentDao.checkAndCreateStu(student, maxInsertNum);
            log.info("{} insertResult: {}", Thread.currentThread().getName(), insertResult);
            return insertResult;
        };

        int threadNum = 100;
        final LinkedList<Integer> resultList = Stream.generate(() -> CompletableFuture.supplyAsync(insertStuFuntion, executor)).limit(threadNum).map(CompletableFuture::join).collect(Collectors.toCollection(LinkedList::new));

        Assert.assertEquals(1, resultList.get(0).intValue());

        for (int i = 1; i < threadNum; i++) {
            Assert.assertEquals(0, resultList.get(i).intValue());
        }

    }

    public String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}