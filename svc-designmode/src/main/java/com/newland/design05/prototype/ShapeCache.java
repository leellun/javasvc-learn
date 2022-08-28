package com.newland.design05.prototype;

import java.util.Hashtable;

public class ShapeCache {

    private static Hashtable<String, Shape> shapeMap
            = new Hashtable<String, Shape>();

    public static Shape getShape(String shapeId) {
        Shape cachedShape = shapeMap.get(shapeId);
        return (Shape) cachedShape.clone();
    }

    // 对每种形状都运行数据库查询，并创建该形状
    // shapeMap.put(shapeKey, shape);
    // 例如，我们要添加三种形状
    public static void loadCache() {
        Shape.Circle circle = new Shape.Circle();
        circle.setId("1");
        shapeMap.put(circle.getId(),circle);

        Shape.Square square = new Shape.Square();
        square.setId("2");
        shapeMap.put(square.getId(),square);

        Shape.Rectangle rectangle = new Shape.Rectangle();
        rectangle.setId("3");
        shapeMap.put(rectangle.getId(),rectangle);
    }
}