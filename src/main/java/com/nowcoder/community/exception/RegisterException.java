package com.nowcoder.community.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/0:24
 * @Description: 统一异常处理,指定处理异常的范围
 */
//@RestControllerAdvice(basePackages = "com.nowcoder.community.controller")
public class RegisterException {
    /**
    * @Description: 校验异常
    * @Param: [exception]
    * @return: [org.springframework.web.bind.MethodArgumentNotValidException]
    * @Author: 陈进松
    * @Date: 2021/10/3
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> registerException(MethodArgumentNotValidException exception){
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            System.out.println(fieldError.getField()+"-->"+fieldError.getDefaultMessage());
        }
        return null;
    }
    /**
    * @Description: 捕获所有异常
    * @Param: [t]
    * @return: [java.lang.Throwable]
    * @Author: 陈进松
    * @Date: 2021/10/3
    */
    @ExceptionHandler(Throwable.class)
    public String excption(Throwable t){
        return t.getMessage();
    }
}