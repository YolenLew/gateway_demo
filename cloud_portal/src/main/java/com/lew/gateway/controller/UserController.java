/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lew.common.entity.CommonResult;
import com.lew.common.util.JacksonUtil;
import com.lew.gateway.assemble.UserDeptAssembleService;
import com.lew.gateway.dao.StudentDao;
import com.lew.gateway.dao.UserDao;
import com.lew.gateway.model.Student;
import com.lew.gateway.model.User;
import com.lew.gateway.model.UserDept;
import com.lew.gateway.service.StudentService;
import com.lew.gateway.service.impl.UserDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Yolen
 * @date 2022/3/8
 */
@RestController
@RequestMapping(value = "/user")
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentService studentService;
    @Autowired
    private UserDeptService userDeptService;
    @Autowired
    private UserDeptAssembleService userDeptAssembleService;

    @GetMapping(value = "/findAll")
    public CommonResult<List<User>> findAll() {
        List<User> userList = userDao.findAll();
        log.info("userList size:{}", userList.size());
        return CommonResult.success(userList);
    }

    @GetMapping(value = "/findByUserId/{id}")
    public CommonResult<User> findByUserId(@PathVariable(value = "id") Long id) {
        User userById = userDao.findByUserId(id);
        return CommonResult.success(userById);
    }

    @GetMapping(value = "/getStuDetailById/{id}/")
    public CommonResult<Student> getStuDetailById(@PathVariable(value = "id") Long id) {
        Student student = studentService.getStuDetailById(id);
        return CommonResult.success(student);
    }

    @GetMapping(value = "/getStuById/{id}/")
    public CommonResult<Student> getStuById(@Min(value = 2) @PathVariable(value = "id") Long id) {
        Student student = studentService.getStuById(id);
        String stuToStr = Objects.toString(student, "");
        log.info("student:{}", stuToStr);
        return CommonResult.success(student);
    }

    @GetMapping(value = "/getAllStu/")
    public CommonResult<List<Student>> getAllStu() {
        List<Student> studenstudentList = studentService.list();
        return CommonResult.success(studenstudentList);
    }

    @PostMapping(value = "/createStu/")
    public CommonResult<Integer> createStu(@RequestBody Student student) {
        student.setId(ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE));
        int result = studentService.createStu(student);
        return CommonResult.success(result);
    }

    @PostMapping(value = "/userDept/")
    public CommonResult<UserDept> getUserDept(@RequestBody UserDept userDept) {
        log.info("getUserDept for condition: {}", JacksonUtil.obj2Str(userDept));
        userDeptAssembleService.assembleUserDepts();
        return CommonResult.success(userDeptService.getOne(Wrappers.emptyWrapper()));
    }
}
