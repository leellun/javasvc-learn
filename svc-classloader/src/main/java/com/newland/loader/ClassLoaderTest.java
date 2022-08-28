package com.newland.loader;


import com.newland.test.HelloWorld;

/**
 * Author: leell
 * Date: 2022/8/28 23:52:10
 */
public class ClassLoaderTest {
    public static void main(String[] args) {
        RestartClassLoader loader = new RestartClassLoader();
        RestartClassLoader loader2 = new RestartClassLoader();
        Class<?> clazz = loader.findClass(HelloWorld.class);
        Class<?> clazz2 = loader2.findClass(HelloWorld.class);
        System.out.println(clazz == clazz2);
    }
}
