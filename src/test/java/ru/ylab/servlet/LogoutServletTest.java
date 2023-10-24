package ru.ylab.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.exception.RepositoryException;
import ru.ylab.service.SessionService;
import ru.ylab.service.impl.SessionServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(
        MockitoExtension.class
)
class LogoutServletTest {
    private static SessionService mockSessionService;
    @InjectMocks
    private static LogoutServlet logoutServlet;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

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
        mockSessionService = Mockito.mock(SessionService.class);
        setMock(mockSessionService);
        logoutServlet = new LogoutServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = SessionServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);
    }

    @Test
    void doGet() {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        logoutServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockSessionService).logout(Mockito.any());
    }

    @Test
    void doGetAccessDeniedException() {
        Mockito.doReturn(Optional.empty()).when(mockSessionService).getUuidFromCookie(Mockito.any());

        logoutServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void doGetBadRequest() {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        Mockito.doThrow(new RepositoryException("Test SQL exception.")).when(mockSessionService).logout(Mockito.any());

        logoutServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}