package com.lew.mbg.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 学生信息表 ：学生的详细信息
 * </p>
 *
 * @author ${author}
 * @since 2023-04-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TStudent implements Serializable {

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
     * 能力平均分数？星
     */
    private BigDecimal avgScore;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 角色类型
     */
    private Integer roleType;

    /**
     * 角色技能
     */
    private String roleRange;


}
