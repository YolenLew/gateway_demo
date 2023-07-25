/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lew.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Yolen
 * @date 2023/7/25
 */
@Slf4j
public class JacksonUtilTest {
    @Test
    public void testJsonObjAndList() {
        User user1 = new User();
        user1.setAge(1);
        user1.setName("lew");
        String userJsonstr = JacksonUtil.obj2String(user1);
        log.info("userJson: {}", userJsonstr);

        User user2 = JacksonUtil.string2Obj(userJsonstr, User.class);
        assertNotNull(user2);

        user2.setAge(2);
        user2.setName("yolen");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        String userListJson = JacksonUtil.obj2String(userList);

        List<User> userListBean = JacksonUtil.string2Obj(userListJson, new TypeReference<List<User>>() {});
        assertTrue(CollectionUtils.isNotEmpty(userListBean));
        userListBean.forEach(user -> log.info("{}-{}", user.getAge(), user.getName()));

        List<User> userListBean2 = JacksonUtil.str2Collection(userListJson, List.class, User.class);
        assertTrue(CollectionUtils.isNotEmpty(userListBean2));
        userListBean2.forEach(user -> log.info("{}-{}", user.getAge(), user.getName()));
    }
}