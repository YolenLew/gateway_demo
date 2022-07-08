package com.lew.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lew.common.entity.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;

/**
 * @author Yolen
 * @date 2022/2/2
 */
@Slf4j
@Validated
@RestController
@RequestMapping(value = "/demo")
public class DemoController {

    private final RestTemplate restTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${appName:null}")
    private String appName;

    @Autowired
    public DemoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @GetMapping(value = "/hello")
    public String hello(@NotBlank(message = "empty name") String name) {
        try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) {e.printStackTrace();}
        log.info("DemoController receive parameter: {}", name);
        log.info("DemoController appName parameter: {}", appName);
        return name + appName;
    }

    @GetMapping(value = "/postForObject")
    public CommonResult<Object> postForObject() {
        Object result = restTemplate.getForObject("http://localhost:8093/portal/health", Object.class);
        return CommonResult.success(result);
    }
}
