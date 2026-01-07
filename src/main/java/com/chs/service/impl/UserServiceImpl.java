package com.chs.service.impl;

import com.chs.dao.UserMapper;
import com.chs.domain.User;
import com.chs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String email, String password) {
        User user = userMapper.getUserByEmail(email);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User register(String nickname, String email, String password, String profile_picture) {
        User existingUser = userMapper.getUserByEmail(email);
        if (existingUser != null) {
            return null; // User already exists
        }

        User newUser = new User();
        newUser.setNickname(nickname);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setProfile_picture(profile_picture);

        userMapper.insertUser(newUser);
        return newUser;
    }

    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userMapper.getUserByEmail(email) != null;
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }
}

