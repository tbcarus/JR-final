package ru.tbcarus.jrfinal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExist(EntityAlreadyExist e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .uuid(UUID.randomUUID())
                                .message(String.format("Username %s already exists", e.getEntityName()))
                                .build()
                );
    }
}
