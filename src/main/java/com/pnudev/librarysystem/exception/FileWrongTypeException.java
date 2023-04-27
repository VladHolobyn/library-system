package com.pnudev.librarysystem.exception;

import java.util.List;

public class FileWrongTypeException extends RuntimeException{
    public FileWrongTypeException(List<String> allowedTypes){
        super("Wrong file type: only %s are allowed".formatted(String.join(", ", allowedTypes)));
    }
}
