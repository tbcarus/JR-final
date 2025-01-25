package ru.tbcarus.jrfinal.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenRevokedException extends RuntimeException {
    private final String token;
    private final String message;
}
