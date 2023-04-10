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

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public Page<CategoryDTO> searchCategoryByName(String name, int pageNumber, int size){
        Page<Category> page = repository.findAllByNameContains(name, PageRequest.of(pageNumber, size));
        return page.map(mapper::toDTO);
    }

    public CategoryDTO addCategory(CategoryDTO categoryDTO){
        return mapper.toDTO(repository.save(mapper.toEntity(categoryDTO)));
    }

    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        Long id = categoryDTO.getId();
        Category updated = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id:%d not found", id)));

        mapper.updateCategoryFromDTO(categoryDTO, updated);

        return mapper.toDTO(repository.save(updated));
    }

    public void deleteCategory(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Category with id: %d not found", id)));
        int usage = category.getBooks().size();

        if(usage > 0){
            throw new DeleteFailedException(String.format("Category with id: %d is used: %d", id, usage));
        }

        repository.deleteById(id);
    }
}
