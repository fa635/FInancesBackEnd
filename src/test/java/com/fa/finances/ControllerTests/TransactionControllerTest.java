package com.fa.finances.ControllerTests;


import com.fa.finances.controllers.TransactionController;
import com.fa.finances.dto.TransactionDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.requests.TransactionRequest;
import com.fa.finances.services.interfaces.ITransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ITransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private TransactionRequest request;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        request = new TransactionRequest();
        request.setAmount(BigDecimal.valueOf(100.0));
        request.setCategoryId(1L);
        request.setDate(LocalDate.now());
        request.setDescription("Test Transaction");

        transactionDTO = new TransactionDTO();
        transactionDTO.setId(1L);
        transactionDTO.setAmount(BigDecimal.valueOf(100.0));
        transactionDTO.setDescription("Test Transaction");
        transactionDTO.setDate(LocalDate.now());
    }

    @Test
    void createTransaction_Success() throws Exception {
        Mockito.when(transactionService.create(any(TransactionRequest.class), eq(1L)))
                .thenReturn(1L);

        mockMvc.perform(post("/api/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Transaction created successfully"))
                .andExpect(jsonPath("$.dati").value(1L));
    }

    @Test
    void createTransaction_Failure() throws Exception {
        Mockito.when(transactionService.create(any(TransactionRequest.class), eq(1L)))
                .thenThrow(new FinancesException("User not found"));

        mockMvc.perform(post("/api/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("User not found"));
    }

    @Test
    void updateTransaction_Success() throws Exception {
        mockMvc.perform(put("/api/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Transaction updated successfully"));
    }

    @Test
    void deleteTransaction_Success() throws Exception {
        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Transaction deleted successfully"));
    }

    @Test
    void getAllTransactions_Success() throws Exception {
        List<TransactionDTO> list = Collections.singletonList(transactionDTO);
        Mockito.when(transactionService.getAll(1L)).thenReturn(list);

        mockMvc.perform(get("/api/transactions/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Transactions retrieved successfully"))
                .andExpect(jsonPath("$.dati[0].id").value(1L));
    }

    @Test
    void getTransactionById_Success() throws Exception {
        Mockito.when(transactionService.getById(1L)).thenReturn(transactionDTO);

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Transaction retrieved successfully"))
                .andExpect(jsonPath("$.dati.id").value(1L));
    }

    @Test
    void getByDateRange_Success() throws Exception {
        List<TransactionDTO> list = Collections.singletonList(transactionDTO);
        Mockito.when(transactionService.getByDateRange(eq(1L), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(list);

        mockMvc.perform(get("/api/transactions/filter/date")
                        .param("userId", "1")
                        .param("startDate", LocalDate.now().minusDays(5).toString())
                        .param("endDate", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Transactions retrieved successfully"))
                .andExpect(jsonPath("$.dati[0].id").value(1L));
    }

    @Test
    void getByCategory_Success() throws Exception {
        List<TransactionDTO> list = Collections.singletonList(transactionDTO);
        Mockito.when(transactionService.getByCategory(eq(1L), eq(2L)))
                .thenReturn(list);

        mockMvc.perform(get("/api/transactions/filter/category")
                        .param("userId", "1")
                        .param("categoryId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Transactions retrieved successfully"))
                .andExpect(jsonPath("$.dati[0].id").value(1L));
    }
}

