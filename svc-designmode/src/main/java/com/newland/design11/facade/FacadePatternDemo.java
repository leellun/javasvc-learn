package com.newland.design11.facade;

/**
 * 外观模式（Facade Pattern）隐藏系统的复杂性，并向客户端提供了一个客户端可以访问系统的接口。
 * 这种类型的设计模式属于结构型模式，它向现有的系统添加一个接口，来隐藏系统的复杂性。
 *
 * 意图：为子系统中的一组接口提供一个一致的界面，外观模式定义了一个高层接口，这个接口使得这一子系统更加容易使用。
 * 主要解决：降低访问复杂系统的内部子系统时的复杂度，简化客户端之间的接口。
 * 应用实例： 1、去医院看病，可能要去挂号、门诊、划价、取药，让患者或患者家属觉得很复杂，如果有提供接待人员，只让接待人员来处理，就很方便。
 * 2、JAVA 的三层开发模式。
 *
 * 优点： 1、减少系统相互依赖。 2、提高灵活性。 3、提高了安全性。
 *
 * 缺点：不符合开闭原则，如果要改东西很麻烦，继承重写都不合适。
 */
public class FacadePatternDemo {
    public static void main(String[] args) {
        ShapeMaker shapeMaker = new ShapeMaker();

        shapeMaker.drawCircle();
        shapeMaker.drawRectangle();
        shapeMaker.drawSquare();
    }
}
