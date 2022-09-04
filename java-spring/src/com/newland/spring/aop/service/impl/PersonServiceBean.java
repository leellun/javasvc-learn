package com.newland.spring.aop.service.impl;

import com.newland.spring.aop.service.PersonService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: leell
 * Date: 2022/9/2 09:00:48
 */
@Service
@Transactional
public class PersonServiceBean implements PersonService {
    public PersonServiceBean(){}
    @Transactional
    public void save(String name) {
        if("3".equals(name))throw new RuntimeException();
        System.out.println("我是save()方法");
        commit();
    }
    public void commit(){
        System.out.println(this);
    }
}
