package com.newland.design01.factory;

/**
 * Author: leell
 * Date: 2022/8/28 00:35:12
 */
public class Circle implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}