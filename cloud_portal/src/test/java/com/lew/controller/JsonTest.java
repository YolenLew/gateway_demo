package com.lew.controller;

import com.alibaba.fastjson.JSON;
import com.aspose.cells.Workbook;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lew.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Yolen
 * @date 2022/5/15
 */
@Slf4j
public class JsonTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    public void testObjToJson() {
        Person person = new Person();
        log.info("new person:{}", person);
        String personJsonStr = JSON.toJSONString(person);
        log.info("person json:{}", personJsonStr);
        assertNull(person.getAge());
    }

    @Test
    public void testJsonToMdTable() {
        String mdTableStr = "| id | name    | age | gender |\n" +
                "|----|---------|-----|--------|\n" +
                "| 1  | Roberta | 39  | M      |\n" +
                "| 2  | Oliver  | 25  | M      |\n" +
                "| 3  | Shayna  | 18  | F      |\n" +
                "| 4  | Fechin  | 18  | M      |\n";
        System.out.println(mdTableStr);
        System.out.println("a" + System.lineSeparator() + "b");
        assertNotNull(mdTableStr);
    }

    @Test
    public void testAspose() throws Exception {
        Workbook workbook = new Workbook("C:\\Users\\Yolen\\Desktop\\user.json");
        workbook.save("user.md");
        assertNotNull(workbook);
    }

    @Test
    public void testArray() {
        List<String> list = new ArrayList<>();
        int size = 100;
        for (int i = 0; i < size; i++) {
            list.add(list.size(), i + "");
        }
        assertEquals(size, list.size());
    }

    @Test
    public void dateTimeToJson() throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.now();

        Date date = new Date();
        String dateToJson01 = MAPPER.writeValueAsString(date);
        assertEquals(String.valueOf(date.toInstant().toEpochMilli()), dateToJson01);
        log.info("dateToJson01: {}", dateToJson01);
        log.info("localDateTime: {}", MAPPER.writeValueAsString(localDateTime));

        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String dateToJson02 = MAPPER.writeValueAsString(date);
        log.info("dateToJson02: {}", dateToJson02);
        log.info("localDateTime: {}", MAPPER.writeValueAsString(localDateTime));
    }
}
