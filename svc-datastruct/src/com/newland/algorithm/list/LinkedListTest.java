package com.newland.algorithm.list;

import java.util.LinkedList;
import java.util.List;

/**
 * LinkedList特点:
 * 1 排列有序，可重复
 * 2 底层使用双向循环链表数据结构
 * 3 查询速度慢、增删快
 * 4 线程不安全
 */
public class LinkedListTest {
    public static void main(String[] args) {
        List<String> list = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.format("%03d", i));
        }
    }
}
