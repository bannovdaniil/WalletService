package ru.ylab.aop.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.ylab.model.Action;
import ru.ylab.service.ActionService;
import ru.ylab.service.SessionService;

import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {
    private final ActionService actionService;
    private final SessionService sessionService;

    @Around("ru.ylab.aop.aspects.CommonPointcuts.isGetMapping(cookie) " +
            "|| ru.ylab.aop.aspects.CommonPointcuts.isPutMapping(cookie) " +
            "|| ru.ylab.aop.aspects.CommonPointcuts.isPostMapping(cookie)")
    public Object addLogging(ProceedingJoinPoint joinPoint, String cookie) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        System.out.println("full method description: " + signature.getMethod());
        System.out.println("method name: " + signature.getMethod().getName());
        System.out.println("declaring type: " + signature.getDeclaringType());

        // Method args
        System.out.println("Method args names:");
        Arrays.stream(signature.getParameterNames())
                .forEach(s -> System.out.println("arg name: " + s));

        System.out.println("Method args types:");
        Arrays.stream(signature.getParameterTypes())
                .forEach(s -> System.out.println("arg type: " + s));

        System.out.println("Method args values:");
        Arrays.stream(joinPoint.getArgs())
                .forEach(o -> System.out.println("arg value: " + o.toString()));

        // Additional Information
        System.out.println("returning type: " + signature.getReturnType());
        System.out.println("method modifier: " + Modifier.toString(signature.getModifiers()));
        Arrays.stream(signature.getExceptionTypes())
                .forEach(aClass -> System.out.println("exception type: " + aClass));


        log.info("Rest Controller");
        Action action;
        Object result = null;

        Long userId = 0L;
        String session = "not login";
        String path = "";
        String json = "";

        long startTime = System.currentTimeMillis();
        long endTime = 0L;
        try {
            result = joinPoint.proceed();
            endTime = System.currentTimeMillis() - startTime;
            return result;
        } catch (Throwable ex) {
            throw ex;
        } finally {
            if (cookie != null && !cookie.isBlank()) {
                Optional<UUID> sessionId = sessionService.getUuidFromCookie(cookie);
                if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                    userId = sessionService.getUser(sessionId.get()).getId();
                    session = sessionId.get().toString();
                }
            }

            action = new Action(
                    LocalDateTime.now(),
                    joinPoint.getSignature().toShortString(),
                    userId,
                    String.format("time: %d ms, session: [%s], path: [%s], json: %s",
                            endTime,
                            session,
                            path == null ? "" : path,
                            json
                    )
            );
            actionService.add(action);
            log.info("{}", action);
        }
    }
}
