package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.category.CategoryDTO;
import com.pnudev.librarysystem.entity.Category;
import com.pnudev.librarysystem.exception.NotUniqueException;
import com.pnudev.librarysystem.exception.OperationFailedException;
import com.pnudev.librarysystem.mapper.CategoryMapper;
import com.pnudev.librarysystem.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Page<CategoryDTO> searchCategory(String name, Pageable pageable) {
        Page<Category> page = categoryRepository.findAllByNameContainsIgnoreCase(name, pageable);
        return page.map(categoryMapper::toDTO);
    }

    public void createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByNameIgnoreCase(categoryDTO.getName())){
            throw new NotUniqueException("Category with such name already exists");
        }
        Category category = categoryMapper.toEntity(categoryDTO);
        categoryRepository.save(category);
    }

    public void updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id:%d not found".formatted(id)));

        categoryMapper.updateCategoryFromDTO(categoryDTO, category);

        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id: %d not found".formatted(id)));

        if (!category.getBooks().isEmpty()) {
            throw new OperationFailedException("Category with id: %d is used".formatted(id));
        }

        categoryRepository.deleteById(id);
    }

}
