package com.newland.design13.proxy;

/**
 * 在代理模式（Proxy Pattern）中，一个类代表另一个类的功能。这种类型的设计模式属于结构型模式。
 * 在代理模式中，我们创建具有现有对象的对象，以便向外界提供功能接口。
 *
 * 意图：为其他对象提供一种代理以控制对这个对象的访问。
 * 优点： 1、职责清晰。 2、高扩展性。 3、智能化。
 *
 * 缺点： 1、由于在客户端和真实主题之间增加了代理对象，因此有些类型的代理模式可能会造成请求的处理速度变慢。 2、实现代理模式需要额外的工作，有些代理模式的实现非常复杂。
 *
 * 使用场景：按职责来划分，通常有以下使用场景：
 * 1、远程代理。
 * 2、虚拟代理。
 * 3、Copy-on-Write 代理。
 * 4、保护（Protect or Access）代理。
 * 5、Cache代理。
 * 6、防火墙（Firewall）代理。
 * 7、同步化（Synchronization）代理。
 * 8、智能引用（Smart Reference）代理。
 */
public class ProxyPatternDemo {

    public static void main(String[] args) {
        Image image = new ProxyImage("test_10mb.jpg");

        // 图像将从磁盘加载
        image.display();
        System.out.println("");
        // 图像不需要从磁盘加载
        image.display();
    }
}
