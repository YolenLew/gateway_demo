/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lew.gateway.model.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Repository;

/**
 * 持久层接口
 *
 * @author Yolen
 * @date 2022/7/6
 */
@Repository
public interface StudentDao extends BaseMapper<Student> {
    Student getStuDetailById(Long id);

    @ResultMap("mybatis-plus_Student")
    Student selectOneById(Long id);

    /**
     * 检查并新增学生信息
     *
     * @param student         新建的学生对象
     * @param maxStuInsertNum 可新增的最大数量
     * @return
     */
    int checkAndCreateStu(@Param(value = "student") Student student, @Param(value = "maxStuInsertNum") Integer maxStuInsertNum);
}