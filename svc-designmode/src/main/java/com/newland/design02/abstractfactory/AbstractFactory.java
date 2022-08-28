package com.newland.design02.abstractfactory;

public abstract class AbstractFactory {
    public abstract Color getColor(String color);
    public abstract Shape getShape(String shape);
    public static class ShapeFactory extends AbstractFactory {

        @Override
        public Shape getShape(String shapeType){
            if(shapeType == null){
                return null;
            }
            if(shapeType.equalsIgnoreCase("CIRCLE")){
                return new Shape.Circle();
            } else if(shapeType.equalsIgnoreCase("RECTANGLE")){
                return new Shape.Rectangle();
            } else if(shapeType.equalsIgnoreCase("SQUARE")){
                return new Shape.Square();
            }
            return null;
        }

        @Override
        public Color getColor(String color) {
            return null;
        }
    }
    public static class ColorFactory extends AbstractFactory {

        @Override
        public Shape getShape(String shapeType){
            return null;
        }

        @Override
        public Color getColor(String color) {
            if(color == null){
                return null;
            }
            if(color.equalsIgnoreCase("RED")){
                return new Color.Red();
            } else if(color.equalsIgnoreCase("GREEN")){
                return new Color.Green();
            } else if(color.equalsIgnoreCase("BLUE")){
                return new Color.Blue();
            }
            return null;
        }
    }
}
