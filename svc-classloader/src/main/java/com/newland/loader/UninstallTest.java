package com.newland.loader;

import java.lang.reflect.Method;

/**

 * @Described：类的卸载测试
 */

public class UninstallTest {


    public static void main(String[] args) {
        while (true) {
            try {
//                HelloWorld helloWorld=new HelloWorld();
//                helloWorld.say();
                System.out.println(UninstallTest.class.getClassLoader());
                MyClassLoader loader = new MyClassLoader("D:/classes");
                Class cls = loader.loadClass("com.newland.test.HelloWorld");
                Object demo = cls.getDeclaredConstructor().newInstance();
                Method m = demo.getClass().getMethod("say");
                m.invoke(demo);

                Thread.sleep(10000);
            } catch (Exception e) {
                System.out.println("not find");
                try {
                    Thread.sleep(10000);
                } catch (Exception e2) {
                }
            }
        }

    }
    /**

     * @param args

     * @throws ClassNotFoundException

     * @throws IllegalAccessException

     * @throws InstantiationException

     * @Author YHJ create at 2011-10-17 下午10:15:40

     */

    public static void main2(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        MyClassLoader classLoader1 = new MyClassLoader("classLoader1");

        Class<?> clazz = classLoader1.loadClass("TestCase1");

        @SuppressWarnings("unused")

        Object obj = clazz.newInstance();

        System.out.println("1:"+clazz.hashCode());
        obj=null;
        System.out.println("2:"+clazz.hashCode());
        classLoader1 = null;
        System.out.println("3:"+clazz.hashCode());
        clazz = null;
        System.out.println("===========");
        classLoader1 = new MyClassLoader("classLoader1");
        clazz = classLoader1.loadClass("TestCase1");

        System.out.println("4:"+clazz.hashCode());

    }

}