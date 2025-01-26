package ru.tbcarus.jrfinal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.model.dto.*;
import ru.tbcarus.jrfinal.service.UserService;

@RestController
@RequiredArgsConstructor
public class RegisterController {
    public static final String USER_URL = "/api/user";
    public static final String REGISTER_URL = "/api/user/register";
    public static final String LOGIN_URL = "/api/user/login";
    public static final String REFRESH_TOKEN_URL = "/api/user/refresh";

    private final UserService userService;

    @PostMapping(REGISTER_URL)
    public ResponseEntity<Void> register(@RequestBody UserRegisterDto userRegisterDto) {
        User savedUser = userService.register(userRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(LOGIN_URL)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(loginRequest));
    }

    @PostMapping(REFRESH_TOKEN_URL)
    public ResponseEntity<RefreshResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.refreshToken(refreshRequest.getRefreshToken()));
    }

    @GetMapping(USER_URL+"/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.status(HttpStatus.OK).body("All good!");
    }

}
