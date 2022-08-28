package com.newland.algorithm.map;

import java.util.Hashtable;
import java.util.TreeMap;

/**
 * TreeMap特点:
 * 1 键不可重复、值可重复
 * 2 底层二叉树
 */
public class TreeMapTest {
    public static void main(String[] args) {
        TreeMap<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(String.valueOf(i) + "-key", i);
        }
    }
}
