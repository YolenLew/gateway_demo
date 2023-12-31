package com.lew.gateway.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Yolen
 * @date 2022/5/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user_dept")
public class UserDept {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_no")
    private String userNo;

    @TableField("user_name")
    private String userName;

    @TableField("user_role")
    private String userRole;

    @TableField("county_id")
    private String countyId;

    @TableField("county_name")
    private String countyName;

    @TableField("dept_id")
    private String deptId;

    @TableField("dept_name")
    private String deptName;

    @TableField("city_id")
    private String cityId;

    @TableField("city_name")
    private String cityName;

    @TableField("province_id")
    private String provinceId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "modify_time", fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;

    @TableField("user_id")
    private String userId;

}
