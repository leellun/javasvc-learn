package com.newland.algorithm.list;

import java.util.Vector;

/**
 * Vector特点：
 *  1 排列有序，可重复
 *  2 底层使用数组
 *  3 速度快、增删满
 *  4 线程安全、效率低
 *  5 当容量不够时，Vector默认扩展一倍容量
 */
public class VectorTest {
    public static void main(String[] args) {
        Vector<String> vector=new Vector<>();
        for (int i = 0; i < 100; i++) {
            vector.add(String.format("%03d",i));
        }
    }
}
