package com.newland.algorithm.set;

import java.util.HashSet;
import java.util.TreeSet;

/**
 * TreeSet特点：
 * 1 排列无序、不可重复
 * 2 底层使用二叉树实现
 * 3 排序存储
 */
public class TreeSetTest {
    public static void main(String[] args) {
        TreeSet<String> list = new TreeSet<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.format("%03d", i));
        }
    }
}
