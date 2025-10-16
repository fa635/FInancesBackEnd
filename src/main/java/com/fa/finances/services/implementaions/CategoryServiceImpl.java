package com.fa.finances.services.implementaions;


import com.fa.finances.dto.CategoryDTO;
import com.fa.finances.exception.FinancesException;
import com.fa.finances.models.Category;
import com.fa.finances.models.User;
import com.fa.finances.repositories.CategoryRepository;
import com.fa.finances.repositories.UserRepository;
import com.fa.finances.requests.CategoryRequest;
import com.fa.finances.services.interfaces.ICategoryService;
import com.fa.finances.utils.CategoryMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public Long create(CategoryRequest req, Long userId) throws FinancesException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new FinancesException("User not found"));

        Category category = Category.builder()
                .name(req.getName())
                .user(user)
                .build();

        categoryRepository.save(category);
        return category.getId();
    }

    @Override
    public void update(Long id, CategoryRequest req) throws FinancesException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new FinancesException("Category not found"));

        category.setName(req.getName());
        categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) throws FinancesException {
        if (!categoryRepository.existsById(id)) {
            throw new FinancesException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDTO> getAll(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return categoryRepository.findByUser(user).stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getById(Long id) throws FinancesException {
    	return CategoryMapper.toDTO(categoryRepository.findById(id)
                .orElseThrow(() -> new FinancesException("Category not found")));
    }
}
