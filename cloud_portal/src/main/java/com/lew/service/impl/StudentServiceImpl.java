package com.lew.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lew.dao.StudentMapper;
import com.lew.model.Student;
import com.lew.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * @author Yolen
 * @date 2022/5/21
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}
