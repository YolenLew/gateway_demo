/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.gateway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lew.gateway.dao.TeacherDao;
import com.lew.gateway.model.Teacher;
import com.lew.gateway.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yolen
 * @date 2023/7/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl extends ServiceImpl<TeacherDao, Teacher> implements TeacherService {

    private final TeacherDao teacherDao;

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> selectByCursor() {
        long start = System.currentTimeMillis();
        List<Teacher> list = new ArrayList<>();
        Cursor<Teacher> cursor = teacherDao.selectByCursor();
        cursor.forEach(tea -> log.info("selectByCursor: {}", tea.getName()));

        log.info("cost total: {}ms", System.currentTimeMillis() - start);
        return list;
    }

    @Override
    public Integer queryCount() {
        return null;
    }
}
