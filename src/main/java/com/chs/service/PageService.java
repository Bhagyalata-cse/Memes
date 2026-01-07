package com.chs.service;

import com.chs.domain.Meme;
import com.chs.domain.User;

import java.util.List;

public interface PageService {
    Meme getMeme(int id);
    User getUser(int user_Id);
    Boolean favorite(int user_Id,int meme_Id);
    void insertFavorite(int user_Id,int meme_Id);
    void deleteFavorite(int user_Id,int meme_Id);
    List<Integer> findSimilar(List<String> tagName);
    List<Integer> findSimilar2(List<String> nameList);
    List<String> getTagName(int meme_Id);
}
