package com.newland.design22.strategy;

/**
 * Author: leell
 * Date: 2022/8/28 12:22:31
 */
public class Context {
    private Strategy strategy;

    public Context(Strategy strategy){
        this.strategy = strategy;
    }

    public int executeStrategy(int num1, int num2){
        return strategy.doOperation(num1, num2);
    }
}
