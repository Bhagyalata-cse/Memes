package com.chs.dao;

import com.chs.domain.User;

public interface UserMapper {
    User getUserByEmail(String email);
    void insertUser(User user);
    User getUserById(int id);
    void updateUser(User user);
}