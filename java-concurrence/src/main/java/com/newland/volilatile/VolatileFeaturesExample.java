package com.newland.volilatile;

/**
 * Author: leell
 * Date: 2022/8/28 21:51:58
 */
public class VolatileFeaturesExample {
    volatile long vl = 0L; //使用volatile声明64位的long型变量
    public void set(long l) {
        vl = l; //单个volatile变量的写
    }
    public void getAndIncrement() {
        vl++; //复合（多个）volatile变量的读/写
    }
    public long get() {
        return vl; //单个volatile变量的读
    }
}

/**
 * 程序语义等价：
 */
class VolatileFeaturesExample1 {
    long vl = 0L; // 64位的long型普通变量
    public synchronized void set(long l) {//对单个的普通变量的写用同一个锁同步
        vl = l;
    }
    public void getAndIncrement() { //普通方法调用
        long temp = get(); //调用已同步的读方法
        temp += 1L; //普通写操作
        set(temp); //调用已同步的写方法
    }
    public synchronized long get() { //对单个的普通变量的读用同一个锁同步
        return vl;
    }
}
