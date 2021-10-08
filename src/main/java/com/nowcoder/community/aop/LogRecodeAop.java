package com.nowcoder.community.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/1:02
 * @Description:
 */
@Slf4j
@Aspect
@Component
public class LogRecodeAop {
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointCut(){
    }
    @Before("pointCut()")
    public void before(JoinPoint joinPoint){
        //日志格式为：ip 时间 调用方法
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null)
            return;
        HttpServletRequest request = requestAttributes.getRequest();
        String remoteHost = request.getRemoteHost();
        String name = joinPoint.getSignature().getDeclaringTypeName()+"." + joinPoint.getSignature().getName();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        log.info(String.format("用户:[%s],时间:[%s],调用服务:[%s]",remoteHost,now,name));
    }
}