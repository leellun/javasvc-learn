package com.newland.algorithm.list;

import java.util.ArrayList;
import java.util.List;

/**
 * ArrayList特点：
 * 1 排列有序，可重复
 * 2 底层使用数组
 * 3 速度快、增速慢，因为基于数组get()和set()块，相当于直接操作内存数据
 * 4 当容量不够时，ArrayList是当前容量*1.5倍(int newCapacity = oldCapacity + (oldCapacity >> 1);)
 * 5 线程不安全
 */
public class ArrayListTest {
    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.format("%03d",i));
        }
    }
}
