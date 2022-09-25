/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.exception;

import com.lew.common.entity.CommonResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常处理 ，并返回封装好的CommonResult对象；
 *
 * @since 2022/07/28
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 方法参数无效异常 处理方法
     *
     * @param e 方法参数无效对象
     * @return 返回用户可理解的错误消息对象
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult<String> handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }

    /**
     * 绑定错误异常
     *
     * @param e 抛出异常对象
     * @return 返回用户可理解的错误消息对象
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public CommonResult<String> handleValidException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }

    /**
     * 方法参数无效异常处理方法
     *
     * @param e 方法参数无效对象
     * @return 返回用户可理解的错误消息对象
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public CommonResult<String> handleValidException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuffer sb = new StringBuffer();
        for (ConstraintViolation<?> item : constraintViolations) {
            sb.append(item.getMessage()).append(";");
        }
        return CommonResult.validateFailed(sb.toString());
    }
}
