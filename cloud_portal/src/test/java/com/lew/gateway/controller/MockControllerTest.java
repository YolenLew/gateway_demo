/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.controller;

import com.lew.gateway.dao.StudentDao;
import com.lew.gateway.service.StudentService;
import com.lew.gateway.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author Yolen
 * @date 2022/5/31
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MockControllerTest {
    @Autowired
    private StudentService studentService;

    @Mock
    private StudentDao studentDao;

    @Test
    public void getStuById() {
        Student student = new Student();
        student.setId(1L);
        student.setName("nameForTest");
        Mockito.when(studentDao.selectOneById(1L)).thenReturn(student);

        Student studentById = studentService.getStuById(1L);
        Assert.assertNotNull(studentById);
        Assert.assertEquals(studentById.getId(), student.getId());
        Assert.assertEquals(studentById.getName(), student.getName());
    }
}
