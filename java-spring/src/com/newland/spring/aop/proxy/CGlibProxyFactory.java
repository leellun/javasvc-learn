package com.newland.spring.aop.proxy;

import com.newland.spring.aop.service.impl.PersonServiceBean;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Author: leell
 * Date: 2022/9/2 09:03:11
 */
public class CGlibProxyFactory implements MethodInterceptor {
    private Object targetObject;

    public Object createProxyIntance(Object targetObject){
        this.targetObject = targetObject;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.targetObject.getClass());//非final
        enhancer.setCallback(this);
        return enhancer.create();
    }
    @Override
    public Object intercept(Object proxy, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {
        Object result = null;
        //..... advice()-->前置通知
        try {
            result = methodProxy.invoke(targetObject, args);
            // afteradvice() -->后置通知
        } catch (RuntimeException e) {
            //exceptionadvice()--> 例外通知
        }finally{
            //finallyadvice(); -->最终通知
        }
        return result;
    }
}
