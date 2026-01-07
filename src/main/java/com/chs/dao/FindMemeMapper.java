package com.chs.dao;

import com.chs.domain.Meme;

import java.util.List;

public interface FindMemeMapper {
    // 根据 memeId 查询表情包信息
    Meme getMemeById(int id);

    // 根据 userId 查询该用户上传的所有表情包
    List<Meme> getMemeByUid(int userId);
}
