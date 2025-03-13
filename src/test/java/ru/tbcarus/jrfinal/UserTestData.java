package ru.tbcarus.jrfinal;

import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.model.dto.LoginRequest;
import ru.tbcarus.jrfinal.model.dto.UserRegisterDto;

public class UserTestData {
    public static UserRegisterDto invalidUserRegisterDto = UserRegisterDto.builder()
            .email("invalid-email")
            .password("pas")
            .name("user")
            .build();

    public static UserRegisterDto testUserRegisterDto = UserRegisterDto.builder()
            .email("qqq@ww.ee")
            .password("pass")
            .name("testUser")
            .build();

    public static LoginRequest testLoginRequest = LoginRequest.builder()
            .email(testUserRegisterDto.getEmail())
            .password(testUserRegisterDto.getPassword())
            .build();

    public static LoginRequest testLoginRequestWrongPassword = LoginRequest.builder()
            .email(testUserRegisterDto.getEmail())
            .password(testUserRegisterDto.getPassword() + "wrong")
            .build();

    public static User user1 = User.builder()
            .email("qqq@ww.ee")
            .password("pass")
            .name("User1")
            .build();
    public static User user2 = User.builder()
            .email("www@ww.ee")
            .password("pass")
            .name("User2")
            .build();


}
