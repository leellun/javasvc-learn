package com.newland.spring.aop.aspects;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Configuration
@MyAnnotation
public @interface MyConfiguration {
}
