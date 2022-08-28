package com.newland.algorithm.map;

import java.util.Hashtable;

/**
 * HashTable特点：
 * 1 键不可重复，值可重复
 * 2 底层哈希表
 * 3 线程安全
 * 4 key、value都允许为null
 */
public class HashTableTest {
    public static void main(String[] args) {
        Hashtable<String, Integer> map = new Hashtable<>();
        for (int i = 0; i < 100; i++) {
            map.put(String.valueOf(i) + "-key", i);
        }
    }
}
