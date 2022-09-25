/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.dao;

import com.lew.gateway.model.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yolen
 * @date 2022/3/8
 */
@Repository
public interface UserDao {
    @Select(value = "select * from user")
    List<User> findAll();

    User findByUserId(Long id);
}
