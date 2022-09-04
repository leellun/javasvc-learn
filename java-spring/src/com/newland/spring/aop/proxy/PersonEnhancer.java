package com.newland.spring.aop.proxy;

import com.newland.spring.aop.aspects.Person;
import com.newland.spring.aop.proxy.callback.BeanMethodInterceptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cglib.core.DefaultGeneratorStrategy;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * Author: leell
 * Date: 2022/9/4 13:49:20
 */
public class PersonEnhancer {
    public static Person person() {
        Class<Person> configSuperClass = Person.class;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(configSuperClass);
        enhancer.setInterfaces(new Class<?>[]{BeanFactory.class});
        enhancer.setUseFactory(false);
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setStrategy(new DefaultGeneratorStrategy());
        BeanMethodInterceptor beanMethodInterceptor = new BeanMethodInterceptor();
        enhancer.setCallbackFilter(new CallbackFilter() {
            @Override
            public int accept(Method method) {
                System.out.println(method.getName());
                if (beanMethodInterceptor.isMatch(method)) {
                    return 0;
                } else {
                    return 1;
                }
            }

            @Override
            public boolean equals(Object obj) {
                return super.equals(obj);
            }
        });
//        enhancer.setCallbacks(new Callback[]{new BeanMethodInterceptor(),NoOp.INSTANCE});
        enhancer.setCallbackTypes(new Class[]{BeanMethodInterceptor.class, NoOp.class});
        try {
            Enhancer.registerStaticCallbacks(enhancer.createClass(), new Callback[]{new BeanMethodInterceptor(), NoOp.INSTANCE});
            Person proxy = (Person) enhancer.createClass().getConstructor().newInstance();
            return (Person) proxy;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Person person = person();
        System.out.println(person.getName());
    }
}
