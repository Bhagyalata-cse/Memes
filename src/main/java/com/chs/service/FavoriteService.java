package com.chs.service;

import com.chs.domain.Favorite;

import java.util.List;

public interface FavoriteService {
    List<Favorite> getFmemeByUid(Integer userId);
}
