package ru.ylab.customenableannotation.aop.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class EnableCommonPointcuts {
    @Pointcut(value = "@annotation(ru.ylab.customenableannotation.aop.annotation.CustomLogger)")
    public void isCustomLogging() {
    }

}
