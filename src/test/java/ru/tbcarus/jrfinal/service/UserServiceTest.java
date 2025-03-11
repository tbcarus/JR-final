package ru.tbcarus.jrfinal.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.tbcarus.jrfinal.exception.EntityAlreadyExistException;
import ru.tbcarus.jrfinal.exception.EntityNotFoundException;
import ru.tbcarus.jrfinal.model.Role;
import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.model.dto.LoginResponse;
import ru.tbcarus.jrfinal.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tbcarus.jrfinal.UserTestData.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:.env.local-test")
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Test
    @Transactional
    public void registerSuccess() {
        User savedUser = userService.register(testUserRegisterDto);
        assertNotNull(savedUser);
        assertEquals(testUserRegisterDto.getEmail(), savedUser.getEmail());
        assertEquals(testUserRegisterDto.getName(), savedUser.getName());
        assertTrue(passwordEncoder.matches(testUserRegisterDto.getPassword(), savedUser.getPassword())); // Проверка шифрования пароля
        assertTrue(savedUser.getRoles().contains(Role.USER));

        User userFromDb = userRepository.findByEmailIgnoreCase(testUserRegisterDto.getEmail()).orElse(null);
        assertNotNull(userFromDb);
        assertEquals(testUserRegisterDto.getEmail(), userFromDb.getEmail());
    }

    @Test
    @Transactional
    public void registerExist() {
        userService.register(testUserRegisterDto);
        assertThrows(EntityAlreadyExistException.class, () -> {
            userService.register(testUserRegisterDto);
        });
    }

    @Test
    @Transactional
    public void loginSuccess() {
        User savedUser = userService.register(testUserRegisterDto);
        LoginResponse loginResponse = userService.login(testLoginRequest);
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getAccessToken());
        assertNotNull(loginResponse.getRefreshToken());

        assertTrue(jwtService.isTokenValid(loginResponse.getAccessToken(), savedUser));
    }

    @Test
    @Transactional
    public void loginFail() {
        assertThrows(EntityNotFoundException.class, () -> {
            userService.login(testLoginRequest);
        });

        User savedUser = userService.register(testUserRegisterDto);
        assertThrows(EntityNotFoundException.class, () -> {
            userService.login(testLoginRequestWrongPassword);
        });
    }

}