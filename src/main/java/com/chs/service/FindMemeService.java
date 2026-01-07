package com.chs.service;

import com.chs.domain.Meme;

import java.util.List;

public interface FindMemeService {
    Meme getMemeById(Integer id);
    List<Meme> getMemeByUid(Integer userId);
}
