package ru.ylab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.service.SessionService;
import ru.ylab.service.impl.SessionServiceImpl;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static ru.ylab.Constants.SESSION_COOKIE;

/**
 * Логин для пользователя.
 */
@WebServlet(urlPatterns = {"/api/logout"})
public class LogoutServlet extends HttpServlet {
    private final transient SessionService sessionService;
    private final ObjectMapper objectMapper;

    public LogoutServlet() {
        this.sessionService = SessionServiceImpl.getInstance();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);

        try {
            String cookieValue = Arrays.stream(req.getCookies())
                    .filter(cookie -> SESSION_COOKIE.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElseThrow();
            UUID sessionId = UUID.fromString(cookieValue);
            if (sessionService.isActive(sessionId)) {
                sessionService.logout(sessionId);
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
