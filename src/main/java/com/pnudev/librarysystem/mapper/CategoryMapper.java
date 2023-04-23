package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.CategoryDTO;
import com.pnudev.librarysystem.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryDTO categoryDTO);
    List<Category> toEntity(List<CategoryDTO> categoryDTOS);

    CategoryDTO toDTO(Category category);

    void updateCategoryFromDTO(CategoryDTO categoryDTO, @MappingTarget Category category);

}
