package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.CategoryDTO;
import com.pnudev.librarysystem.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryDTO categoryDTO);

    CategoryDTO toDTO(Category customer);

    void updateCategoryFromDTO(CategoryDTO categoryDTO, @MappingTarget Category category);

}
