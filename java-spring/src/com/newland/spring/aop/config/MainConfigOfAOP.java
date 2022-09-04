package com.newland.spring.aop.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Author: leell
 * Date: 2022/9/2 09:27:45
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.newland.spring.aop.service")
public class MainConfigOfAOP {
}
