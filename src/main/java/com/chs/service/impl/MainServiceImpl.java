package com.chs.service.impl;

import com.chs.dao.MainMapper;
import com.chs.domain.Meme;
import com.chs.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainServiceImpl implements MainService {

    @Autowired
    private MainMapper mainMapper;

    @Override
    public List<Meme> getMemes(int page, int pageSize, String sortBy, String query, String searchType) {
        int offset = (page - 1) * pageSize;
        return mainMapper.getMemes(offset, pageSize, sortBy, query, searchType);
    }

    @Override
    public int getMemeNumber(String query, String searchType) {
        return mainMapper.getMemeNumber(query, searchType);
    }

    @Override
    @Transactional
    public void incrementViews(int id) {
        mainMapper.incrementViews(id);
    }

    @Override
    public Meme getMemeById(int id) {
        return mainMapper.getMemeById(id);
    }

    @Override
    @Transactional
    public void addTagToMeme(int memeId, String tagName) {
        // Check if the tag exists, if not, create it
        if (mainMapper.getTagByName(tagName) == null) {
            mainMapper.insertTag(tagName);
        }
        // Add the tag to the meme
        mainMapper.addTagToMeme(memeId, tagName);
    }

    @Override
    @Transactional
    public int addMeme(Meme meme) {
        mainMapper.insertMeme(meme);
        return meme.getId();
    }

    @Override
    public List<String> getSuggestions(String query) {
        return mainMapper.getSuggestions(query + "%", 5); // Limit to 5 suggestions
    }

    @Override
    public List<String> getSearchSuggestions(String query, String type) {
        return mainMapper.getSearchSuggestions(query,type);
    }
}