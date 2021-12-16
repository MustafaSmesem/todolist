package com.comodo.todolistspring.service;

import com.comodo.todolistspring.document.User;
import com.comodo.todolistspring.exception.BadRequestException;
import com.comodo.todolistspring.exception.DocumentNotFoundException;
import com.comodo.todolistspring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            var existsByUsername = userRepository.existsByUsername(user.getUsername());
            if (existsByUsername)
                throw new BadRequestException(String.format("this email [%s] is already registered", user.getUsername()));
        }
        return userRepository.save(user);
    }

    public User getUserById(String id) {
        var userOptional = userRepository.findUserById(id);
        if (userOptional.isEmpty())
            throw new DocumentNotFoundException("User", id);
        return userOptional.get();
    }

    public List<User> getAll() {
        return userRepository.findByEnabledTrue().stream().peek(user -> user.setPassword("")).toList();
    }

    public User getUserByUserName(String userName) {
        var userOptional = userRepository.findUserByUsername(userName);
        if (userOptional.isEmpty())
            throw new DocumentNotFoundException("User", userName);
        return userOptional.get();
    }

}
