package com.newland.design02.abstractfactory;

/**
 * Author: leell
 * Date: 2022/8/28 00:43:23
 */
public class FactoryProducer {
    public static AbstractFactory getFactory(String choice){
        if(choice.equalsIgnoreCase("SHAPE")){
            return new AbstractFactory.ShapeFactory();
        } else if(choice.equalsIgnoreCase("COLOR")){
            return new AbstractFactory.ColorFactory();
        }
        return null;
    }
}
