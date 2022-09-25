/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.bean;

import com.lew.gateway.dao.StudentDao;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

/**
 * @author Yolen
 * @date 2022/5/31
 */
public class MockBeanProvider {
    @Primary
    public StudentDao studentDao() {
        return mock(StudentDao.class);
    }
}
