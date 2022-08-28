package com.newland.design01.factory;

/**
 * Author: leell
 * Date: 2022/8/28 00:34:49
 */
public class Rectangle implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }
}
