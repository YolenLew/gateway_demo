package com.lew.controller;


import com.lew.bean.MockBeanProvider;
import com.lew.dao.StudentDao;
import com.lew.model.Student;
import com.lew.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author Yolen
 * @date 2022/5/31
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MockControllerTest {
    @Autowired
    private StudentService studentService;

    @Mock
    private StudentDao studentDao;

    @Test
    public void getStuById() {
        Student student = new Student();
        student.setId(1L);
        student.setName("nameForTest");
        Mockito.when(studentDao.selectOneById(1L)).thenReturn(student);

        Student studentById = studentService.getStuById(1L);
        Assert.assertNotNull(studentById);
        Assert.assertEquals(studentById.getId(), student.getId());
        Assert.assertEquals(studentById.getName(), student.getName());
    }
}
