package ru.tbcarus.jrfinal.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.model.dto.UserRegisterDto;
import ru.tbcarus.jrfinal.service.UserService;

@RestController
public class RegisterController {
    public static final String REGISTER_URL = "/api/user/register";
    public static final String LOGIN_URL = "/api/user/login";

    @Autowired
    private UserService userService;

    @PostMapping(REGISTER_URL)
    public ResponseEntity<Void> register(@RequestBody UserRegisterDto userRegisterDto) {
        User savedUser = userService.register(userRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @PostMapping(LOGIN_URL)
//    public ResponseEntity<Void> login(@RequestBody User user) {
//
//    }

}
