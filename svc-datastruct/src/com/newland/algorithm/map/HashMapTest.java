package com.newland.algorithm.map;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: leell
 * Date: 2022/8/28 17:34:38
 */
public class HashMapTest {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("aaaa", "bbbb");
        for(int i=0;i<10;i++){
            map.put(String.valueOf(i),"sdfsdf");
        }
        map.put("aaa","sdfsdf");
        map.put("aaa1","sdfsdf");
        map.put("aaa2","sdfsdf");
        map.put("aaa3","sdfsdf");
        map.put("aaa4","sdfsdf");
    }
}
