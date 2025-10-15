package com.fa.finances.repositories;


import com.fa.finances.models.Budget;
import com.fa.finances.models.User;
import com.fa.finances.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUser(User user);
    Optional<Budget> findByUserAndMonthAndCategory(User user, String month, Category category);
}

