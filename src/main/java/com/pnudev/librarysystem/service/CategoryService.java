package com.pnudev.librarysystem.service;

import com.pnudev.librarysystem.dto.CategoryDTO;
import com.pnudev.librarysystem.entity.Category;
import com.pnudev.librarysystem.mapper.CategoryMapper;
import com.pnudev.librarysystem.repository.CategoryRepository;
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

}
