/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lew.gateway.model.UserDept;
import org.springframework.stereotype.Repository;

/**
 * 持久层接口
 *
 * @author Yolen
 * @date 2022/7/6
 */
@Repository
public interface UserDeptDao extends BaseMapper<UserDept> {
}