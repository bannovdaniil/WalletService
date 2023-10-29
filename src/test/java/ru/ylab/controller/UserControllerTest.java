package ru.ylab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import ru.ylab.model.dto.UserIncomingDto;
import ru.ylab.model.dto.UserOutDto;
import ru.ylab.service.SessionService;
import ru.ylab.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private UserService mockUserService;
    @Mock
    private SessionService mockSessionService;
    @InjectMocks
    private UserController controller;
    private MockMvc mvc;
    private UserIncomingDto inDto;
    private UserOutDto outDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        inDto = new UserIncomingDto(
                "F1 test name",
                "L2 lastname",
                "password"
        );
        outDto = new UserOutDto(
                123L,
                "f1 123L",
                "l2 123v"
        );
    }

    @Test
    void getUserById() throws Exception {
        Mockito.doReturn(outDto).when(mockUserService).findById(Mockito.anyLong());
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        mvc.perform(get("/api/user/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outDto.getId()))
                .andExpect(jsonPath("$.firstName").value(outDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(outDto.getLastName()));

    }

    @Test
    void getUserList() throws Exception {
        Mockito.doReturn(List.of(outDto)).when(mockUserService).findAll();
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        mvc.perform(get("/api/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(outDto.getId()))
                .andExpect(jsonPath("$[0].firstName").value(outDto.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(outDto.getLastName()));

    }

    @Test
    void createUser() throws Exception {
        Mockito.doReturn(outDto).when(mockUserService).add(Mockito.any());

        mvc.perform(post("/api/user")
                        .content(mapper.writeValueAsString(inDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outDto.getId()))
                .andExpect(jsonPath("$.firstName").value(outDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(outDto.getLastName()));

    }

}