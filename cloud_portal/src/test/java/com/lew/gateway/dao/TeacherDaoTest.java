/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.gateway.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * @author Yolen
 * @date 2022/7/7
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:config/application.yml")
@Slf4j
public class TeacherDaoTest {

    public String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}