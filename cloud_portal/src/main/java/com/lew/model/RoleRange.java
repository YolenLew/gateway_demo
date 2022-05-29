package com.lew.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author Yolen
 * @date 2022/5/21
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleRange {
    private String role;
    private List<String> skills;
}
