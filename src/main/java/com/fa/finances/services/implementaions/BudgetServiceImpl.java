package com.fa.finances.services.implementaions;


import com.fa.finances.dto.BudgetDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.models.Budget;
import com.fa.finances.models.Category;
import com.fa.finances.models.User;
import com.fa.finances.repositories.BudgetRepository;
import com.fa.finances.repositories.CategoryRepository;
import com.fa.finances.repositories.UserRepository;
import com.fa.finances.requests.BudgetRequest;
import com.fa.finances.services.interfaces.IBudgetService;
import com.fa.finances.utils.BudgetMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements IBudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(BudgetRequest req, Long userId) throws FinancesException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FinancesException("User not found"));

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new FinancesException("Category not found"));


        if (budgetRepository.findByUserAndMonthAndCategory(user, req.getMonth(), category).isPresent()) {
            throw new FinancesException("Budget already exists for this month and category");
        }

        Budget budget = Budget.builder()
                .month(req.getMonth())
                .amount(req.getAmount())
                .category(category)
                .user(user)
                .build();

        budgetRepository.save(budget);
        return budget.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, BudgetRequest req) throws FinancesException {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new FinancesException("Budget not found"));

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new FinancesException("Category not found"));

        budget.setMonth(req.getMonth());
        budget.setAmount(req.getAmount());
        budget.setCategory(category);

        budgetRepository.save(budget);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) throws FinancesException {
        if (!budgetRepository.existsById(id)) {
            throw new FinancesException("Budget not found");
        }
        budgetRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetDTO> getAll(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return budgetRepository.findByUser(user).stream()
                .map(BudgetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetDTO getById(Long id) throws FinancesException {
    	return BudgetMapper.toDTO(budgetRepository.findById(id)
    			.orElseThrow(() -> new FinancesException("Budget not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetDTO getByMonthAndCategory(Long userId, String month, Long categoryId) throws FinancesException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FinancesException("User not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new FinancesException("Category not found"));

        return BudgetMapper.toDTO(budgetRepository.findByUserAndMonthAndCategory(user, month, category)
        		.orElseThrow(() -> new FinancesException("Budget not found")));
    }

}

