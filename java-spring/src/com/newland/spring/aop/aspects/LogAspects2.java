package com.newland.spring.aop.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;

/**
 * Author: leell
 * Date: 2022/9/2 09:30:18
 */
@Aspect
public class LogAspects2{
    @DeclareParents(value="com.newland.spring.aop.aspects.MathCalculator",defaultImpl=CalculatorImpl2.class)
    private Calculator calculator;
}


