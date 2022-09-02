package com.newland.spring.aop.aspects;

/**
 * Author: leell
 * Date: 2022/9/2 09:26:48
 */
public class MathCalculator {
    public int div(int i,int j){
        System.out.println("MathCalculator...div...");
        return i/j;
    }
}