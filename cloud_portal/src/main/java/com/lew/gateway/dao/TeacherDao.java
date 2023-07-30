/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.dao;

import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lew.gateway.model.Teacher;

/**
 * @author  Yolen
 * @date  2023/7/28
 */
@Repository
public interface TeacherDao extends BaseMapper<Teacher> {
    Cursor<Teacher> selectByCursor();

    Integer queryCount();
}