package com.fa.finances.ControllerTests;


import com.fa.finances.controllers.GoalController;
import com.fa.finances.dto.GoalDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.requests.GoalRequest;
import com.fa.finances.services.interfaces.IGoalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GoalController.class)
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IGoalService goalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createGoal_Success() throws Exception {
        when(goalService.create(any(GoalRequest.class), anyLong())).thenReturn(1L);

        GoalRequest req = new GoalRequest();
        req.setName("Save Car");
        req.setTargetAmount(BigDecimal.valueOf(5000));
        req.setCurrentAmount(BigDecimal.valueOf(1000));
        req.setDeadline(LocalDate.now().plusMonths(6));

        mockMvc.perform(post("/api/goals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Goal created successfully"))
                .andExpect(jsonPath("$.dati").value(1));
    }

    @Test
    void createGoal_Failure() throws Exception {
        when(goalService.create(any(GoalRequest.class), anyLong()))
                .thenThrow(new FinancesException("User not found"));

        GoalRequest req = new GoalRequest();
        req.setName("Save Car");

        mockMvc.perform(post("/api/goals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("User not found"));
    }

    @Test
    void updateGoal_Success() throws Exception {
        GoalRequest req = new GoalRequest();
        req.setName("Save Bike");

        mockMvc.perform(put("/api/goals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Goal updated successfully"));
    }

    @Test
    void deleteGoal_Success() throws Exception {
        mockMvc.perform(delete("/api/goals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Goal deleted successfully"));
    }

    @Test
    void getAllGoals_Success() throws Exception {
        GoalDTO dto = new GoalDTO();
        dto.setId(1L);
        dto.setName("Save Car");

        when(goalService.getAll(1L)).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/goals/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Goals retrieved successfully"))
                .andExpect(jsonPath("$.dati[0].name").value("Save Car"));
    }

    @Test
    void getGoalById_Success() throws Exception {
        GoalDTO dto = new GoalDTO();
        dto.setId(1L);
        dto.setName("Save Car");

        when(goalService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/goals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Goal retrieved successfully"))
                .andExpect(jsonPath("$.dati.name").value("Save Car"));
    }
}

