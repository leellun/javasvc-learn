package com.newland.design05.prototype;

/**
 * Author: leell
 * Date: 2022/8/28 01:23:05
 */
public abstract class Shape implements Cloneable {

    private String id;
    protected String type;

    abstract void draw();

    public String getType(){
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
    public static class Rectangle extends Shape {

        public Rectangle(){
            type = "Rectangle";
        }

        @Override
        public void draw() {
            System.out.println("Inside Rectangle::draw() method.");
        }
    }
    public static class Square extends Shape {

        public Square(){
            type = "Square";
        }

        @Override
        public void draw() {
            System.out.println("Inside Square::draw() method.");
        }
    }
    public static class Circle extends Shape {

        public Circle(){
            type = "Circle";
        }

        @Override
        public void draw() {
            System.out.println("Inside Circle::draw() method.");
        }
    }
}