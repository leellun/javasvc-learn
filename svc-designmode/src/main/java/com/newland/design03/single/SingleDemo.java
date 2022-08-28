package com.newland.design03.single;

/**
 * 意图：保证一个类仅有一个实例，并提供一个访问它的全局访问点。
 * 主要解决：一个全局使用的类频繁地创建与销毁。
 * 何时使用：当您想控制实例数目，节省系统资源的时候。
 *
 * 优点：
 * 1、在内存里只有一个实例，减少了内存的开销，尤其是频繁的创建和销毁实例（比如管理学院首页页面缓存）。
 * 2、避免对资源的多重占用（比如写文件操作）。
 * 缺点：没有接口，不能继承，与单一职责原则冲突，一个类应该只关心内部逻辑，而不关心外面怎么样来实例化。
 */
public class SingleDemo {
    /**
     * 懒汉式，线程不安全
     */
    public static class Singleton1 {
        private static Singleton1 instance;
        private Singleton1 (){}

        public static Singleton1 getInstance() {
            if (instance == null) {
                instance = new Singleton1();
            }
            return instance;
        }
    }

    /**
     * 懒汉式，线程安全
     */
    public static class Singleton2 {
        private static Singleton2 instance;
        private Singleton2 (){}
        public static synchronized Singleton2 getInstance() {
            if (instance == null) {
                instance = new Singleton2();
            }
            return instance;
        }
    }

    /**
     * 饿汉式
     */
    public static class Singleton3 {
        private static Singleton3 instance = new Singleton3();
        private Singleton3 (){}
        public static Singleton3 getInstance() {
            return instance;
        }
    }

    /**
     * 双检锁/双重校验锁
     */
    public static class Singleton4 {
        private volatile static Singleton4 singleton;
        private Singleton4 (){}
        public static Singleton4 getSingleton() {
            if (singleton == null) {
                synchronized (Singleton4.class) {
                    if (singleton == null) {
                        singleton = new Singleton4();
                    }
                }
            }
            return singleton;
        }
    }

    /**
     * 登记式/静态内部类
     */
    public static class Singleton5 {
        private static class SingletonHolder {
            private static final Singleton5 INSTANCE = new Singleton5();
        }
        private Singleton5 (){}
        public static final Singleton5 getInstance() {
            return SingletonHolder.INSTANCE;
        }
    }
    public static enum Singleton6 {
        INSTANCE;
        public void whateverMethod() {
        }
    }
}
