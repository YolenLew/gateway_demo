package com.lew.controller;

import com.lew.model.User;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Yolen
 * @date 2022/5/6
 */
public class JsonToYamlTest {
    private static final Yaml YAML = new Yaml();
    private static final String userJsonStr = "{\n" +
            "            \"id\": 9,\n" +
            "            \"name\": \"后台管理员\",\n" +
            "            \"wxCard\": \"wechat666\",\n" +
            "            \"type\": 3,\n" +
            "            \"title\": \"\",\n" +
            "            \"workYear\": \"\",\n" +
            "            \"projectNumber\": 0,\n" +
            "            \"avgScore\": 0,\n" +
            "            \"avgScoreByA\": null,\n" +
            "            \"dailySalary\": 0,\n" +
            "            \"managerRank\": 0,\n" +
            "            \"isDeleted\": 0,\n" +
            "            \"imageHeadUrl\": \"http://175.24.60.137:9000/kkb/1620101368747_%E5%B1%B1%E6%9C%89%E6%9C%A8%E5%85%AE.jpg\",\n" +
            "            \"createdTime\": \"2021-05-08T23:49:31.000+00:00\",\n" +
            "            \"updatedTime\": \"2021-05-08T23:53:26.000+00:00\",\n" +
            "            \"revision\": 0,\n" +
            "            \"loginId\": 11\n" +
            "        }";

    @Test
    public void testJsonToYaml() throws IOException {
        Map<String, Object> userMap = YAML.load(userJsonStr);
        userMap.entrySet().forEach(System.out::println);
        User user = YAML.loadAs(userJsonStr, User.class);
        System.out.println(user);
        String rootPath = this.getClass().getClassLoader().getResource("").getFile();

        File file = new File("src/test/resources/user.txt");
//        File file = new File(rootPath, "user.txt");
        file.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(userJsonStr);
        bufferedWriter.newLine();//按行
        bufferedWriter.close();
    }
}
