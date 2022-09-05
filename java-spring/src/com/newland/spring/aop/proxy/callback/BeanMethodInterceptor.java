package com.newland.spring.aop.proxy.callback;

import com.newland.spring.aop.aspects.Person;
import com.newland.spring.aop.proxy.PersonEnhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Author: leell
 * Date: 2022/9/4 14:31:56
 */
public class BeanMethodInterceptor implements MethodInterceptor {
    private Person person;

    public BeanMethodInterceptor() {
    }

    public BeanMethodInterceptor(Person person) {
        this.person = person;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("s=================1");
        Object result;
        if (this.person != null) {
            result = methodProxy.invoke(person, objects);
        } else {
            //这里注意了，如果用方法代理执行invoke，将会出现死循环，因为执行代理对象方法将被拦截，如果再次执行methodProxy的invoke方法又会被拦截，所以需要调用
            // invokeSuper执行person的方法，例如 执行proxy.getName()=>methodProxy.invokeSuper()=>person.getName()
            //methodProxy.invokeSuper()等同于super.method()
            result = methodProxy.invokeSuper(o, objects);
        }
        System.out.println("e=================1");
        return result;
    }

    public boolean isMatch(Method candidateMethod) {
        if (candidateMethod.getName().contains("Name") || candidateMethod.getName().contains("Age")) {
            return true;
        }
        return false;
    }
}
