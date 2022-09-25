package com.newland.spring.aop.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;

import java.util.Arrays;

/**
 * Author: leell
 * Date: 2022/9/2 09:28:19
 */
@Aspect
public class LogAspects22 implements Ordered {
    public int getOrder() {
        return 1;
    }
    //抽取公共的切入点表达式
    //1、本类引用
    //2、其他的切面引用
    @Pointcut("execution(public int com.newland.spring.aop.aspects.MathCalculator.*(..))")
    public void pointCut(){};

    //@Before在目标方法之前切入；切入点表达式（指定在哪个方法切入）
    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        System.out.println(""+joinPoint.getSignature().getName()+"运行。。。@Before:参数列表是：{"+ Arrays.asList(args)+"}");
    }

    @After("com.newland.spring.aop.aspects.LogAspects22.pointCut()")
    public void logEnd(JoinPoint joinPoint){
        System.out.println(""+joinPoint.getSignature().getName()+"结束。。。@After");
    }

    //JoinPoint一定要出现在参数表的第一位
    @AfterReturning(value="pointCut()",returning="result")
    public void logReturn(JoinPoint joinPoint,Object result){
        System.out.println(""+joinPoint.getSignature().getName()+"正常返回。。。@AfterReturning:运行结果：{"+result+"}");
    }

    @AfterThrowing(value="pointCut()",throwing="exception")
    public void logException(JoinPoint joinPoint,Exception exception){
        System.out.println(""+joinPoint.getSignature().getName()+"异常。。。异常信息：{"+exception+"}");
    }

}
