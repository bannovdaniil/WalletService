package ru.ylab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.model.dto.BalanceDto;
import ru.ylab.model.dto.WalletIncomingDto;
import ru.ylab.service.SessionService;
import ru.ylab.service.WalletService;
import ru.ylab.service.impl.SessionServiceImpl;
import ru.ylab.service.impl.WalletServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

/**
 * Показывает Лог действий пользователя.
 */
@WebServlet(urlPatterns = {"/api/wallet/*"})
public class WalletServlet extends HttpServlet {
    private final transient SessionService sessionService;
    private final transient WalletService walletService;
    private final ObjectMapper objectMapper;

    public WalletServlet() {
        this.walletService = WalletServiceImpl.getInstance();
        this.sessionService = SessionServiceImpl.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);

        String responseAnswer = "";
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(req.getCookies());
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                BalanceDto balance = walletService.getBalance(sessionId.get());
                responseAnswer = objectMapper.writeValueAsString(balance);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                throw new AccessDeniedException("Forbidden");
            }
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseAnswer = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);

        String responseAnswer = "";
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(req.getCookies());
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                String json = getJson(req);
                WalletIncomingDto dto = Optional.ofNullable(objectMapper.readValue(json, WalletIncomingDto.class))
                        .orElseThrow(IllegalArgumentException::new);

                BalanceDto balanceDto = walletService.changeBalance(sessionId.get(), dto);

                responseAnswer = objectMapper.writeValueAsString(balanceDto);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                throw new AccessDeniedException("Forbidden");
            }
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseAnswer = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = e.getMessage();
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    private void setJsonHeader(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
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
