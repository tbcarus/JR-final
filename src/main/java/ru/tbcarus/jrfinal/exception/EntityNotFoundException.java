package ru.tbcarus.jrfinal.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EntityNotFoundException extends RuntimeException {
    private final String entityName;
    private final String message;
}
