package com.newland.cas;

/**
 * Author: leell
 * Date: 2022/8/28 19:23:10
 */
public class CasIntegerTest {
    public static void main(String[] args) {
        CasInteger casInteger=new CasInteger(0);
        for(int i=0;i<10;i++){
            System.out.println(casInteger.getAndIncrement());
        }
    }
}
