package com.newland.cas;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Author: leell
 * Date: 2022/8/28 19:16:31
 */
public class CasInteger {
    private static Unsafe unsafe;

    static {
        try {
            Field field=Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static final long VALUE;

    static {
        try {
            VALUE = unsafe.objectFieldOffset(CasInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public CasInteger(Integer i) {
        this.value = i;
    }

    private volatile int value;

    public final int get() {
        return value;
    }

    public final void set(int newValue) {
        value = newValue;
    }

    public final boolean compareAndSet(int expectedValue, int newValue) {
        return unsafe.compareAndSwapInt(this, VALUE, expectedValue, newValue);
    }

    public final int getAndIncrement() {
        return unsafe.getAndAddInt(this, VALUE, 1);
    }
}
