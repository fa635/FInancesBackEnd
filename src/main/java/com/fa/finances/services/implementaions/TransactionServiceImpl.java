package com.fa.finances.services.implementaions;


import com.fa.finances.dto.TransactionDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.models.*;
import com.fa.finances.repositories.*;
import com.fa.finances.requests.TransactionRequest;
import com.fa.finances.services.interfaces.ITransactionService;
import com.fa.finances.utils.TransactionMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public Long create(TransactionRequest req, Long userId) throws FinancesException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FinancesException("User not found"));
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new FinancesException("Category not found"));

        Transaction t = Transaction.builder()
                .date(req.getDate())
                .amount(req.getAmount())
                .type(req.getType())
                .description(req.getDescription())
                .category(category)
                .user(user)
                .build();

        transactionRepository.save(t);
        return t.getId();
    }

    @Override
    public void update(Long id, TransactionRequest req) throws FinancesException {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new FinancesException("Transaction not found"));

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new FinancesException("Category not found"));

        t.setDate(req.getDate());
        t.setAmount(req.getAmount());
        t.setType(req.getType());
        t.setDescription(req.getDescription());
        t.setCategory(category);

        transactionRepository.save(t);
    }

    @Override
    public void delete(Long id) throws FinancesException {
        if (!transactionRepository.existsById(id)) {
            throw new FinancesException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }

    @Override
    public List<TransactionDTO> getAll(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return transactionRepository.findByUser(user).stream()
                .map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDTO getById(Long id) throws FinancesException {
    	return TransactionMapper.toDTO(transactionRepository.findById(id)
                .orElseThrow(() -> new FinancesException("Transaction not found")));
    }

    @Override
    public List<TransactionDTO> getByDateRange(Long userId, LocalDate startDate, LocalDate endDate) throws FinancesException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FinancesException("User not found"));
        return transactionRepository.findByUserAndDateBetween(user, startDate, endDate)
                .stream().map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getByCategory(Long userId, Long categoryId) throws FinancesException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FinancesException("User not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new FinancesException("Category not found"));

        return transactionRepository.findByUserAndCategory(user, category)
                .stream().map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }

}

