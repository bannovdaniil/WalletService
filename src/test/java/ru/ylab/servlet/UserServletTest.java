package ru.ylab.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ylab.exception.NotFoundException;
import ru.ylab.exception.RepositoryException;
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.service.SessionService;
import ru.ylab.service.UserService;
import ru.ylab.service.impl.SessionServiceImpl;
import ru.ylab.service.impl.UserServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(
        MockitoExtension.class
)
class UserServletTest {
    private static UserService mockUserService;
    private static SessionService mockSessionService;
    @InjectMocks
    private static UserServlet userServlet;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    private static void setMock(UserService mock) {
        try {
            Field instance = UserServiceImpl.class.getDeclaredField("instance");
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
        mockUserService = Mockito.mock(UserService.class);
        setMock(mockUserService);
        mockSessionService = Mockito.mock(SessionService.class);
        setMock(mockSessionService);

        userServlet = new UserServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = UserServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);
        instance = SessionServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.reset(mockUserService);
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.doReturn("/all").when(mockRequest).getPathInfo();
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        userServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockUserService).findAll();
    }

    @Test
    void doGetById() throws NotFoundException, IOException {
        Mockito.doReturn("/1").when(mockRequest).getPathInfo();
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        userServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockUserService).findById(1L);
    }

    @Test
    void doGetNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("/100").when(mockRequest).getPathInfo();
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());
        Mockito.doThrow(new NotFoundException("not found.")).when(mockUserService).findById(100L);

        userServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetAccessDeniedException() throws IOException {
        Mockito.doReturn(Optional.empty()).when(mockSessionService).getUuidFromCookie(Mockito.any());

        userServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void doGetBadRequest() throws IOException, NotFoundException {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        Mockito.doThrow(new RepositoryException("Test SQL exception.")).when(mockUserService).findById(Mockito.any());

        userServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException, NotFoundException {
        String expectedName = "f1 test name";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\n" +
                "    \"firstName\":\"" + expectedName + "\",\n" +
                "    \"lastName\":\"L2 last Name\",\n" +
                "    \"password\":\"1\"\n" +
                "}",
                null
        ).when(mockBufferedReader).readLine();

        userServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<UserIncomingDto> argumentCaptor = ArgumentCaptor.forClass(UserIncomingDto.class);
        Mockito.verify(mockUserService).add(argumentCaptor.capture());

        UserIncomingDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getFirstName());
    }

    @Test
    void doPostBadValidation() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\n" +
                "    \"firstName\":\"\"," +
                "    \"lastName\":\"L2 last Name\",\n" +
                "    \"password\":\"1\"\n" +
                "}",
                null
        ).when(mockBufferedReader).readLine();

        userServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}