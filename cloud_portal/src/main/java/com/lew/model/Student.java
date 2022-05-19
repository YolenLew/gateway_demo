package com.lew.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author Yolen
 * @date 2022/5/19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_student")
public class Student {
    private Long id;
    private String name;
    private Double avgScore;
    private Date createdTime;
    private Date updatedTime;
    @TableField(exist = false)
    private List<Teacher> teachers;
}
