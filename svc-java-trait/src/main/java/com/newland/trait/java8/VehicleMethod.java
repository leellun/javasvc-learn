package com.newland.trait.java8;

/**
 * Java 8 新增了接口的默认方法。 简单说，默认方法就是接口可以有实现方法，而且不需要实现类去实现其方法。
 * 我们只需在方法名前面加个 default 关键字即可实现默认方法。
 *
 * 注意：一个类实现了多个接口，且这些接口有相同的默认方法，则实现类必须实现方法
 */
public class VehicleMethod {
    public interface Vehicle {
        default void print(){
            System.out.println("我是一辆车!");
        }
    }

    public interface FourWheeler {
        default void print(){
            System.out.println("我是一辆四轮车!");
        }
    }

    /**
     * 如果实现类只实现类不存在冲突方法，则实现类可以不用实现
     */
    public class Car0 implements Vehicle {
    }

    /**
     * 实现接口有相同方法，则必须实现，可以指定接口方法或者自定义子类方法
     */
    public class Car1 implements Vehicle, FourWheeler {
        public void print(){
            Vehicle.super.print();
        }
    }
    public class Car2 implements Vehicle, FourWheeler {
        public void print(){
            Vehicle.super.print();
        }
    }
}
