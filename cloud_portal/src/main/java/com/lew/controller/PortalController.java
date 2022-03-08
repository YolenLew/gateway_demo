package com.lew.controller;

import com.lew.entity.CommonResult;
import com.lew.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.foundation.common.part.FilePart;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Yolen
 * @date 2022/2/9
 */
@Slf4j
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
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
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
}
