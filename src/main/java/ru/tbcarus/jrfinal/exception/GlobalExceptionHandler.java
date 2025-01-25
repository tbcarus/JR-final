package ru.tbcarus.jrfinal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenRevokedException.class)
    public ResponseEntity<ErrorResponse> handleTokenRevokedException(TokenRevokedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ErrorResponse.builder()
                                .uuid(UUID.randomUUID())
                                .message(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExist(EntityAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .uuid(UUID.randomUUID())
                                .message(String.format("Username %s already exists", e.getEntityName()))
                                .build()
                );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .uuid(UUID.randomUUID())
                                .message(String.format("Entity %s not found", e.getEntityName()))
                                .build()
                );
    }
}
