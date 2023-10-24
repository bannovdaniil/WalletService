package ru.ylab.aop.aspects;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ylab.model.Action;
import ru.ylab.service.ActionService;
import ru.ylab.service.SessionService;
import ru.ylab.service.impl.ActionServiceImpl;
import ru.ylab.service.impl.SessionServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Aspect
public class AuditAspect {
    public static final Logger LOG = LoggerFactory.getLogger(AuditAspect.class);
    private final ActionService actionService = ActionServiceImpl.getInstance();
    private final SessionService sessionService = SessionServiceImpl.getInstance();

    @Pointcut("@annotation(ru.ylab.aop.annotations.Audit) && args(req, ..)")
    public void annotatedByAudit(HttpServletRequest req) {
    }

    @Around("annotatedByAudit(req)")
    public Object saveGet(ProceedingJoinPoint joinPoint, HttpServletRequest req) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis() - startTime;

        Long userId = 0L;
        String session = "not login";
        String path = "";
        String json = "";
        if (req != null) {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(req.getCookies());
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                userId = sessionService.getUser(sessionId.get()).getId();
                session = sessionId.get().toString();
            }
            path = req.getPathInfo();
            json = getJson(req);
        }
        Action action = new Action(
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

        action = actionService.add(action);
        LOG.info("{}", action);

        return result;
    }

    private String getJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader postData = req.getReader();
        String line;
        while ((line = postData.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
