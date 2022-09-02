package com.newland.spring.aop.aspects;

/**
 * Author: leell
 * Date: 2022/9/2 09:26:48
 */
public class MathCalculator {
    public int div(int i,int j){
        System.out.println("MathCalculator...div...");
        int sum=add(i,j);
        System.out.println(sum);
        return i/j;
    }
    public int add(int i,int j){
        System.out.println("MathCalculator...div...");
        return i+j;
    }
}