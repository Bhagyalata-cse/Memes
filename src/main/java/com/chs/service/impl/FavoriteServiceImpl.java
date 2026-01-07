package com.chs.service.impl;

import com.chs.dao.FavoriteMapper;
import com.chs.domain.Favorite;
import com.chs.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
@Autowired
    private FavoriteMapper favoriteMapper;
@Override
public List<Favorite> getFmemeByUid(Integer userId) {return favoriteMapper.getFmemeByUid(userId);}


}
