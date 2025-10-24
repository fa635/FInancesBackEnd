package com.fa.finances.ServiceTests;


import com.fa.finances.dto.BudgetDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.models.Budget;
import com.fa.finances.models.Category;
import com.fa.finances.models.User;
import com.fa.finances.repositories.BudgetRepository;
import com.fa.finances.repositories.CategoryRepository;
import com.fa.finances.repositories.UserRepository;
import com.fa.finances.requests.BudgetRequest;
import com.fa.finances.services.implementaions.BudgetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BudgetServiceImplTest {

    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private User user;
    private Category category;
    private BudgetRequest budgetRequest;
    private Budget budget;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        category = new Category();
        category.setId(1L);

        budgetRequest = new BudgetRequest();
        budgetRequest.setMonth("2025-10");
        budgetRequest.setAmount(BigDecimal.valueOf(500.0));
        budgetRequest.setCategoryId(1L);

        budget = Budget.builder()
                .id(1L)
                .user(user)
                .category(category)
                .month("2025-10")
                .amount(BigDecimal.valueOf(500.0))
                .build();
    }

    @Test
    void createBudget_Success() throws FinancesException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(budgetRepository.findByUserAndMonthAndCategory(any(User.class), anyString(), any(Category.class)))
                .thenReturn(Optional.empty());
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Long id = budgetService.create(budgetRequest, 1L);
        assertEquals(1L, id);
    }

    @Test
    void createBudget_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class, () ->
                budgetService.create(budgetRequest, 1L)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void createBudget_CategoryNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class, () ->
                budgetService.create(budgetRequest, 1L)
        );

        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void createBudget_AlreadyExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(budgetRepository.findByUserAndMonthAndCategory(any(User.class), anyString(), any(Category.class)))
                .thenReturn(Optional.of(budget));

        FinancesException exception = assertThrows(FinancesException.class, () ->
                budgetService.create(budgetRequest, 1L)
        );

        assertEquals("Budget already exists for this month and category", exception.getMessage());
    }

    @Test
    void updateBudget_Success() throws FinancesException {
        when(budgetRepository.findById(anyLong())).thenReturn(Optional.of(budget));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        budgetService.update(1L, budgetRequest);
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    @Test
    void updateBudget_NotFound() {
        when(budgetRepository.findById(anyLong())).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class, () ->
                budgetService.update(1L, budgetRequest)
        );

        assertEquals("Budget not found", exception.getMessage());
    }

    @Test
    void deleteBudget_Success() throws FinancesException {
        when(budgetRepository.existsById(anyLong())).thenReturn(true);

        budgetService.delete(1L);
        verify(budgetRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBudget_NotFound() {
        when(budgetRepository.existsById(anyLong())).thenReturn(false);

        FinancesException exception = assertThrows(FinancesException.class, () ->
                budgetService.delete(1L)
        );

        assertEquals("Budget not found", exception.getMessage());
    }

    @Test
    void getAllBudgets_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(budgetRepository.findByUser(any(User.class)))
                .thenReturn(Collections.singletonList(budget));

        assertEquals(1, budgetService.getAll(1L).size());
    }

    @Test
    void getById_Success() throws FinancesException {
        when(budgetRepository.findById(anyLong())).thenReturn(Optional.of(budget));

        BudgetDTO dto = budgetService.getById(1L);
        assertEquals(500.0, dto.getAmount());
    }

    @Test
    void getById_NotFound() {
        when(budgetRepository.findById(anyLong())).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class, () ->
                budgetService.getById(1L)
        );

        assertEquals("Budget not found", exception.getMessage());
    }

    @Test
    void getByMonthAndCategory_Success() throws FinancesException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(budgetRepository.findByUserAndMonthAndCategory(any(User.class), anyString(), any(Category.class)))
                .thenReturn(Optional.of(budget));

        BudgetDTO dto = budgetService.getByMonthAndCategory(1L, "2025-10", 1L);
        assertEquals(500.0, dto.getAmount());
    }

    @Test
    void getByMonthAndCategory_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(budgetRepository.findByUserAndMonthAndCategory(any(User.class), anyString(), any(Category.class)))
                .thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class, () ->
                budgetService.getByMonthAndCategory(1L, "2025-10", 1L)
        );

        assertEquals("Budget not found", exception.getMessage());
    }
}

