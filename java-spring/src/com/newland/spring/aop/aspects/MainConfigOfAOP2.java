package com.newland.spring.aop.aspects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Author: leell
 * Date: 2022/9/2 09:27:45
 */
@EnableAspectJAutoProxy
@Configuration
public class MainConfigOfAOP2 {
    //业务逻辑类加入容器中
    @Bean("mathCalculator")
    public MathCalculator calculator(){
        return new MathCalculator();
    }

    //切面类加入到容器中
    @Bean
    public LogAspects2 logAspects(){
        return new LogAspects2();
    }
}
