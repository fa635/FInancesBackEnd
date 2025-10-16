package com.fa.finances.utils;


import com.fa.finances.dto.TransactionDTO;
import com.fa.finances.dto.CategoryDTO;
import com.fa.finances.models.Transaction;

public class TransactionMapper {

    public static TransactionDTO toDTO(Transaction t) {
        if (t == null) return null;
        CategoryDTO catDTO = CategoryMapper.toDTO(t.getCategory());
        return new TransactionDTO(t.getId(), t.getDate(), t.getAmount(), t.getType(), t.getDescription(), catDTO);
    }
}

