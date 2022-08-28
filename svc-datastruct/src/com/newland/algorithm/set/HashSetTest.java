package com.newland.algorithm.set;

import java.util.HashSet;

/**
 * HashSet特点：
 * 1 排列无序，不可重复
 * 2 底层使用Hash表实现
 * 3 存取速度快
 * 4 内部是HashMap
 * 5 线程不安全
 */
public class HashSetTest {
    public static void main(String[] args) {
        HashSet<String> list = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.format("%03d", i));
        }
    }
}
