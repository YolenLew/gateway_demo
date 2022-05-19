package com.lew.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Yolen
 * @date 2022/5/19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_teacher")
public class Teacher {
    private Long id;
    private String name;
    private String workYear;
    private Double dailySalary;
    private Date createdTime;
    private Date updatedTime;
}
