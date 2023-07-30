/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.gateway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lew.gateway.model.Teacher;

import java.util.List;

/**
 * @author Yolen
 * @date 2022/5/21
 */
public interface TeacherService extends IService<Teacher> {
    List<Teacher> selectByCursor();

    Integer queryCount();
}
