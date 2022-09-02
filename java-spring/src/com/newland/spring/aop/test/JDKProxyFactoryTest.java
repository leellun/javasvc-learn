package com.newland.spring.aop.test;

import com.newland.spring.aop.proxy.CGlibProxyFactory;
import com.newland.spring.aop.service.impl.NiaoServiceBean;
import com.newland.spring.aop.service.impl.PersonServiceBean;

/**
 * Author: leell
 * Date: 2022/9/2 09:02:24
 */
public class JDKProxyFactoryTest {
    public static void main(String[] args) {
//        JDKProxyFactory factory = new JDKProxyFactory();
//        PersonService service = (PersonService) factory.createProxyIntance(new PersonServiceBean());
//        service.save("888");
        testCglibProxy();
    }
    public static void testCglibProxy(){
        CGlibProxyFactory factory = new CGlibProxyFactory();
        PersonServiceBean service = (PersonServiceBean) factory.createProxyIntance(new PersonServiceBean());
        service.save("999");
    }
    public static void testCglibProxy2(){
        CGlibProxyFactory factory = new CGlibProxyFactory();
        NiaoServiceBean service = (NiaoServiceBean) factory.createProxyIntance(new NiaoServiceBean());
        service.save("999");
    }
}
