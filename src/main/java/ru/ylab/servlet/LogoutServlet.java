package ru.ylab.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.aop.annotations.Audit;
import ru.ylab.service.SessionService;
import ru.ylab.service.impl.SessionServiceImpl;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.impl.LiquibaseUtilImpl;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

/**
 * Логин для пользователя.
 */
@WebServlet(urlPatterns = {"/api/logout"})
public class LogoutServlet extends HttpServlet {
    private final transient SessionService sessionService;
    private final transient LiquibaseUtil liquibaseUtil;

    public LogoutServlet() {
        this.sessionService = SessionServiceImpl.getInstance();
        this.liquibaseUtil = LiquibaseUtilImpl.getInstance();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        liquibaseUtil.init();
    }
    /**
     * Закончить сессию пользователя.
     */
    @Audit
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        setJsonHeader(resp);

        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(req.getCookies());
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                sessionService.logout(sessionId.get());
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                throw new AccessDeniedException("Forbidden");
            }
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void setJsonHeader(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }
}
