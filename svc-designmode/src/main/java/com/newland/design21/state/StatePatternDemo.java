package com.newland.design21.state;

/**
 * 在状态模式（State Pattern）中，类的行为是基于它的状态改变的。这种类型的设计模式属于行为型模式。
 *
 *意图：允许对象在内部状态发生改变时改变它的行为，对象看起来好像修改了它的类。
 * 应用实例： 1、打篮球的时候运动员可以有正常状态、不正常状态和超常状态。
 * 2、曾侯乙编钟中，'钟是抽象接口','钟A'等是具体状态，'曾侯乙编钟'是具体环境（Context）。
 *
 * 优点： 1、封装了转换规则。 2、枚举可能的状态，在枚举状态之前需要确定状态种类。
 * 3、将所有与某个状态有关的行为放到一个类中，并且可以方便地增加新的状态，只需要改变对象状态即可改变对象的行为。
 * 4、允许状态转换逻辑与状态对象合成一体，而不是某一个巨大的条件语句块。
 * 5、可以让多个环境对象共享一个状态对象，从而减少系统中对象的个数。
 *
 * 缺点： 1、状态模式的使用必然会增加系统类和对象的个数。 2、状态模式的结构与实现都较为复杂，如果使用不当将导致程序结构和代码的混乱。
 * 3、状态模式对"开闭原则"的支持并不太好，对于可以切换状态的状态模式，增加新的状态类需要修改那些负责状态转换的源代码，否则无法切换到新增状态，
 * 而且修改某个状态类的行为也需修改对应类的源代码。
 */
public class StatePatternDemo {
    public static void main(String[] args) {
        Context context = new Context();

        StartState startState = new StartState();
        startState.doAction(context);

        System.out.println(context.getState().toString());

        StopState stopState = new StopState();
        stopState.doAction(context);

        System.out.println(context.getState().toString());
    }
}
