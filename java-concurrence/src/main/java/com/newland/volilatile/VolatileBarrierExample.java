package com.newland.volilatile;

/**
 * LoadLoad屏障用来禁止处理器把上面的volatile读与下面的普通读重排序。
 * LoadStore屏障用来禁止处理器把上面的volatile读与下面的普通写重排序。
 */
public class VolatileBarrierExample {
    int          a;
    volatile int v1 = 1;
    volatile int v2 = 2;
    void readAndWrite() {
        int i = v1; //第一个volatile读
        int j = v2; // 第二个volatile读
        a = i + j; //普通写
        v1 = i + 1; // 第一个volatile写
        v2 = j * 2; //第二个 volatile写
    }
    //…                 //其他方法
}
