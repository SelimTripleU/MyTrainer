package org.example.dao;

import org.example.entity.User;

public class UserRepository extends GenericDao<User> {

    public UserRepository() {
        super(User.class);
    }
}
