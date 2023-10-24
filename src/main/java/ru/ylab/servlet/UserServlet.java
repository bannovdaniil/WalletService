package ru.ylab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.aop.annotations.Audit;
import ru.ylab.exception.ErrorHeader;
import ru.ylab.exception.NotFoundException;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;
import ru.ylab.service.SessionService;
import ru.ylab.service.UserService;
import ru.ylab.service.impl.SessionServiceImpl;
import ru.ylab.service.impl.UserServiceImpl;
import ru.ylab.validator.Validator;
import ru.ylab.validator.impl.UserIncomingDtoValidatorImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Показывает Лог действий пользователя.
 */
@WebServlet(urlPatterns = {"/api/user/*"})
public class UserServlet extends HttpServlet {
    private final transient UserService userService;
    private final transient SessionService sessionService;
    private final transient Validator<UserIncomingDto> userIncomingDtoValidator;

    private final ObjectMapper objectMapper;

    public UserServlet() {
        this.userService = UserServiceImpl.getInstance();
        this.sessionService = SessionServiceImpl.getInstance();
        this.userIncomingDtoValidator = UserIncomingDtoValidatorImpl.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Получить пользователя по ID либо весь список
     */
    @Audit
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);

        String responseAnswer;
        try {
            Optional<UUID> sessionId = sessionService.getUuidFromCookie(req.getCookies());
            if (sessionId.isPresent() && sessionService.isActive(sessionId.get())) {
                String[] pathPart = req.getPathInfo().split("/");
                if ("all".equals(pathPart[1])) {
                    List<UserOutDto> userList = userService.findAll();
                    resp.setStatus(HttpServletResponse.SC_OK);
                    responseAnswer = objectMapper.writeValueAsString(userList);
                } else {
                    Long userId = Long.parseLong(pathPart[1]);
                    UserOutDto userDto = userService.findById(userId);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    responseAnswer = objectMapper.writeValueAsString(userDto);
                }
            } else {
                throw new AccessDeniedException("Forbidden");
            }
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseAnswer = objectMapper.writeValueAsString(new ErrorHeader(e.getMessage()));
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseAnswer = objectMapper.writeValueAsString(new ErrorHeader(e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = objectMapper.writeValueAsString(new ErrorHeader(e.getMessage()));
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    /**
     * Создать пользователя
     */
    @Audit
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer;
        try {
            UserIncomingDto dto = Optional.ofNullable(objectMapper.readValue(json, UserIncomingDto.class))
                    .orElseThrow(IllegalArgumentException::new);

            if (!userIncomingDtoValidator.isValid(dto)) {
                throw new IllegalArgumentException("Error validation");
            }

            UserOutDto userOutDto = userService.add(dto);
            responseAnswer = objectMapper.writeValueAsString(userOutDto);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = objectMapper.writeValueAsString(new ErrorHeader(e.getMessage()));
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
