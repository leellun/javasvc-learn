package com.newland.spring.aop.aspects;

import org.springframework.stereotype.Component;

/**
 * Author: leell
 * Date: 2022/9/3 22:54:48
 */
public class Person {
    private String name = "23423423424324234";

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name + getAge();
    }

    public int getAge() {
        return 1;
    }
}
