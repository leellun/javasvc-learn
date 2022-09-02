package com.newland.spring.aop.aspects;

/**
 * Author: leell
 * Date: 2022/9/2 09:31:44
 */
public class CalculatorImpl2 implements Calculator{

    public int get(float a) {
        return (int)Math.floor(a);
    }
    public String getString() {
        return "实现类";
    }
}