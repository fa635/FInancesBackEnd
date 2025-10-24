package com.fa.finances.ServiceTests;


import com.fa.finances.dto.CategoryDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.models.Category;
import com.fa.finances.models.User;
import com.fa.finances.repositories.CategoryRepository;
import com.fa.finances.repositories.UserRepository;
import com.fa.finances.requests.CategoryRequest;
import com.fa.finances.services.implementaions.CategoryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Category category;
    private CategoryRequest categoryRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        category = Category.builder()
                .id(1L)
                .name("Groceries")
                .user(user)
                .build();

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Groceries");
    }

    @Test
    void createCategory_Success() throws FinancesException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Long id = categoryService.create(categoryRequest, 1L);

        assertEquals(1L, id);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class,
                () -> categoryService.create(categoryRequest, 1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void updateCategory_Success() throws FinancesException {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        categoryRequest.setName("Utilities");
        categoryService.update(1L, categoryRequest);

        verify(categoryRepository, times(1)).save(any(Category.class));
        assertEquals("Utilities", category.getName());
    }

    @Test
    void updateCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class,
                () -> categoryService.update(1L, categoryRequest));
        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void deleteCategory_Success() throws FinancesException {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.delete(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCategory_NotFound() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        FinancesException exception = assertThrows(FinancesException.class,
                () -> categoryService.delete(1L));
        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void getAllCategories_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findByUser(user)).thenReturn(Collections.singletonList(category));

        List<CategoryDTO> result = categoryService.getAll(1L);

        assertEquals(1, result.size());
        assertEquals("Groceries", result.get(0).getName());
    }

    @Test
    void getCategoryById_Success() throws FinancesException {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDTO dto = categoryService.getById(1L);

        assertEquals("Groceries", dto.getName());
        assertEquals(1L, dto.getId());
    }

    @Test
    void getCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class,
                () -> categoryService.getById(1L));
        assertEquals("Category not found", exception.getMessage());
    }
}
