/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lew.common.entity.CommonResult;
import com.lew.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Yolen
 * @date 2023/7/25
 */
@Slf4j
public class JacksonUtilTest {
    public static final String USER_JSON = "{\"name\":\"lew\",\"gender\":\"男\",\"age\":1}";

    private static User user;

    @Before
    public void before() {
        user = JacksonUtil.str2Obj(USER_JSON, User.class);
    }

    @Test
    public void testJsonObjAndList() {
        User user1 = new User();
        user1.setAge(1);
        user1.setName("lew");
        String userJsonstr = JacksonUtil.obj2Str(user1);
        log.info("userJson: {}", userJsonstr);

        User user2 = JacksonUtil.str2Obj(userJsonstr, User.class);
        assertNotNull(user2);

        user2.setAge(2);
        user2.setName("yolen");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        String userListJson = JacksonUtil.obj2Str(userList);

        List<User> userListBean = JacksonUtil.str2Obj(userListJson, new TypeReference<List<User>>() {});
        assertTrue(CollectionUtils.isNotEmpty(userListBean));
        userListBean.forEach(user -> log.info("{}-{}", user.getAge(), user.getName()));

        List<User> userListBean2 = JacksonUtil.str2Collection(userListJson, List.class, User.class);
        assertTrue(CollectionUtils.isNotEmpty(userListBean2));
        userListBean2.forEach(user -> log.info("{}-{}", user.getAge(), user.getName()));

        List<User> userListBean3 = JacksonUtil.str2List(userListJson, User.class);
        assertTrue(CollectionUtils.isNotEmpty(userListBean3));
        userListBean3.forEach(user -> log.info("{}-{}", user.getAge(), user.getName()));
    }

    @Test
    public void testCommonResult() {
        CommonResult<User> result1 = CommonResult.success(user);
        CommonResult<User> result2 = CommonResult.forbidden(user);
        CommonResult<User> result3 = CommonResult.unauthorized(user);
        String resultsJson = JacksonUtil.obj2Str(Arrays.asList(result1, result2, result3));
        assertNotNull(resultsJson);

        List<CommonResult<User>> results = JacksonUtil.str2Obj(resultsJson, new TypeReference<List<CommonResult<User>>>(){});
        log.info("CommonResult: {}", JacksonUtil.obj2Str(results));

        List<CommonResult<User>> results2 = JacksonUtil.str2GenericObj(resultsJson, List.class, CommonResult.class,
            User.class);
        assertTrue(CollectionUtils.isNotEmpty(results2));
    }

    @Test
    public void testListTypeRef() {
        User user1 = JacksonUtil.str2Obj(USER_JSON, User.class);
        User user2 = JacksonUtil.str2Obj(USER_JSON, User.class);
        String usersJson = JacksonUtil.obj2Str(Arrays.asList(user1, user2));
        assertNotNull(usersJson);

        List<User> users = JacksonUtil.str2Obj(usersJson, JacksonUtil.<User>getListTypeRef());
        log.info("users: {}", JacksonUtil.obj2Str(users));
        assertTrue(CollectionUtils.isNotEmpty(users));
    }

    @Test
    public void testStr2HashMap() {
        Map<String, Object> userMap = JacksonUtil.str2HashMap(USER_JSON, String.class, Object.class);
        log.info("userMap: {}", JacksonUtil.obj2Str(userMap));

        assertTrue(MapUtils.isNotEmpty(userMap));
        assertTrue(userMap.containsKey("name"));
    }

    @Test
    public void testMap2Bean() {
        Map<String, Object> userMap = JacksonUtil.str2HashMap(USER_JSON, String.class, Object.class);
        log.info("userMap: {}", JacksonUtil.obj2Str(userMap));

        User user = JacksonUtil.map2Bean(userMap, User.class);
        log.info("user: {}", JacksonUtil.obj2Str(user));
        assertNotNull(user);
    }
}