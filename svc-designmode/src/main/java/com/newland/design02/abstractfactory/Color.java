package com.newland.design02.abstractfactory;

public interface Color {
    void fill();
    public class Red implements Color {

        @Override
        public void fill() {
            System.out.println("Inside Red::fill() method.");
        }
    }
    public class Green implements Color {

        @Override
        public void fill() {
            System.out.println("Inside Green::fill() method.");
        }
    }
    public class Blue implements Color {

        @Override
        public void fill() {
            System.out.println("Inside Blue::fill() method.");
        }
    }
}
