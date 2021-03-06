package com.example.examsys.Support.JWT;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常捕获，主要是为了屏蔽掉奇怪的HttpMessageConversionException
//@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        if (e instanceof HttpMessageConversionException) {
            System.out.println("HttpMessageConversionException");
            return null;
        }
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "服务器出错";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", msg);
        return jsonObject;
    }
}