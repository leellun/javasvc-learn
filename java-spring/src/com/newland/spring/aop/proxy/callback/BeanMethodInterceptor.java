package com.newland.spring.aop.proxy.callback;

import com.newland.spring.aop.proxy.PersonEnhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Author: leell
 * Date: 2022/9/4 14:31:56
 */
public class BeanMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("s=================1");
        Object result= methodProxy.invokeSuper(o,objects);
        System.out.println("e=================1");
        return result;
    }

    public boolean isMatch(Method candidateMethod) {
        if (candidateMethod.getName().contains("Name")||candidateMethod.getName().contains("Age")) {
            return true;
        }
        return false;
    }
}
