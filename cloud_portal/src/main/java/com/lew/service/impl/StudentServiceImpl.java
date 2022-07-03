package com.lew.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lew.dao.StudentDao;
import com.lew.model.Student;
import com.lew.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * @author Yolen
 * @date 2022/5/21
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentDao, Student> implements StudentService {
    private final StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Student getStuDetailById(Long id) {
        return studentDao.getStuDetailById(id);
    }

    @Override
    public Student selectOneById(Long id) {
        return studentDao.selectOneById(id);
    }

    @Override
    public Student getStuById(Long id) {
        return studentDao.selectOneById(id);
    }
}
