package com.chs.dao;

import com.chs.domain.Following;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowMapper {
    List<Following> getFollowee(int id);//用户的关注
    int getFolloweeNum(int id);
    List<Following> getFollower(int id);//用户的粉丝
    int getFollowerNum(int id);
    // 添加关注关系
    void followUser(@Param("followerId") Integer followerId, @Param("followeeId") Integer followeeId);

    // 取消关注
    void unfollowUser(@Param("followerId") Integer followerId, @Param("followeeId") Integer followeeId);

    // 检查是否已关注
    int isFollowing(@Param("followerId") Integer followerId, @Param("followeeId") Integer followeeId);
}
