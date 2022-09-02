package com.newland.spring.aop.service.impl;

/**
 * Author: leell
 * Date: 2022/9/2 09:10:01
 */
public class NiaoServiceBean {
    public NiaoServiceBean(){}
    public void save(String name) {
        if("3".equals(name))throw new RuntimeException();
        System.out.println("我是save()方法");
    }
}
