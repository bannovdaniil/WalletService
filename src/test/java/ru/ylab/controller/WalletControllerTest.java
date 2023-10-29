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
import ru.ylab.model.dto.BalanceType;
import ru.ylab.model.dto.WalletIncomingDto;
import ru.ylab.model.dto.WalletOutDto;
import ru.ylab.service.SessionService;
import ru.ylab.service.WalletService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WalletControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private WalletService mockWalletService;
    @Mock
    private SessionService mockSessionService;
    @InjectMocks
    private WalletController controller;
    private MockMvc mvc;
    private WalletOutDto outDto;
    private WalletIncomingDto inDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        outDto = new WalletOutDto(
                BigDecimal.TEN
        );
        inDto = new WalletIncomingDto(
                BalanceType.GET,
                "10"
        );
    }

    @Test
    void getWalletBalance() throws Exception {
        Mockito.doReturn(outDto).when(mockWalletService).getBalance(Mockito.any());
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        mvc.perform(get("/api/wallet"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.balance").value(outDto.balance()));

    }

    @Test
    void changeBalance() throws Exception {
        Mockito.doReturn(outDto).when(mockWalletService).changeBalance(Mockito.any(), Mockito.any());
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        mvc.perform(put("/api/wallet")
                        .content(mapper.writeValueAsString(inDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(outDto.balance()));
    }

}