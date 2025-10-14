package com.fa.finances.services.interfaces;


import com.fa.finances.dto.TransactionDTO;
import com.fa.finances.requests.TransactionRequest;
import com.fa.finances.exception.FinancesException;

import java.time.LocalDate;
import java.util.List;

public interface ITransactionService {

    Long create(TransactionRequest req, Long userId) throws FinancesException;

    void update(Long id, TransactionRequest req) throws FinancesException;

    void delete(Long id) throws FinancesException;

    List<TransactionDTO> getAll(Long userId);

    TransactionDTO getById(Long id) throws FinancesException;

    List<TransactionDTO> getByDateRange(Long userId, LocalDate startDate, LocalDate endDate) throws FinancesException;

    List<TransactionDTO> getByCategory(Long userId, Long categoryId) throws FinancesException;
}
