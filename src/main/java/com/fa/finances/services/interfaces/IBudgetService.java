package com.fa.finances.services.interfaces;


import com.fa.finances.dto.BudgetDTO;
import com.fa.finances.requests.BudgetRequest;
import com.fa.finances.exception.FinancesException;

import java.util.List;

public interface IBudgetService {

    Long create(BudgetRequest req, Long userId) throws FinancesException;

    void update(Long id, BudgetRequest req) throws FinancesException;

    void delete(Long id) throws FinancesException;

    List<BudgetDTO> getAll(Long userId);

    BudgetDTO getById(Long id) throws FinancesException;

    BudgetDTO getByMonthAndCategory(Long userId, String month, Long categoryId) throws FinancesException;
}
