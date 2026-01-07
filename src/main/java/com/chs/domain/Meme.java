package com.chs.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Meme {
    private int Id;
    private String name;
    private String file;
    private String introduction;
    private Timestamp upload_time;
    private Integer user_Id;
    private Integer views;
}
