package ru.ylab.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.ylab.exception.RepositoryException;
import ru.ylab.service.SessionService;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LogoutControllerTest {
    @Mock
    private SessionService mockSessionService;
    @InjectMocks
    private LogoutController controller;
    private MockMvc mvc;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void logoutAction() throws Exception {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        mvc.perform(get("/api/logout"))
                .andExpect(status().isOk());
    }

    @Test
    void logoutActionAccessDenied() throws Exception {
        Mockito.doReturn(Optional.empty()).when(mockSessionService).getUuidFromCookie(Mockito.any());

        mvc.perform(get("/api/logout"))
                .andExpect(status().isForbidden());
    }

    @Test
    void logoutActionBadRequest() throws Exception {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());
        Mockito.doThrow(new RepositoryException("Test SQL exception.")).when(mockSessionService).logout(Mockito.any());

        mvc.perform(get("/api/logout"))
                .andExpect(status().isBadRequest());
    }
}