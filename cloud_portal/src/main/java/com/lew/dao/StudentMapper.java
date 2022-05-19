package com.lew.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lew.model.Student;


public interface StudentMapper extends BaseMapper<Student> {
    Student getStuDetailById(Long id);
}