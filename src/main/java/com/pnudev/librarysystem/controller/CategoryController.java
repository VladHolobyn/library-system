package com.pnudev.librarysystem.controller;

import com.pnudev.librarysystem.dto.CategoryDTO;
import com.pnudev.librarysystem.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;


    @GetMapping
    public Page<CategoryDTO> searchCategoryByName(
            @RequestParam("name") String name,
            @RequestParam(value = "page",required = false) Integer pageNumber,
            @RequestParam(value = "size", required = false) Integer size
    ){
        return service.searchCategoryByName(
                name,
                Optional.ofNullable(pageNumber).filter(p -> p > 0).orElse(0),
                Optional.ofNullable(size).filter(s -> s > 0).orElse(5)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO addCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return service.addCategory(categoryDTO);
    }

    @PutMapping
    public CategoryDTO updateCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return service.updateCategory(categoryDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id){
        service.deleteCategory(id);
    }

}
