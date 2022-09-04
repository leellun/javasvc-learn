package com.newland.bean;

/**
 * Author: leell
 * Date: 2022/9/4 10:21:15
 */
public class Animal {
    public static class Cat{

    }
    public class Cat1{

    }
    Cat1 getCat1(){
        return new Cat1();
    }
    public static void main(String[] args) {
        System.out.println(Cat.class.getEnclosingClass());
        System.out.println(new Animal().getCat1().getClass().getEnclosingClass());
    }
}
