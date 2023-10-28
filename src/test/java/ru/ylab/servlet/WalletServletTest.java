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
import ru.ylab.model.dto.WalletIncomingDto;
import ru.ylab.service.SessionService;
import ru.ylab.service.WalletService;
import ru.ylab.service.impl.SessionServiceImpl;
import ru.ylab.service.impl.WalletServiceImpl;

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
class WalletServletTest {
    private static WalletService mockWalletService;
    private static SessionService mockSessionService;
    @InjectMocks
    private static WalletServlet walletServlet;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    private static void setMock(WalletService mock) {
        try {
            Field instance = WalletServiceImpl.class.getDeclaredField("instance");
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
        mockWalletService = Mockito.mock(WalletService.class);
        setMock(mockWalletService);
        mockSessionService = Mockito.mock(SessionService.class);
        setMock(mockSessionService);
        walletServlet = new WalletServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = WalletServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);
        instance = SessionServiceImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, null);
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.reset(mockWalletService);
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @Test
    void doGet() throws IOException {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        walletServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockWalletService).getBalance(Mockito.any());
    }

    @Test
    void doGetAccessDeniedException() throws IOException {
        Mockito.doReturn(Optional.empty()).when(mockSessionService).getUuidFromCookie(Mockito.any());

        walletServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void doGetBadRequest() throws IOException {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        Mockito.doThrow(new RepositoryException("Test SQL exception.")).when(mockWalletService).getBalance(Mockito.any());

        walletServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPutGetMoney() throws IOException, NotFoundException {
        String expectedMoney = "515.85";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "   { \"type\": \"GET\",\n" +
                "    \"sum\": \"" + expectedMoney + "\"}",
                null
        ).when(mockBufferedReader).readLine();
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        walletServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<WalletIncomingDto> argumentCaptor = ArgumentCaptor.forClass(WalletIncomingDto.class);
        Mockito.verify(mockWalletService).changeBalance(Mockito.any(), argumentCaptor.capture());

        WalletIncomingDto result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedMoney, result.getSum());
    }

    @Test
    void doPutAccessDeniedException() throws IOException {
        Mockito.doReturn(Optional.empty()).when(mockSessionService).getUuidFromCookie(Mockito.any());

        walletServlet.doPut(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

}