/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lew.gateway.model.Student;

/**
 * @author Yolen
 * @date 2022/5/21
 */
public interface StudentService extends IService<Student> {
    Student getStuDetailById(Long id);

    Student selectOneById(Long id);

    Student getStuById(Long id);

    int createStu(Student student);
}
