package com.lew.controller;

import cn.hutool.core.map.MapUtil;
import com.lew.common.entity.CommonResult;
import com.lew.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.foundation.common.part.FilePart;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Yolen
 * @date 2022/2/9
 */
@Slf4j
@Validated
@RestController
@RequestMapping(value = "/portal")
public class PortalController {

    private CommonResult<List<User>> property;

    private CommonResult<User> singleNested;

    private CommonResult rawProperty;

    private String filePath = "D:\\Temp\\excel.xlsx";

    @PostMapping(value = "filepart", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public FilePart filePart(HttpServletRequest request) {
        String filePath = "D:\\Temp\\excel.xlsx";
        File file = new File(filePath);
        return new FilePart(file.getName(), file);
    }


    @PostMapping(value = "excel", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> excel(Long id)
            throws IOException {
        String filePath = "D:\\Temp\\excel.xlsx";
        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(file.getInputStream()));
    }

    @GetMapping(value = "/health")
    public CommonResult<List<User>> health() {
        User user = new User();
        user.setAge(10);
        user.setName("Tom");
        User user2 = new User();
        user2.setAge(10);
        user2.setName("Tom");
        List<User> users = Arrays.asList(user, user2);
        return CommonResult.success(users);
    }

    @GetMapping(value = "/singleNested")
    public CommonResult<User> singleNested() {
        User user = new User();
        user.setAge(10);
        user.setName("Tom");
        return CommonResult.success(user);
    }

    @PostMapping(value = "/ids/post/")
    public CommonResult<List<String>> postIds(@RequestBody List<String> idList) {
        log.info("idList: {}", idList);
        return CommonResult.success(idList);
    }

    @GetMapping(value = "/ids/get/")
    public CommonResult<List<String>> getIds(@RequestParam(value = "ids") List<String> ids) {
        log.info("idList: {}", ids);
        return CommonResult.success(ids);
    }

    @GetMapping(value = "/ids/get/str/")
    public CommonResult<Map<String, String>> getStrIds(@Pattern(regexp = "^[A-Za-z0-9.]{1,32}(,[A-Za-z0-9.]{1,32})*$") @RequestParam(value = "ids") String ids, @RequestParam(value = "name") String name) {
        log.info("ids: {}, name: {}", ids, name);
        return CommonResult.success(MapUtil.<String, String>builder().put("ids", ids).put("name", name).map());
    }
}
