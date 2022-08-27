package com.newland.trait.java8;

import java.util.function.Function;

/**
 * 函数式接口(Functional Interface)就是一个有且仅有一个抽象方法，但是可以有多个非抽象方法的接口。
 * 函数式接口可以被隐式转换为 lambda 表达式。
 * Lambda 表达式和方法引用（实际上也可认为是Lambda表达式）上。
 * 无论接口声明中是否标记了@FunctionalInterface注解，编译器都会将满足函数接口定义的任何接口视为函数接口。
 */
public class FunctionalInterfaceTest {
    private String str = "";

    public void setStr(String str) {
        this.str = str;
    }

    @FunctionalInterface
    interface GreetingService {
        void sayMessage(String message);
    }

    interface Animal {
        Integer get(String msg);
    }

    public Integer function1(Function<String, Integer> function) {
        Integer result = function.apply(str);
        return result;
    }
    public Integer function2(Animal animal) {
        Integer result = animal.get(str);
        return result;
    }

    public void test1(){
        GreetingService greetService1 = message -> System.out.println("Hello " + message);
        greetService1.sayMessage("sdfsdfsdf");
    }
    public void test2(){
        FunctionalInterfaceTest test=new FunctionalInterfaceTest();
        test.setStr("234");
        System.out.println(test.function1(Integer::valueOf));
    }
    public void test3(){
        setStr("234");
        System.out.println(function2(Integer::valueOf));
    }
    public static void main(String[] args) {
        FunctionalInterfaceTest test=new FunctionalInterfaceTest();
        test.test3();
    }
}
