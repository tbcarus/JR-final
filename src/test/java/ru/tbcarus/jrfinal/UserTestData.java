package ru.tbcarus.jrfinal;

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


}
