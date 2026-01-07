package com.chs.dao;

import com.chs.domain.Favorite;

import java.util.List;

public interface FavoriteMapper {
List<Favorite> getFmemeByUid(Integer userId);
}
