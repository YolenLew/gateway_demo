package com.lew.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lew.model.Student;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDao extends BaseMapper<Student> {
    Student getStuDetailById(Long id);

    @ResultMap("mybatis-plus_Student")
//    @Select("SELECT * FROM t_student WHERE id=#{id}")
    Student selectOneById(Long id);
}