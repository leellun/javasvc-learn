package com.newland.jdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * Author: leell
 * Date: 2022/10/5 00:55:20
 */
public class DelayQueueDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("00000001");
        list.add("00000002");
        list.add("00000003");
        list.add("00000004");
        list.add("00000005");

        DelayQueue<OrderDelay> queue = new DelayQueue<OrderDelay>();

        long start = System.currentTimeMillis();
        for(int i = 0;i<5;i++){

            //延迟三秒取出
            queue.put(new OrderDelay(list.get(i),
                    TimeUnit.NANOSECONDS.convert(3, TimeUnit.SECONDS)));
            try {
                queue.take().print();
                System.out.println("After " +
                        (System.currentTimeMillis()-start) + " MilliSeconds");
            } catch (InterruptedException e) {}
        }
    }
}
