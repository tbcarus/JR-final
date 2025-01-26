package ru.tbcarus.jrfinal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.tbcarus.jrfinal.exception.EntityAlreadyExistException;
import ru.tbcarus.jrfinal.exception.EntityNotFoundException;
import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.model.dto.*;
import ru.tbcarus.jrfinal.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRegisterMapper userRegisterMapper;
    private final JwtService jwtService;

    public User register(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByEmail(userRegisterDto.getEmail().toLowerCase())) {
            throw new EntityAlreadyExistException(userRegisterDto.getEmail(), String.format("User %s already exist", userRegisterDto.getEmail()));
        }
        User user = userRegisterMapper.toUser(userRegisterDto);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(username).get();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = getUserByEmail(loginRequest.getEmail());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new LoginResponse(accessToken, refreshToken);
    }

    public RefreshResponse refreshToken(String refreshToken) {
        User user = getUserByEmail(jwtService.extractUserName(refreshToken));
        return new RefreshResponse(jwtService.refreshAccessToken(user, refreshToken));
    }

    private User getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException(email, String.format("User %s does not exist", email));
        }
        return optionalUser.get();
    }
}
