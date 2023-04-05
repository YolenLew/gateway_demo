package com.lew.mbg.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 老师信息表
 * </p>
 *
 * @author ${author}
 * @since 2023-04-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TTeacher implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户ID 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 工作年限
     */
    private String workYear;

    /**
     * 日薪
     */
    private BigDecimal dailySalary;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;


}
