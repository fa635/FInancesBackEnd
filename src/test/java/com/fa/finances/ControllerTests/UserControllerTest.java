package com.fa.finances.ControllerTests;


import com.fa.finances.controllers.UserController;
import com.fa.finances.dto.UserDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.requests.UserRequest;
import com.fa.finances.services.interfaces.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest userRequest;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {

        userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setEmail("john@example.com");
        userRequest.setPassword("password123");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setRole("USER");
    }

    @Test
    void createUser_success() throws Exception {
        Mockito.when(userService.create(any(UserRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("User created successfully"))
                .andExpect(jsonPath("$.dati").value(1L));
    }

    @Test
    void createUser_emailExists_returnsError() throws Exception {
        Mockito.when(userService.create(any(UserRequest.class)))
                .thenThrow(new FinancesException("Email already in use"));

        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("Email already in use"));
    }

    @Test
    void signin_success() throws Exception {
        Mockito.when(userService.signin(any(UserRequest.class))).thenReturn("jwt-token");

        mockMvc.perform(post("/api/users/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Login successful"))
                .andExpect(jsonPath("$.dati").value("jwt-token"));
    }

    @Test
    void signin_invalidCredentials_returnsError() throws Exception {
        Mockito.when(userService.signin(any(UserRequest.class)))
                .thenThrow(new FinancesException("Invalid email or password"));

        mockMvc.perform(post("/api/users/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("Invalid email or password"));
    }

    @Test
    void updateUser_success() throws Exception {
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("User updated successfully"));
    }

    @Test
    void updateUser_notFound_returnsError() throws Exception {
        Mockito.doThrow(new FinancesException("User not found"))
                .when(userService).update(eq(1L), any(UserRequest.class));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("User not found"));
    }

    @Test
    void deleteUser_success() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("User deleted successfully"));
    }

    @Test
    void deleteUser_notFound_returnsError() throws Exception {
        Mockito.doThrow(new FinancesException("User not found"))
                .when(userService).delete(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("User not found"));
    }

    @Test
    void getAllUsers_success() throws Exception {
        Mockito.when(userService.getAll()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("Users retrieved successfully"))
                .andExpect(jsonPath("$.dati[0].email").value("john@example.com"));
    }

    @Test
    void getUserById_success() throws Exception {
        Mockito.when(userService.getById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(true))
                .andExpect(jsonPath("$.msg").value("User retrieved successfully"))
                .andExpect(jsonPath("$.dati.email").value("john@example.com"));
    }

    @Test
    void getUserById_notFound_returnsError() throws Exception {
        Mockito.when(userService.getById(1L)).thenThrow(new FinancesException("User not found"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc").value(false))
                .andExpect(jsonPath("$.msg").value("User not found"));
    }
}

