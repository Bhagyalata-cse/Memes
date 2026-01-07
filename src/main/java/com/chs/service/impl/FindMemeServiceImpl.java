package com.chs.service.impl;

import com.chs.dao.FindMemeMapper;
import com.chs.domain.Meme;
import com.chs.service.FindMemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindMemeServiceImpl implements FindMemeService {
    @Autowired
    private FindMemeMapper findMemeMapper;

    // 通过 memeId 查询表情包
    @Override
    public Meme getMemeById(Integer id) {
        return findMemeMapper.getMemeById(id);
    }

    // 根据 userId 查询该用户上传的所有表情包
    @Override
    public List<Meme> getMemeByUid(Integer userId) {
        return findMemeMapper.getMemeByUid(userId);
    }
}
