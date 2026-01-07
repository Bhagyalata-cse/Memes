package com.chs.service;

import com.chs.domain.Following;

import java.util.List;

public interface FollowService {
    List<Following> getFollowee(int id);//用户的关注
    int getFolloweeNum(int id);
    List<Following> getFollower(int id);//用户的粉丝
    int getFollowerNum(int id);
    public void followUser(int followerId, int followeeId);
    public void unfollowUser(int followerId, int followeeId);
    public boolean isFollowing(int followerId, int followeeId);
}
