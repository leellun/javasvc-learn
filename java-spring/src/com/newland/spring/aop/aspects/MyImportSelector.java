package com.newland.spring.aop.aspects;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * Author: leell
 * Date: 2022/9/3 22:57:34
 */
public class MyImportSelector implements ImportSelector {

    //返回值，就是到导入到容器中的组件全类名
    //AnnotationMetadata:当前标注@Import注解的类的所有注解信息
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //importingClassMetadata
        return new String[]{Person1.class.getName()};
    }
}