package ru.tbcarus.jrfinal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tbcarus.jrfinal.exception.EntityAlreadyExist;
import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.model.dto.UserRegisterDto;
import ru.tbcarus.jrfinal.model.dto.UserRegisterMapper;
import ru.tbcarus.jrfinal.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserRegisterMapper userRegisterMapper;

    public User register(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new EntityAlreadyExist(userRegisterDto.getEmail(), "Entity already exist");
        }
        User user = userRegisterMapper.toUser(userRegisterDto);
        return userRepository.save(user);
    }
}
