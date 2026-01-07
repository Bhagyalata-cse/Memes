package com.chs.domain;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class Following {
    private Integer followee;   // 被关注者ID
    private Integer follower;   // 关注者ID
    private Timestamp time;      // 关注时间

}
