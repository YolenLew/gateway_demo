package com.lew.controller;

import com.lew.dao.UserDao;
import com.lew.entity.CommonResult;
import com.lew.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yolen
 * @date 2022/3/8
 */
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @Autowired
    private UserDao userDao;

    @GetMapping(value = "findAll")
    public CommonResult<List<User>> findAll() {
        List<User> userList = userDao.findAll();
        log.info("userList size:{}", userList.size());
        return CommonResult.success(userList);
    }
}
