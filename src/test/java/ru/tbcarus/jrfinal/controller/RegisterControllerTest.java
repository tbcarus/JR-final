package ru.tbcarus.jrfinal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.tbcarus.jrfinal.UserTestData;
import ru.tbcarus.jrfinal.exception.EntityAlreadyExistException;
import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.model.dto.LoginRequest;
import ru.tbcarus.jrfinal.model.dto.RefreshRequest;
import ru.tbcarus.jrfinal.model.dto.RefreshResponse;
import ru.tbcarus.jrfinal.model.dto.UserRegisterDto;
import ru.tbcarus.jrfinal.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:.env.local-test")
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    public void testRegisterSuccess() throws Exception {
        when(userService.register(any(UserRegisterDto.class))).thenReturn(new User());

        ResultActions perform = mockMvc.perform(post(RegisterController.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserTestData.testUserRegisterDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testRegisterBadRequest() throws Exception {
        mockMvc.perform(post(RegisterController.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserTestData.invalidUserRegisterDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterUserAlreadyExists() throws Exception {
        UserRegisterDto urd = UserTestData.invalidUserRegisterDto;

        when(userService.register(any(UserRegisterDto.class))).thenThrow(new EntityAlreadyExistException(urd.getEmail(), "User already exist"));

        mockMvc.perform(post(RegisterController.REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(urd)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void login() throws Exception {
        LoginRequest loginRequest = new LoginRequest("qqq@ww.ee", "pass");
        mockMvc.perform(post(RegisterController.LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void refresh() throws Exception {
        when(userService.refreshToken(any(String.class))).thenReturn(new RefreshResponse());

        mockMvc.perform(post(RegisterController.REFRESH_TOKEN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshRequest("token"))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testAuthenticated() throws Exception {
        mockMvc.perform(get(RegisterController.USER_URL + "/test"))
                .andExpect(status().isOk());
    }

    @Test
    void testUnAuthenticated() throws Exception {
        mockMvc.perform(get(RegisterController.USER_URL + "/test"))
                .andExpect(status().isForbidden());
    }

}