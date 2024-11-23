package com.example.seckill_backend.exception;

import com.example.seckill_backend.entity.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseBody
    public Response handleMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        return new Response(406, "响应内容格式不被接受：" + ex.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleException(Exception ex) {
        return new Response(500, "服务器内部错误：" + ex.getMessage(), null);
    }
}
