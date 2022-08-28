package com.newland.demo02.sync;

/**
 * JVM基于进入和退出Monitor对象来实现方法同步和代码块同步，但两者的实现细节不一样。代码块同步是使用monitorenter和monitorexit指令实现的，
 * 而方法同步是使用另外一种方式实现的(ACC_SYNCHRONIZED标志)。
 *
 * JVM要保证每个monitorenter必须有对应的monitorexit与之配对。任何对象都有一个monitor与之关联，当且一个monitor被持有后，
 * 它将处于锁定状态。线程执行monitorenter指令时，将会尝试获取对象所对应的monitor的所有权，即尝试获得对象的锁
 *
 * monitorenter指令是在编译后插入到同步代码块的开始位置，而monitorexit是插入到方法结束处和异常
 */
public class Synchronized {
    public static void main(String[] args) {
        // 对Synchronized Class对象进行加锁
        // 第一个monitorexit指令是同步代码块正常释放锁的一个标志；
        // 如果同步代码块中出现Exception或者Error，则会调用第二个monitorexit指令来保证释放锁
        synchronized (Synchronized.class) {

        }
        // 静态同步方法，对Synchronized Class对象进行加锁
        m();
    }

    public static synchronized void m() {
    }
    @FunctionalInterface
    public static interface SyncRunnable{
        void syncrun();
    }
}
