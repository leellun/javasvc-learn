package com.newland.spring.aop.aspects;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Author: leell
 * Date: 2022/9/2 09:32:12
 */
public class LogAspects2Test {
    public static void main(String[] args) {
        test02();
    }
    public static void test02(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAOP2.class);
        MathCalculator mathCalculator = applicationContext.getBean(MathCalculator.class);
        mathCalculator.div(1, 1);
        Calculator calculator=(Calculator) mathCalculator;
        System.out.println(calculator.getString());
        applicationContext.close();
    }
    @Test
    public void test01(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAOP.class);
        MathCalculator mathCalculator = applicationContext.getBean(MathCalculator.class);
        mathCalculator.div(1, 1);
        applicationContext.close();
    }
    @Test
    public void test(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        applicationContext.close();
    }
}
