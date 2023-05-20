package com.pnudev.librarysystem.dto.error;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorDTO {
    private List<String> errors;

    public ErrorDTO(List<String> errors) {
        this.errors = errors;
    }

    public ErrorDTO(String error) {
        this.errors = List.of(error);
    }
}
