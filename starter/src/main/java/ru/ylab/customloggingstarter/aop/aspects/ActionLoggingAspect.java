package ru.ylab.customloggingstarter.aop.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import ru.ylab.customloggingstarter.model.Action;
import ru.ylab.customloggingstarter.repository.ActionRepository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class ActionLoggingAspect {
    private final ActionRepository actionRepository;

    @Around("ru.ylab.customloggingstarter.aop.aspects.CommonPointcuts.isLogging(cookie)")
    public Object addLogging(ProceedingJoinPoint joinPoint, String cookie) throws Throwable {
        StringJoiner jsonJoiner = new StringJoiner(", ");

        for (Object s : joinPoint.getArgs()) {
            if (!Objects.equals(s, cookie)) {
                jsonJoiner.add(s.toString());
            }
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

            log.info("{}, {}, {}, ", LocalDateTime.now(),
                    joinPoint.getSignature().toShortString(),
                    String.format("time: %d ms, cookie: [%s], json: %s",
                            endTime,
                            cookie,
                            json
                    ));
            Action action = new Action(
                    LocalDateTime.now(),
                    joinPoint.getSignature().toShortString(),
                    0L,
                    String.format("time: %d ms, cookie: [%s], json: %s",
                            endTime,
                            cookie,
                            json
                    )
            );
            actionRepository.save(action);
        }
    }

}
