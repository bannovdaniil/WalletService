package ru.ylab.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.model.dto.UserLoginDto;
import ru.ylab.service.SessionService;
import ru.ylab.service.impl.SessionServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

@ExtendWith(
        MockitoExtension.class
)
class LoginServletTest {
    private static SessionService mockSessionService;
    @InjectMocks
    private static LoginServlet loginServlet;
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
        loginServlet = new LoginServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = SessionServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);
    }

    @Test
    void doPost() throws IOException {
        Long expectedUserId = 123L;
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{ \"userId\": \"" + expectedUserId + "\", \"password\": \"1\" }",
                null
        ).when(mockBufferedReader).readLine();
        Mockito.doReturn(UUID.randomUUID()).when(mockSessionService).login(Mockito.any());

        loginServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<UserLoginDto> argumentCaptor = ArgumentCaptor.forClass(UserLoginDto.class);
        Mockito.verify(mockSessionService).login(argumentCaptor.capture());

        UserLoginDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedUserId, result.getUserId());
    }

    @Test
    void doPostAccessDeniedException() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{ \"userId\": \"1\", \"password\": \"1\" }",
                null
        ).when(mockBufferedReader).readLine();
        // Mockito.doReturn(UUID.randomUUID()).when(mockSessionService).login(Mockito.any());
        Mockito.doThrow(new AccessDeniedException("Test exception.")).when(mockSessionService).login(Mockito.any());

        loginServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void doPostBadValidation() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{ \"userId\": \"\", \"password\": \"1\" }",
                null
        ).when(mockBufferedReader).readLine();

        loginServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}