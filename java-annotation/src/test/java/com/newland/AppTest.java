package com.newland;

import com.newland.annotation.MyBean;
import com.newland.annotation.MyComponent;
import com.newland.bean.Student;
import com.newland.bean.StudentImpl;
import org.junit.Test;

import java.lang.annotation.Annotation;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigourous Test :-)
     */
    @Test
    public void testApp()    {
        System.out.println(StudentImpl.class.getAnnotations().length);
        System.out.println(StudentImpl.class.getDeclaredAnnotations().length);
        for(Annotation annotation:StudentImpl.class.getAnnotations()){
            System.out.println(annotation);
        }
        System.out.println("===================");
        for(Annotation annotation:StudentImpl.class.getDeclaredAnnotations()){
            System.out.println(annotation);
        }
    }
}
