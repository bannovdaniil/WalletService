package ru.ylab.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.exception.RepositoryException;
import ru.ylab.service.ActionService;
import ru.ylab.service.SessionService;
import ru.ylab.service.impl.ActionServiceImpl;
import ru.ylab.service.impl.SessionServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(
        MockitoExtension.class
)
class ActionServletTest {
    private static ActionService mockActionService;
    private static SessionService mockSessionService;
    @InjectMocks
    private static ActionServlet actionServlet;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;

    private static void setMock(ActionService mock) {
        try {
            Field instance = ActionServiceImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setMock(SessionService mock) {
        try {
            Field instance = SessionServiceImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockActionService = Mockito.mock(ActionService.class);
        setMock(mockActionService);
        mockSessionService = Mockito.mock(SessionService.class);
        setMock(mockSessionService);
        actionServlet = new ActionServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = ActionServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);
        instance = SessionServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.reset(mockActionService);
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @Test
    void doGet() throws IOException {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        actionServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockActionService).findAll();
    }

    @Test
    void doGetAccessDeniedException() throws IOException {
        Mockito.doReturn(Optional.empty()).when(mockSessionService).getUuidFromCookie(Mockito.any());

        actionServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void doGetBadRequest() throws IOException {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        Mockito.doThrow(new RepositoryException("Test SQL exception.")).when(mockActionService).findAll();

        actionServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}