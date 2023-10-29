package ru.ylab.aop.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.ylab.model.Action;
import ru.ylab.service.ActionService;
import ru.ylab.service.SessionService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
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
        StringJoiner jsonJoiner = new StringJoiner(", ");

        for (Object s : joinPoint.getArgs()) {
            if (!Objects.equals(s, cookie)) {
                jsonJoiner.add(s.toString());
            }
        }

        Long userId = 0L;
        String session = "not login";

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
            if (cookie != null && !cookie.isBlank()) {
                Optional<UUID> sessionId = sessionService.getUuidFromCookie(cookie);
                if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                    userId = sessionService.getUser(sessionId.get()).getId();
                    session = sessionId.get().toString();
                }
            }

            Action action = new Action(
                    LocalDateTime.now(),
                    joinPoint.getSignature().toShortString(),
                    userId,
                    String.format("time: %d ms, session: [%s], json: %s",
                            endTime,
                            session,
                            json
                    )
            );
            action = actionService.add(action);
            log.info("{}", action);
        }
    }
}
