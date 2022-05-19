package com.lew.model;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author Yolen
 * @date 2022/5/15
 */
@Data
public class Person {
    private String name;
    private Integer age;
    private Map<String, Object> details = null;

    public Object getCompany() {
        if (CollectionUtils.isEmpty(details)) {
            return "";
//            throw new RuntimeException("empty details");
        }
        return details.get("company");
    }
}
