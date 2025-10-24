package com.fa.finances.ControllerTests;


import com.fa.finances.controllers.CategoryController;
import com.fa.finances.dto.CategoryDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.requests.CategoryRequest;
import com.fa.finances.services.interfaces.ICategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryRequest categoryRequest;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Groceries");

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Groceries");
    }

    @Test
    void createCategory_Success() throws Exception {
        when(categoryService.create(any(CategoryRequest.class), anyLong())).thenReturn(1L);

        mockMvc.perform(post("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc", is(true)))
                .andExpect(jsonPath("$.msg", is("Category created successfully")))
                .andExpect(jsonPath("$.dati", is(1)));
    }

    @Test
    void createCategory_Failure() throws Exception {
        when(categoryService.create(any(CategoryRequest.class), anyLong()))
                .thenThrow(new FinancesException("User not found"));

        mockMvc.perform(post("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc", is(false)))
                .andExpect(jsonPath("$.msg", is("User not found")));
    }

    @Test
    void updateCategory_Success() throws Exception {
        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc", is(true)))
                .andExpect(jsonPath("$.msg", is("Category updated successfully")));
    }

    @Test
    void updateCategory_Failure() throws Exception {
        Mockito.doThrow(new FinancesException("Category not found"))
                .when(categoryService).update(anyLong(), any(CategoryRequest.class));

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc", is(false)))
                .andExpect(jsonPath("$.msg", is("Category not found")));
    }

    @Test
    void deleteCategory_Success() throws Exception {
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc", is(true)))
                .andExpect(jsonPath("$.msg", is("Category deleted successfully")));
    }

    @Test
    void deleteCategory_Failure() throws Exception {
        Mockito.doThrow(new FinancesException("Category not found"))
                .when(categoryService).delete(anyLong());

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc", is(false)))
                .andExpect(jsonPath("$.msg", is("Category not found")));
    }

    @Test
    void getAllCategories_Success() throws Exception {
        when(categoryService.getAll(anyLong())).thenReturn(Collections.singletonList(categoryDTO));

        mockMvc.perform(get("/api/categories/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc", is(true)))
                .andExpect(jsonPath("$.msg", is("Categories retrieved successfully")))
                .andExpect(jsonPath("$.dati", hasSize(1)))
                .andExpect(jsonPath("$.dati[0].name", is("Groceries")));
    }

    @Test
    void getCategoryById_Success() throws Exception {
        when(categoryService.getById(anyLong())).thenReturn(categoryDTO);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc", is(true)))
                .andExpect(jsonPath("$.msg", is("Category retrieved successfully")))
                .andExpect(jsonPath("$.dati.name", is("Groceries")));
    }

    @Test
    void getCategoryById_Failure() throws Exception {
        when(categoryService.getById(anyLong()))
                .thenThrow(new FinancesException("Category not found"));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rc", is(false)))
                .andExpect(jsonPath("$.msg", is("Category not found")));
    }
}
