package com.fa.finances.services.interfaces;


import com.fa.finances.dto.CategoryDTO;
import com.fa.finances.requests.CategoryRequest;
import com.fa.finances.exception.FinancesException;

import java.util.List;

public interface ICategoryService {

    Long create(CategoryRequest req, Long userId) throws FinancesException;

    void update(Long id, CategoryRequest req) throws FinancesException;

    void delete(Long id) throws FinancesException;

    List<CategoryDTO> getAll(Long userId);

    CategoryDTO getById(Long id) throws FinancesException;
}

