
/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.gateway.assemble;

import cn.hutool.core.text.StrPool;
import com.lew.common.constant.DistrictLevel;
import com.lew.common.util.DefaultListUtil;
import com.lew.gateway.model.SysDept;
import com.lew.gateway.model.UserDept;
import com.lew.gateway.service.impl.UserDeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Yolen
 * @date 2023/12/10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDeptAssembleService {
    private final UserDeptService userDeptService;

    public void assembleUserDepts() {
        // 查询所有员工列表
        List<UserDept> userDeptList = userDeptService.list();

        if (CollectionUtils.isEmpty(userDeptList)) {
            log.warn("get empty user dept info from database!");
            return;
        }

        // 针对空的县填充默认标识信息
        userDeptList.forEach(e -> {
            if (StringUtils.isBlank(e.getCountyId())) {
                e.setCityId("Other_" + e.getCityId());
                e.setCountyName("其他_" + e.getCityId());
            }
        });

        // 列表分类聚合：县部门
        Map<String, List<UserDept>> countyDeptMap = userDeptList.stream().collect(
            Collectors.groupingBy(UserDept::getCountyId, Collectors.collectingAndThen(Collectors.toList(),
                userList -> DefaultListUtil.distinctByField(userList, UserDept::getUserId))));

        // 列表分类聚合：城市部门
        Map<String, List<UserDept>> cityDeptMap = userDeptList.stream().collect(
            Collectors.groupingBy(UserDept::getCityId, Collectors.collectingAndThen(Collectors.toList(),
                userList -> DefaultListUtil.distinctByFields(userList, UserDept::getUserId, UserDept::getCountyId))));

        // 列表分类聚合：省份部门
        Map<String, List<UserDept>> provinceDeptMap = userDeptList.stream().collect(
            Collectors.groupingBy(UserDept::getProvinceId, Collectors.collectingAndThen(Collectors.toList(),
                userList -> DefaultListUtil.distinctByFields(userList, UserDept::getUserId, UserDept::getCountyId,
                    UserDept::getCityId))));

        // 组装部门层级关系信息
        // 县部门组织关系
        for (Entry<String, List<UserDept>> deptEntry : countyDeptMap.entrySet()) {
            List<UserDept> deptEntryValue = deptEntry.getValue();
            SysDept sysDept = new SysDept();
            sysDept.setDeptId(deptEntry.getKey());
            sysDept.setDeptCount(deptEntryValue.size());

            sysDept.setDeptName(
                deptEntryValue.stream().map(UserDept::getCountyName).filter(StringUtils::isNotBlank).findFirst()
                    .orElse(StringUtils.EMPTY));
            sysDept.setDeptType(DistrictLevel.COUNTY.getCode());
            sysDept.setParentId(
                deptEntryValue.stream().map(UserDept::getCityId).filter(StringUtils::isNotBlank).findFirst()
                    .orElse(StringUtils.EMPTY));
            String superParentId = "0";
            String firstParentId =
                deptEntryValue.stream().map(UserDept::getCityId).filter(StringUtils::isNotBlank).findFirst()
                    .orElse(StringUtils.EMPTY);
            String ancessors = Stream.of(superParentId, firstParentId).filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(StrPool.COMMA));
            sysDept.setAncestors(ancessors);
        }
    }
}
