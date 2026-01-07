package com.chs.domain;

public class Update {
    private int meme_Id;
    private int user_Id;
    private String file;
    private String upload_time;
    private String nickname;
    private String profile_picture;

    public int getMeme_Id() {
        return meme_Id;
    }

    public void setMeme_Id(int meme_Id) {
        this.meme_Id = meme_Id;
    }

    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
