package com.chs.domain;

import lombok.Data;

@Data
public class User {
    private int Id;
    private String nickname;
    private String email;
    private String password;
    private String profile_picture;
}

