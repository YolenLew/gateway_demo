package com.lew.controller;

import com.alibaba.fastjson.JSON;
import com.lew.dao.StudentDao;
import com.lew.model.Student;
import com.lew.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Yolen
 * @date 2022/5/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserControllerTest {
    public static final String stuJsonStr = "{\"name\":\"zhaoliu\",\"avgScore\":5.6,\"createdTime\":\"2022-05-18T10:04:48.000+00:00\",\"updatedTime\":\"2022-05-21T07:20:05.000+00:00\",\"roleRange\":{\"role\":\"character\",\"skills\":[\"*\"]}}";

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentDao studentMapper;

    @Test
    public void testInsertStudent() {
        Student student = JSON.parseObject(stuJsonStr, Student.class);
        boolean result = studentService.saveOrUpdate(student);
        log.info("saveOrUpdate" + result);
    }

    @Test
    public void testCustomSelectOne() {
        Student student = studentMapper.selectOneById(1L);
        log.info("testCustomSelectOne:{}", student);
    }

    @Test
    public void testUpdateById() {
        Student student = new Student();
        student.setId(1L);
        student.setName("zhangsan-update");
        int updateRows = studentMapper.updateById(student);
        log.info("testUpdateById:{}", updateRows);
    }
}