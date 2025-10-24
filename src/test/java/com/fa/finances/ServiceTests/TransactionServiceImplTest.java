package com.fa.finances.ServiceTests;


import com.fa.finances.dto.TransactionDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.models.*;
import com.fa.finances.repositories.*;
import com.fa.finances.requests.TransactionRequest;
import com.fa.finances.services.implementaions.TransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User user;
    private Category category;
    private Transaction transaction;
    private TransactionRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .email("test@email.com")
                .name("Test User")
                .build();

        category = Category.builder()
                .id(1L)
                .name("Food")
                .build();

        transaction = Transaction.builder()
                .id(1L)
                .user(user)
                .category(category)
                .amount(BigDecimal.valueOf(50.0))
                .type(TransactionType.EXPENSE)
                .date(LocalDate.now())
                .description("Dinner")
                .build();

        request = new TransactionRequest();
        request.setAmount(BigDecimal.valueOf(50.0));
        request.setCategoryId(1L);
        request.setDate(LocalDate.now());
        request.setDescription("Dinner");
        request.setType(TransactionType.EXPENSE);
    }

    @Test
    void create_shouldSaveTransaction_whenValid() throws FinancesException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Long id = transactionService.create(request, 1L);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
        assertNotNull(id);
    }

    @Test
    void create_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FinancesException.class, () -> transactionService.create(request, 1L));
    }

    @Test
    void update_shouldUpdateTransaction_whenValid() throws FinancesException {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        transactionService.update(1L, request);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void delete_shouldRemoveTransaction_whenExists() throws FinancesException {
        when(transactionRepository.existsById(1L)).thenReturn(true);

        transactionService.delete(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        when(transactionRepository.existsById(1L)).thenReturn(false);

        assertThrows(FinancesException.class, () -> transactionService.delete(1L));
    }

    @Test
    void getById_shouldReturnTransaction_whenFound() throws FinancesException {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionDTO dto = transactionService.getById(1L);

        assertEquals(transaction.getAmount(), dto.getAmount());
        assertEquals(transaction.getDescription(), dto.getDescription());
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FinancesException.class, () -> transactionService.getById(1L));
    }

    @Test
    void getAll_shouldReturnTransactionsForUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(transactionRepository.findByUser(user)).thenReturn(List.of(transaction));

        List<TransactionDTO> dtos = transactionService.getAll(1L);

        assertEquals(1, dtos.size());
        assertEquals(transaction.getAmount(), dtos.get(0).getAmount());
    }

    @Test
    void getByDateRange_shouldReturnFilteredTransactions() throws FinancesException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(transactionRepository.findByUserAndDateBetween(any(), any(), any()))
                .thenReturn(List.of(transaction));

        List<TransactionDTO> dtos = transactionService.getByDateRange(
                1L, LocalDate.now().minusDays(1), LocalDate.now());

        assertEquals(1, dtos.size());
    }

    @Test
    void getByCategory_shouldReturnTransactions() throws FinancesException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.findByUserAndCategory(user, category))
                .thenReturn(List.of(transaction));

        List<TransactionDTO> dtos = transactionService.getByCategory(1L, 1L);

        assertEquals(1, dtos.size());
        assertEquals(transaction.getDescription(), dtos.get(0).getDescription());
    }
}

