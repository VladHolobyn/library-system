package com.pnudev.librarysystem.mapper;

import com.pnudev.librarysystem.dto.CategoryDTO;
import com.pnudev.librarysystem.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryDTO categoryDTO);

    CategoryDTO toDTO(Category customer);

}
