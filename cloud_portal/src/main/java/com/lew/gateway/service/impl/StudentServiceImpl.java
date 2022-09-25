/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lew.gateway.dao.StudentDao;
import com.lew.gateway.service.StudentService;
import com.lew.gateway.model.Student;
import org.springframework.stereotype.Service;

/**
 * @author Yolen
 * @date 2022/5/21
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentDao, Student> implements StudentService {
    private final StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Student getStuDetailById(Long id) {
        return studentDao.getStuDetailById(id);
    }

    @Override
    public Student selectOneById(Long id) {
        return studentDao.selectOneById(id);
    }

    @Override
    public Student getStuById(Long id) {
        return studentDao.selectOneById(id);
    }

    @Override
    public int createStu(Student student) {
        return studentDao.checkAndCreateStu(student, 5);
    }
}
