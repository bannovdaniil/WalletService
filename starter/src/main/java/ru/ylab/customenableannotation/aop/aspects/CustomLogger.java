package ru.ylab.customenableannotation.aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Slf4j
@Aspect
public class CustomLogger {

    @Around("ru.ylab.customenableannotation.aop.aspects.EnableCommonPointcuts.isCustomLogging()")
    public Object addLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        StringJoiner jsonJoiner = new StringJoiner(", ");

        for (Object s : joinPoint.getArgs()) {
            jsonJoiner.add(s.toString());
        }

        long startTime = System.currentTimeMillis();
        long endTime = 0L;
        try {
            Object result = joinPoint.proceed();
            endTime = System.currentTimeMillis() - startTime;
            return result;
        } catch (Throwable ex) {
            jsonJoiner.add("Exception: " + ex.getMessage());
            throw ex;
        } finally {
            String json = jsonJoiner.toString();

            log.info("Custom Logger: {}, {}, {}, ", LocalDateTime.now(),
                    joinPoint.getSignature().toShortString(),
                    String.format("time: %d ms, json: %s",
                            endTime,
                            json
                    ));
        }
    }

}
