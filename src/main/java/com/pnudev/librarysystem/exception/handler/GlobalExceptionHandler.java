package com.pnudev.librarysystem.exception.handler;

import com.pnudev.librarysystem.dto.ErrorDTO;
import com.pnudev.librarysystem.exception.DeleteFailedException;
import com.pnudev.librarysystem.exception.EmptyFileException;
import com.pnudev.librarysystem.exception.FileWrongTypeException;
import com.pnudev.librarysystem.exception.IOErrorInFileException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleValidationError(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();

        return new ErrorDTO(errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleEntityNotFoundException(EntityNotFoundException ex){
        return new ErrorDTO(List.of(ex.getMessage()));
    }

    @ExceptionHandler(DeleteFailedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleDeleteError(DeleteFailedException ex){
        return new ErrorDTO(List.of(ex.getMessage()));
    }

    @ExceptionHandler(EmptyFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleEmptyFileException(EmptyFileException e) {
        return new ErrorDTO(List.of(e.getMessage()));
    }

    @ExceptionHandler(FileWrongTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleFileWrongTypeException(FileWrongTypeException e) {
        return new ErrorDTO(List.of(e.getMessage()));
    }

    @ExceptionHandler(IOErrorInFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleIOErrorInFileException(IOErrorInFileException e) {
        return new ErrorDTO(List.of(e.getMessage()));
    }

}
