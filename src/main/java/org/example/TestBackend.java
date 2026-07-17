package org.example;

import org.example.service.UserService;

import java.time.LocalDate;

public class TestBackend {
    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.createUser("kevin", LocalDate.of(2002, 10, 22), 190, "male");
    }


}
