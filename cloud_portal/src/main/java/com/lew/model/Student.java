package com.lew.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
@TableName(value = "t_student", autoResultMap = true)
public class Student {
    private Long id;
    private String name;
    private Double avgScore;
    private Date createdTime;
    private Date updatedTime;
    private Integer roleType;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private RoleRange roleRange;
    @TableField(exist = false)
    private List<Teacher> teachers;
}
