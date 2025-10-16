package com.fa.finances.utils;


import com.fa.finances.dto.BudgetDTO;
import com.fa.finances.dto.CategoryDTO;
import com.fa.finances.models.Budget;

public class BudgetMapper {

    public static BudgetDTO toDTO(Budget b) {
        if (b == null) return null;
        CategoryDTO catDTO = CategoryMapper.toDTO(b.getCategory());
        return new BudgetDTO(b.getId(), b.getMonth(), b.getAmount(), catDTO);
    }
}
