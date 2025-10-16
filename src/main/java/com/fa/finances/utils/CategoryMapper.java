package com.fa.finances.utils;


import com.fa.finances.dto.CategoryDTO;
import com.fa.finances.models.Category;

public class CategoryMapper {

    public static CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        return new CategoryDTO(category.getId(), category.getName());
    }
}
