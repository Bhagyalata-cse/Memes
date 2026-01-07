package com.chs.service.impl;

import com.chs.dao.FollowMapper;
import com.chs.domain.Following;
import com.chs.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
@Autowired
private FollowMapper followMapper;
    @Override
    public List<Following> getFollowee(int id) {
        return followMapper.getFollowee(id);
    }

    @Override
    public int getFolloweeNum(int id) {
        return followMapper.getFolloweeNum(id);
    }

    @Override
    public List<Following> getFollower(int id) {
        return followMapper.getFollower(id);
    }

    @Override
    public int getFollowerNum(int id) {
        return followMapper.getFollowerNum(id);
    }
    // 关注用户
    @Override
    public void followUser(int followerId, int followeeId) {
        followMapper.followUser(followerId, followeeId);
    }

    // 取消关注
    @Override
    public void unfollowUser(int followerId, int followeeId) {
        followMapper.unfollowUser(followerId, followeeId);
    }

    // 判断是否已经关注
    @Override
    public boolean isFollowing(int followerId, int followeeId) {
        int count = followMapper.isFollowing(followerId, followeeId);
        System.out.println("isFollowing查询结果：" + count);
        return count > 0;
    }


}
