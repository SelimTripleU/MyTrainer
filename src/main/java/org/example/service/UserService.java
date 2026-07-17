package org.example.service;

import org.example.dao.UserRepository;
import org.example.entity.User;

import java.time.LocalDate;

public class UserService {

    private UserRepository userRepository = new UserRepository();

    public User createUser(String name, LocalDate dateOfBirth, double height, String gender) {
        User user = new User();
        user.setName(name);
        user.setDateOfBirth(dateOfBirth);
        user.setHeight(height);
        user.setGender(gender);
        userRepository.save(user);
        return user;
    }

}
