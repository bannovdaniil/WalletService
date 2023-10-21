package ru.ylab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.model.Action;
import ru.ylab.service.ActionService;
import ru.ylab.service.impl.ActionServiceImpl;
import ru.ylab.util.LiquibaseUtil;
import ru.ylab.util.impl.LiquibaseUtilImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Показывает Лог действий пользователя.
 */
@WebServlet(urlPatterns = {"/api/action"})
public class ActionServlet extends HttpServlet {
    private final transient ActionService actionService;
    private final transient LiquibaseUtil liquibaseUtil;
    private final ObjectMapper objectMapper;

    public ActionServlet() {
        this.actionService = ActionServiceImpl.getInstance();
        this.liquibaseUtil = LiquibaseUtilImpl.getInstance();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        liquibaseUtil.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);

        String responseAnswer = "";
        try {
            List<Action> actionList = actionService.findAll();
            resp.setStatus(HttpServletResponse.SC_OK);
            responseAnswer = objectMapper.writeValueAsString(actionList);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Bad request.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    private void setJsonHeader(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }
}
