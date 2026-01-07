package com.chs.dao;

import com.chs.domain.Update;

import java.util.List;

public interface ZoneMapper {
    List<Integer> getFollowee(int user_Id);
    List<Update> getAllInfo(int follower);
    List<Update> getInfoById(int user_Id);
}
