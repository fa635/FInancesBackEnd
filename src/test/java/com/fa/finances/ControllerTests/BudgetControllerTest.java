package com.fa.finances.ControllerTests;


import com.fa.finances.controllers.BudgetController;
import com.fa.finances.dto.BudgetDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.requests.BudgetRequest;
import com.fa.finances.services.interfaces.IBudgetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BudgetController.class)
class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IBudgetService budgetService;

    private BudgetRequest budgetRequest;
    private BudgetDTO budgetDTO;

    @BeforeEach
    void setUp() {
        budgetRequest = new BudgetRequest();
        budgetRequest.setMonth("2025-10");
        budgetRequest.setAmount(BigDecimal.valueOf(500.0));
        budgetRequest.setCategoryId(1L);

        budgetDTO = new BudgetDTO();
        budgetDTO.setId(1L);
        budgetDTO.setAmount(BigDecimal.valueOf(500.0));
        budgetDTO.setMonth("2025-10");
    }

    @Test
    void createBudget_Success() throws Exception {
        when(budgetService.create(any(BudgetRequest.class), anyLong())).thenReturn(1L);

        mockMvc.perform(post("/api/budgets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.dati").value(1));
    }

    @Test
    void createBudget_Exception() throws Exception {
        when(budgetService.create(any(BudgetRequest.class), anyLong()))
                .thenThrow(new FinancesException("Budget error"));

        mockMvc.perform(post("/api/budgets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("Budget error"));
    }

    @Test
    void updateBudget_Success() throws Exception {
        doNothing().when(budgetService).update(anyLong(), any(BudgetRequest.class));

        mockMvc.perform(put("/api/budgets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true));
    }

    @Test
    void deleteBudget_Success() throws Exception {
        doNothing().when(budgetService).delete(anyLong());

        mockMvc.perform(delete("/api/budgets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true));
    }

    @Test
    void getAllBudgets_Success() throws Exception {
        when(budgetService.getAll(anyLong())).thenReturn(Collections.singletonList(budgetDTO));

        mockMvc.perform(get("/api/budgets/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.dati[0].id").value(1));
    }

    @Test
    void getBudgetById_Success() throws Exception {
        when(budgetService.getById(anyLong())).thenReturn(budgetDTO);

        mockMvc.perform(get("/api/budgets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.dati.id").value(1));
    }

    @Test
    void getBudgetByMonthAndCategory_Success() throws Exception {
        when(budgetService.getByMonthAndCategory(anyLong(), anyString(), anyLong())).thenReturn(budgetDTO);

        mockMvc.perform(get("/api/budgets/filter")
                        .param("userId", "1")
                        .param("month", "2025-10")
                        .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.dati.id").value(1));
    }

    @Test
    void getBudgetByMonthAndCategory_Exception() throws Exception {
        when(budgetService.getByMonthAndCategory(anyLong(), anyString(), anyLong()))
                .thenThrow(new FinancesException("Budget not found"));

        mockMvc.perform(get("/api/budgets/filter")
                        .param("userId", "1")
                        .param("month", "2025-10")
                        .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("Budget not found"));
    }
}
