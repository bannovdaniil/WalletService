package ru.ylab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.service.SessionService;
import ru.ylab.service.impl.SessionServiceImpl;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * Логин для пользователя.
 */
@WebServlet(urlPatterns = {"/api/logout"})
public class LogoutServlet extends HttpServlet {
    private final transient SessionService sessionService;

    public LogoutServlet() {
        this.sessionService = SessionServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);

        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(req.getCookies());
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                sessionService.logout(sessionId.get());
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void setJsonHeader(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }
}
