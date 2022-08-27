package com.newland.trait.java8;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Lambda表达式
 */
public class Lambda {
    public void startThread(){
        //无参数方式
        Thread thread = new Thread(() -> System.out.println("sdfsfd"));
    }
    public void compare(){
        List<String> list= Arrays.asList("c","b","a");
        // 方式一 Lambda表达式 多参数
        Collections.sort(list,(o1, o2)->o1.compareTo(o2));

        // 方式二 方法引用
        Collections.sort(list, String::compareTo);
    }

}
