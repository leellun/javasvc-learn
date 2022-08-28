package com.newland.design01.factory;

/**
 * Author: leell
 * Date: 2022/8/28 00:35:01
 */
public class Square implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }
}
