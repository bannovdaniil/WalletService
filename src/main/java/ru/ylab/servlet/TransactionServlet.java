package ru.ylab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.model.Transaction;
import ru.ylab.service.TransactionService;
import ru.ylab.service.impl.TransactionServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Показывает Историю транзакций.
 */
@WebServlet(urlPatterns = {"/api/transaction"})
public class TransactionServlet extends HttpServlet {
    private final transient TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public TransactionServlet() {
        this.transactionService = TransactionServiceImpl.getInstance();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);

        String responseAnswer;
        try {
            List<Transaction> transactionList = transactionService.findAll();
            resp.setStatus(HttpServletResponse.SC_OK);
            responseAnswer = objectMapper.writeValueAsString(transactionList);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Bad request.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    private static void setJsonHeader(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }
}
