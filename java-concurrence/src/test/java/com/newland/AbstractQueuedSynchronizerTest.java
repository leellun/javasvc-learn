package com.newland;

import com.newland.aqs.AbstractQueuedSynchronizer;
import org.junit.Test;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: leell
 * Date: 2022/9/30 21:51:00
 */
public class AbstractQueuedSynchronizerTest {
    public static class ThreadLock implements Serializable {
        abstract static class Sync extends AbstractQueuedSynchronizer {
            final ConditionObject newCondition() {
                return new ConditionObject();
            }

            protected final boolean isHeldExclusively() {
                return getExclusiveOwnerThread() == Thread.currentThread();
            }

            protected final boolean tryAcquire(int acquires) {
                final Thread current = Thread.currentThread();
                int c = getState();
                if (c == 0) {
                    if (!hasQueuedPredecessors() &&
                            compareAndSetState(0, acquires)) {
                        setExclusiveOwnerThread(current);
                        return true;
                    }
                } else if (current == getExclusiveOwnerThread()) {
                    int nextc = c + acquires;
                    if (nextc < 0)
                        throw new Error("Maximum lock count exceeded");
                    setState(nextc);
                    return true;
                }
                return false;
            }

            protected final boolean tryRelease(int releases) {
                int c = getState() - releases;
                if (Thread.currentThread() != getExclusiveOwnerThread())
                    throw new IllegalMonitorStateException();
                boolean free = false;
                if (c == 0) {
                    free = true;
                    setExclusiveOwnerThread(null);
                }
                setState(c);
                return free;
            }
        }

    }

    @Test
    public void testAwaitThreadSafe() throws InterruptedException {
//        ThreadLock threadLock = new ThreadLock();
//        threadLock.acquire(1);
//        Condition condition = threadLock.newCondition();
//        condition.await();
//        threadLock.release(1);
        ReentrantLock lock = new ReentrantLock();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            lock.lock();
        }
        for (int i = 0; i < 10; i++) {
            lock.lock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AbstractQueuedSynchronizerTest test=new AbstractQueuedSynchronizerTest();
        test.testYield();
    }
    @Test
    public void testWaitNotify() throws InterruptedException {
        Object object1 = new Object();
        Thread thread1 = new Thread(() -> {
            while (true) {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    synchronized (object1) {
                        System.out.println("线程1打印信息");
                        object1.notify();
                    }
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            while (true) {
                synchronized (object1){
                    try {
                        object1.wait();
                        System.out.println("线程2打印信息");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        thread1.start();
        thread2.start();
    }
    @Test
    public void testAwaitSignal() throws InterruptedException {
        ReentrantLock reentrantLock=new ReentrantLock();
        Condition condition= reentrantLock.newCondition();
        Thread thread1 = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                    reentrantLock.lock();
                    System.out.println("线程1打印信息");
                    condition.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    reentrantLock.unlock();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            while (true) {
                try {
                    reentrantLock.lock();
                    condition.await();
                    System.out.println("线程2打印信息");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    condition.signal();
                }
            }
        });

        thread1.start();
        thread2.start();
    }
    @Test
    public void testYield(){
        Thread thread1 = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                    System.out.println("线程1打印信息");
                    Thread.yield();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }finally {
                }
            }
        });
        thread1.start();
    }
}
