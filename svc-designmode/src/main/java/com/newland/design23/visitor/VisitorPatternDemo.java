package com.newland.design23.visitor;

/**
 * 在访问者模式（Visitor Pattern）中，我们使用了一个访问者类，它改变了元素类的执行算法。通过这种方式，元素的执行算法可以随着访问者改变而改变。
 * 这种类型的设计模式属于行为型模式。
 * 根据模式，元素对象已接受访问者对象，这样访问者对象就可以处理元素对象上的操作。
 * 意图：主要将数据结构与数据操作分离。
 * 应用实例：您在朋友家做客，您是访问者，朋友接受您的访问，您通过朋友的描述，然后对朋友的描述做出一个判断，这就是访问者模式。
 *
 * 优点： 1、符合单一职责原则。 2、优秀的扩展性。 3、灵活性。
 *
 * 缺点： 1、具体元素对访问者公布细节，违反了迪米特原则。 2、具体元素变更比较困难。 3、违反了依赖倒置原则，依赖了具体类，没有依赖抽象。
 */
public class VisitorPatternDemo {
    public static void main(String[] args) {

        ComputerPart computer = new Computer();
        computer.accept(new ComputerPartDisplayVisitor());
    }
}