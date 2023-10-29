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
import ru.ylab.model.Action;
import ru.ylab.service.ActionService;
import ru.ylab.service.SessionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * для просмотра mvc: .andDo(print())
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ActionControllerTest {
    @Mock
    private ActionService mockActionService;
    @Mock
    private SessionService mockSessionService;
    @InjectMocks
    private ActionController controller;
    private MockMvc mvc;
    private Action dto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        dto = new Action(
                1L,
                LocalDateTime.now(),
                "user action",
                2L,
                "information"
        );
    }

    @Test
    void getActionList() throws Exception {
        Mockito.doReturn(List.of(dto)).when(mockActionService).findAll();
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        mvc.perform(get("/api/action"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dto.getId()))
                .andExpect(jsonPath("$[0].userAction").value(dto.getUserAction()))
                .andExpect(jsonPath("$[0].userId").value(dto.getUserId()))
                .andExpect(jsonPath("$[0].information").value(dto.getInformation()));

    }

    @Test
    void getActionListAccessDenied() throws Exception {
        Mockito.doReturn(List.of(dto)).when(mockActionService).findAll();
        Mockito.doReturn(false).when(mockSessionService).isActive(Mockito.any());

        mvc.perform(get("/api/action"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getActionListBadRequest() throws Exception {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        Mockito.doThrow(new RepositoryException("Test SQL exception.")).when(mockActionService).findAll();

        mvc.perform(get("/api/action"))
                .andExpect(status().isBadRequest());
    }

}