package com.newland.spring.aop.service.impl;

import com.newland.spring.aop.service.PersonService;

/**
 * Author: leell
 * Date: 2022/9/2 09:00:48
 */
public class PersonServiceBean implements PersonService {
    public PersonServiceBean(){}
    public void save(String name) {
        if("3".equals(name))throw new RuntimeException();
        System.out.println("我是save()方法");
    }
}
