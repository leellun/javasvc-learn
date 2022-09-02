package com.newland.spring.aop.proxy;

import com.newland.spring.aop.service.impl.PersonServiceBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author: leell
 * Date: 2022/9/2 08:59:30
 */
public class JDKProxyFactory implements InvocationHandler {
    //这里就是上面所说的目标对象
    private Object targetObject;
    //这里就相当于上面AOP框架创建的代理对象
    public Object createProxyIntance(Object targetObject){
        this.targetObject = targetObject;
        return Proxy.newProxyInstance(this.targetObject.getClass().getClassLoader(),
                this.targetObject.getClass().getInterfaces(), this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {//环绕通知
        PersonServiceBean bean = (PersonServiceBean) this.targetObject;
        Object result = null;
        //..... advice()-->前置通知
        try {
            result = method.invoke(targetObject, args);
            // afteradvice() -->后置通知
        } catch (RuntimeException e) {
            //exceptionadvice()--> 例外通知
        }finally{
            //finallyadvice(); -->最终通知
        }
        return result;
    }

}
