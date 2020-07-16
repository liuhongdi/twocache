package com.twocache.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(3)
public class CostTimeAspect {

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.twocache.demo.controller.*.*(..))")
    private void pointcut() {}

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        //System.out.println("------costtime aop doBefore begin");
        startTime.set(System.currentTimeMillis());
    }

    //和doBefore搭配，得到使用的时间
    @AfterReturning(returning = "ret" , pointcut = "pointcut()")
    public void doAfterReturning(Object ret){
        //System.out.println("------costtime aop doAfterReturning begin");
        System.out.println("costtime aop 方法doafterreturning:毫秒数:"+ (System.currentTimeMillis() - startTime.get()));
    }

}
