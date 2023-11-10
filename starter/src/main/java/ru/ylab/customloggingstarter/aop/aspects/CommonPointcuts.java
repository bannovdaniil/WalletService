package ru.ylab.customloggingstarter.aop.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CommonPointcuts {
    @Pointcut(value = "@annotation(ru.ylab.customloggingstarter.aop.annotation.ActionLogger) && args(cookie, ..)")
    public void isLogging(String cookie) {
    }

}
