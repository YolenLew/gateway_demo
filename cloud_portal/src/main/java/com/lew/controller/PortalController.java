package com.lew.controller;

import com.lew.entity.CommonResult;
import com.lew.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author Yolen
 * @date 2022/2/9
 */
@RestController
@RequestMapping(value = "/portal")
public class PortalController {

    private CommonResult<List<User>> property;

    private CommonResult<User> singleNested;

    @GetMapping(value = "/health")
    public CommonResult<List<User>> health() {
        User user = new User();
        user.setAge(10);
        user.setName("Tom");
        User user2 = new User();
        user2.setAge(10);
        user2.setName("Tom");
        List<User> users = Arrays.asList(user, user2);
        return CommonResult.success(users);
    }

    @GetMapping(value = "/singleNested")
    public CommonResult<User> singleNested() {
        User user = new User();
        user.setAge(10);
        user.setName("Tom");
        return CommonResult.success(user);
    }
}
