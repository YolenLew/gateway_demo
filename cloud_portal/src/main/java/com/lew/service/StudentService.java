package com.lew.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lew.model.Student;

/**
 * @author Yolen
 * @date 2022/5/21
 */
public interface StudentService extends IService<Student> {
    Student getStuDetailById(Long id);

    Student selectOneById(Long id);

    Student getStuById(Long id);
}
