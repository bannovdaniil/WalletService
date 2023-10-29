package ru.ylab.aop.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CommonPointcuts {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) && args(cookie, ..)")
    public void isGetMapping(String cookie) {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping) && args(cookie, ..)")
    public void isPutMapping(String cookie) {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) && args(cookie, ..)")
    public void isPostMapping(String cookie) {
    }

}
