package com.chs.service.impl;

import com.chs.dao.PageMapper;
import com.chs.domain.Meme;
import com.chs.domain.User;
import com.chs.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private PageMapper pageMapper;
    @Override
    public Meme getMeme(int id) {
        return pageMapper.getMeme(id);
    }
    @Override
    public User getUser(int user_Id) {
        return pageMapper.getUser(user_Id);
    }
    @Override
    public Boolean favorite(int user_Id,int meme_Id){
        return pageMapper.favorite(user_Id,meme_Id);
    }
    @Override
    public void insertFavorite(int user_Id,int meme_Id){
        pageMapper.insertFavorite(user_Id,meme_Id);
    }
    @Override
    public void deleteFavorite(int user_Id,int meme_Id){
        pageMapper.deleteFavorite(user_Id,meme_Id);
    }
    @Override
    public List<Integer> findSimilar(List<String> tagName){return pageMapper.findSimilar(tagName);}
    @Override
    public List<Integer> findSimilar2(List<String> nameList){return pageMapper.findSimilar2(nameList);}
    @Override
    public List<String> getTagName(int meme_Id){
        return pageMapper.getTagName(meme_Id);
    }
}
