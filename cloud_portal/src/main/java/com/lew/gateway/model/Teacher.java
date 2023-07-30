/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String workYear;
    private Double dailySalary;
    private Date createdTime;
    private Date updatedTime;
}
