package com.chs.service;

import com.chs.domain.Meme;

import java.util.List;

public interface MainService {
    List<Meme> getMemes(int page, int pageSize, String sortBy, String query, String searchType);
    int getMemeNumber(String query, String searchType);
    void incrementViews(int id);
    Meme getMemeById(int id);
    void addTagToMeme(int memeId, String tagName);
    int addMeme(Meme meme);
    List<String> getSuggestions(String query);
    List<String> getSearchSuggestions(String query, String type);
}