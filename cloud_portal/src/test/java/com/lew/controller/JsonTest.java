package com.lew.controller;

import com.alibaba.fastjson.JSON;
import com.aspose.cells.Workbook;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lew.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yolen
 * @date 2022/5/15
 */
@Slf4j
public class JsonTest {

    @Test
    public void testObjToJson() {
        Person person = new Person();
        log.info("new person:{}", person);
        String personJsonStr = JSON.toJSONString(person);
        log.info("person json:{}", personJsonStr);
    }

    @Test
    public void testJsonToMdTable() {
        String json = "{}";
        String mdTableStr = "| id | name    | age | gender |\n" +
                "|----|---------|-----|--------|\n" +
                "| 1  | Roberta | 39  | M      |\n" +
                "| 2  | Oliver  | 25  | M      |\n" +
                "| 3  | Shayna  | 18  | F      |\n" +
                "| 4  | Fechin  | 18  | M      |\n";
        System.out.println(mdTableStr);

        System.out.println("a" + System.lineSeparator() + "b");
    }

    @Test
    public void testAspose() throws Exception {
        Workbook workbook = new Workbook("C:\\Users\\Yolen\\Desktop\\user.json");
        workbook.save("user.md");
    }

    @Test
    public void testArray() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(list.size(), i + "");
        }
//        list.add(126,"126");
        System.out.println(list);
    }
}
