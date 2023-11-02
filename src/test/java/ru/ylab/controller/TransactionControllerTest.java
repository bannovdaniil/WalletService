package ru.ylab.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.ylab.exception.RepositoryException;
import ru.ylab.model.Transaction;
import ru.ylab.model.TransactionType;
import ru.ylab.service.SessionService;
import ru.ylab.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionControllerTest {
    @Mock
    private TransactionService mockTransactionService;
    @Mock
    private SessionService mockSessionService;
    @InjectMocks
    private TransactionController controller;
    private MockMvc mvc;
    private Transaction dto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        dto = new Transaction(
                1L,
                LocalDateTime.now(),
                TransactionType.GET,
                BigDecimal.TEN,
                2L
        );
    }

    @DisplayName("get Transaction List")
    @Test
    void getTransactionList() throws Exception {
        Mockito.doReturn(List.of(dto)).when(mockTransactionService).findAll();
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        mvc.perform(get("/api/transaction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dto.getId()))
                .andExpect(jsonPath("$[0].type").value(dto.getType().toString()))
                .andExpect(jsonPath("$[0].sum").value(dto.getSum()))
                .andExpect(jsonPath("$[0].userId").value(dto.getUserId()));

    }

    @DisplayName("get Transaction List - AccessDenied")
    @Test
    void getTransactionListAccessDenied() throws Exception {
        Mockito.doReturn(List.of(dto)).when(mockTransactionService).findAll();
        Mockito.doReturn(false).when(mockSessionService).isActive(Mockito.any());

        mvc.perform(get("/api/transaction"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("get Transaction List - BadRequest")
    @Test
    void getTransactionListBadRequest() throws Exception {
        Mockito.doReturn(Optional.of(UUID.randomUUID())).when(mockSessionService).getUuidFromCookie(Mockito.any());
        Mockito.doReturn(true).when(mockSessionService).isActive(Mockito.any());

        Mockito.doThrow(new RepositoryException("Test SQL exception.")).when(mockTransactionService).findAll();

        mvc.perform(get("/api/transaction"))
                .andExpect(status().isBadRequest());
    }

}