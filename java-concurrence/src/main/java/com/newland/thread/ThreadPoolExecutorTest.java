package com.newland.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池参数设置：
 * 1 核心线程数 corePoolSize
 * 2 最大线程数 maximumPoolSize
 * 3 线程空闲时间 keepAliveTime
 * 4 时间单位 unit
 * 5 任務队列 workQueue
 * 6 线程工厂 threadFactory
 * 7 任務拒绝策略 handler
 */
public class ThreadPoolExecutorTest {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, Runtime.getRuntime().availableProcessors(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("=====================>");
            }
        });
    }
}
