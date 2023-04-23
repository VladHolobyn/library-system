package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.CategoryDTO;
import com.pnudev.librarysystem.entity.Category;
import com.pnudev.librarysystem.exception.DeleteFailedException;
import com.pnudev.librarysystem.mapper.CategoryMapper;
import com.pnudev.librarysystem.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Page<CategoryDTO> searchCategoryByName(String name, int pageNumber, int size){
        Page<Category> page = categoryRepository.findAllByNameContains(name, PageRequest.of(pageNumber, size));
        return page.map(categoryMapper::toDTO);
    }

    public CategoryDTO addCategory(CategoryDTO categoryDTO){
        return categoryMapper.toDTO(categoryRepository.save(categoryMapper.toEntity(categoryDTO)));
    }

    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        Long id = categoryDTO.getId();
        Category updated = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id:%d not found".formatted(id)));

        categoryMapper.updateCategoryFromDTO(categoryDTO, updated);

        return categoryMapper.toDTO(categoryRepository.save(updated));
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category with id: %d not found".formatted(id)));

        if(!category.getBooks().isEmpty()){
            throw new DeleteFailedException("Category with id: %d is used".formatted(id));
        }

        categoryRepository.deleteById(id);
    }

    public List<CategoryDTO> findAllById(List<Long> ids){
        return categoryRepository.findAllById(ids).stream().map(categoryMapper::toDTO).toList();
    }

}
