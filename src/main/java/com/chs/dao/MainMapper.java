package com.chs.dao;

import com.chs.domain.Meme;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MainMapper {
    List<Meme> getMemes(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("sortBy") String sortBy, @Param("query") String query, @Param("searchType") String searchType);
    int getMemeNumber(@Param("query") String query, @Param("searchType") String searchType);
    void incrementViews(int id);
    Meme getMemeById(int id);
    String getTagByName(String tagName);
    void insertTag(String tagName);
    void addTagToMeme(@Param("memeId") int memeId, @Param("tagName") String tagName);
    void insertMeme(Meme meme);
    List<String> getSuggestions(@Param("query") String query, @Param("limit") int limit);
    List<String> getSearchSuggestions(@Param("query") String query, @Param("type") String type);
}