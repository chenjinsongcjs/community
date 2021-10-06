package com.nowcoder.community.exception;

import com.nowcoder.community.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/03/0:24
 * @Description: 统一异常处理,指定处理异常的范围
 */
@RestControllerAdvice(basePackages = "com.nowcoder.community.controller")
@Slf4j
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
    @ExceptionHandler(Exception.class)
    public void exception(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //对同步请求和一般请求进行处理
        log.error("服务器发生异常：{}",e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        String header = request.getHeader("x-requested-with");
        if("XMLHttpRequest".equals(header)){
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSONUtils.getJSONString(1,"服务器发生异常"));
        }else{
            response.sendRedirect(request.getContextPath()+"/error");
        }
    }
}