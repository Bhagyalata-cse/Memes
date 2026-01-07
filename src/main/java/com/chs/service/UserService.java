package com.chs.service;

import com.chs.domain.User;

public interface UserService {
    User login(String email, String password);
    User register(String nickname, String email, String password, String profile_picture);
    User getUserById(int id);
    boolean isEmailExists(String email);
    void updateUser(User user);
}