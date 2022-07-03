package com.lew.controller;

import com.lew.dao.StudentDao;
import com.lew.dao.UserDao;
import com.lew.entity.CommonResult;
import com.lew.model.Student;
import com.lew.model.User;
import com.lew.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;

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
}
