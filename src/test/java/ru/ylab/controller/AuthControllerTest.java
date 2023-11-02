package ru.ylab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.ylab.exception.RepositoryException;
import ru.ylab.model.dto.UserLoginDto;
import ru.ylab.service.SessionService;

import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private SessionService mockSessionService;
    @InjectMocks
    private AuthController controller;
    private MockMvc mvc;
    private UserLoginDto dto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        dto = new UserLoginDto(
                123L,
                "password"
        );

    }

    @DisplayName("User Login")
    @Test
    void loginAction() throws Exception {
        Mockito.doReturn(UUID.randomUUID()).when(mockSessionService).login(Mockito.any());
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        mvc.perform(post("/api/login")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @DisplayName("User Login - AccessDenied")
    @Test
    void loginActionAccessDenied() throws Exception {
        Mockito.doThrow(new AccessDeniedException("Test exception.")).when(mockSessionService).login(Mockito.any());

        mvc.perform(post("/api/login")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @DisplayName("User Login - BadRequest")
    @Test
    void loginActionBadRequest() throws Exception {
        Mockito.doThrow(new RepositoryException("Test SQL exception.")).when(mockSessionService).login(Mockito.any());

        mvc.perform(post("/api/login")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("User Logout")
    @Test
    void logoutAction() throws Exception {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        mvc.perform(get("/api/logout"))
                .andExpect(status().isOk());
    }

    @DisplayName("User Logout - AccessDenied")
    @Test
    void logoutActionAccessDenied() throws Exception {
        Mockito.doReturn(Optional.empty()).when(mockSessionService).getUuidFromCookie(Mockito.any());

        mvc.perform(get("/api/logout"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("User Logout - BadRequest")
    @Test
    void logoutActionBadRequest() throws Exception {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());
        Mockito.doThrow(new RepositoryException("Test SQL exception.")).when(mockSessionService).logout(Mockito.any());

        mvc.perform(get("/api/logout"))
                .andExpect(status().isBadRequest());
    }
}