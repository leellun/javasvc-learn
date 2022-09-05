package com.newland.algorithm.map;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap也改为了数据加链表加红黑树的存储结构，并且废弃了segment的加锁操作，
 * 采用了volatile HashEntry<K,V>对象保存数据，即对每一条数据直接通过volatile避免冲突，
 * 同时ConcurrentHashMap还使用了大量的synchronized和CAS算法来保证线程安全。
 */
public class ConcurrentHashMapTest {
    public static void main(String[] args) {
        ConcurrentHashMap<String,String> concurrentHashMap=new ConcurrentHashMap<>();
        concurrentHashMap.put("aa","11111");
        System.out.println(concurrentHashMap.get("aa"));
    }
}
