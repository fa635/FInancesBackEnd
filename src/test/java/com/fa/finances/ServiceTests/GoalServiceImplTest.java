package com.fa.finances.ServiceTests;


import com.fa.finances.dto.GoalDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.models.Goal;
import com.fa.finances.models.User;
import com.fa.finances.repositories.GoalRepository;
import com.fa.finances.repositories.UserRepository;
import com.fa.finances.requests.GoalRequest;
import com.fa.finances.services.implementaions.GoalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceImplTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GoalServiceImpl goalService;

    private GoalRequest request;
    private User user;
    private Goal goal;

    @BeforeEach
    void setUp() {
        request = new GoalRequest();
        request.setName("Save for a car");
        request.setTargetAmount(BigDecimal.valueOf(5000));
        request.setCurrentAmount(BigDecimal.valueOf(1000));
        request.setDeadline(LocalDate.now().plusMonths(6));

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        goal = Goal.builder()
                .id(1L)
                .name(request.getName())
                .targetAmount(request.getTargetAmount())
                .currentAmount(request.getCurrentAmount())
                .deadline(request.getDeadline())
                .user(user)
                .build();
    }

    @Test
    void createGoal_Success() throws FinancesException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Long id = goalService.create(request, 1L);

        assertEquals(1L, id);
        verify(goalRepository, times(1)).save(any(Goal.class));
    }

    @Test
    void createGoal_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class,
                () -> goalService.create(request, 1L));

        assertEquals("User not found", exception.getMessage());
        verify(goalRepository, never()).save(any());
    }

    @Test
    void updateGoal_Success() throws FinancesException {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        goalService.update(1L, request);

        verify(goalRepository, times(1)).save(goal);
        assertEquals(request.getName(), goal.getName());
        assertEquals(request.getTargetAmount(), goal.getTargetAmount());
    }

    @Test
    void updateGoal_NotFound() {
        when(goalRepository.findById(1L)).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class,
                () -> goalService.update(1L, request));

        assertEquals("Goal not found", exception.getMessage());
    }

    @Test
    void deleteGoal_Success() throws FinancesException {
        when(goalRepository.existsById(1L)).thenReturn(true);

        goalService.delete(1L);

        verify(goalRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteGoal_NotFound() {
        when(goalRepository.existsById(1L)).thenReturn(false);

        FinancesException exception = assertThrows(FinancesException.class,
                () -> goalService.delete(1L));

        assertEquals("Goal not found", exception.getMessage());
    }

    @Test
    void getAllGoals_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(goalRepository.findByUser(user)).thenReturn(Collections.singletonList(goal));

        var result = goalService.getAll(1L);

        assertEquals(1, result.size());
        assertEquals(goal.getName(), result.get(0).getName());
    }

    @Test
    void getGoalById_Success() throws FinancesException {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        GoalDTO result = goalService.getById(1L);

        assertNotNull(result);
        assertEquals(goal.getName(), result.getName());
    }

    @Test
    void getGoalById_NotFound() {
        when(goalRepository.findById(1L)).thenReturn(Optional.empty());

        FinancesException exception = assertThrows(FinancesException.class,
                () -> goalService.getById(1L));

        assertEquals("Goal not found", exception.getMessage());
    }
}

