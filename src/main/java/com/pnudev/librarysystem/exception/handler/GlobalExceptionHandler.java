package com.pnudev.librarysystem.exception.handler;

import com.pnudev.librarysystem.dto.error.ErrorDTO;
import com.pnudev.librarysystem.exception.OperationFailedException;
import com.pnudev.librarysystem.exception.EmptyFileException;
import com.pnudev.librarysystem.exception.FileWrongTypeException;
import com.pnudev.librarysystem.exception.IOErrorInFileException;
import com.pnudev.librarysystem.exception.NotUniqueException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ErrorDTO handleValidationError(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();

        return new ErrorDTO(errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleEntityNotFoundException(EntityNotFoundException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler({
            OperationFailedException.class,
            NotUniqueException.class,
            BadCredentialsException.class,
            EmptyFileException.class,
            FileWrongTypeException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequestExceptions(RuntimeException e) {
        return new ErrorDTO(e.getMessage());
    }

    @ExceptionHandler(IOErrorInFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleIOErrorInFileException(IOErrorInFileException e) {
        return new ErrorDTO(e.getMessage());
    }
}
