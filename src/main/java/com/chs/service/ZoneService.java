package com.chs.service;

import com.chs.domain.Update;
import com.chs.domain.User;

import java.util.List;

public interface ZoneService {
    List<User> getFollowee(int user_Id);
    List<Update> getAllInfo(int follower);
    List<Update> getInfoById(int user_Id);
}
