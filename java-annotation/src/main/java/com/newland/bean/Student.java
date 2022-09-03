package com.newland.bean;

import com.newland.annotation.Field;
import com.newland.annotation.MyBean;
import com.newland.annotation.MyComponent;
import com.newland.annotation.MyConstructor;

/**
 * Author: leell
 * Date: 2022/9/3 15:58:10
 */
@MyBean
public class Student {

    @Field(value = {"小明", "小红"})
    private String name;

    public Student() {
    }

    @MyConstructor("这是name构造方法")
    public Student(String name) {
        this.name = name;
    }
}
