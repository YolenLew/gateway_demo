/*
 * Copyright (c) 2022. This is my custom copyright information.$today.year-2022. All rights reserved.
 */

package com.lew.gateway.controller;

import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Yolen
 * @date 2022/10/12
 */
public class JsonTest {
    @Test
    public void jsonToObj() {
        String json = "{\"code\":0,\"data\":{\"context\":{\"output\":\"\"," +
                "\"input\":{\"inspection\":\"{\\\"id\\\":\\\"uuidxxxx\\\",\\\"tag\\\":\\\"imp\\\"}\"}}," +
                "\"session_id\":\"\",\"items\":[{\"itemstring\":\"手机\",\"itemcoord\":{\"x\":0,\"width\":40,\"y\":100," +
                "\"height\":20}}]},\"message\":\"OK\"}";
        JSONObject jsonObject = JSON.parseObject(json);
        System.out.println(jsonObject);

        String unescapeJava = StringEscapeUtils.unescapeJava(json);
        System.out.println(unescapeJava);
    }
}
