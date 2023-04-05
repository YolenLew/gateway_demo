package com.lew.mbg.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 关系表
 * </p>
 *
 * @author ${author}
 * @since 2023-04-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TStudentTeacherRelation implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long stuId;

    private Long teaId;


}
