package com.lew.bean;

import com.lew.dao.StudentDao;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

/**
 * @author Yolen
 * @date 2022/5/31
 */
public class MockBeanProvider {
    @Primary
    public StudentDao studentDao() {
        return mock(StudentDao.class);
    }
}
