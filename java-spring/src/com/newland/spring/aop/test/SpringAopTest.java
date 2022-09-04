package com.newland.spring.aop.test;

import com.newland.spring.aop.config.MainConfigOfAOP;
import com.newland.spring.aop.service.PersonController;
import com.newland.spring.aop.service.PersonService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Author: leell
 * Date: 2022/9/4 00:01:14
 */
public class SpringAopTest {

    @Test
    public void test(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAOP.class);
        PersonController personService = applicationContext.getBean(PersonController.class);
        personService.save("sdfsdfsdf");
    }
}
