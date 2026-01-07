package com.chs.service.impl;

import com.chs.dao.PageMapper;
import com.chs.dao.ZoneMapper;
import com.chs.domain.Update;
import com.chs.domain.User;
import com.chs.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ZoneServiceImpl implements ZoneService {
    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private ZoneMapper zoneMapper;
    @Override
    public List<User> getFollowee(int user_Id){
        List<Integer> ids=zoneMapper.getFollowee(user_Id);
        List<User> users=new ArrayList<>();
        for(Integer id:ids){
            users.add(pageMapper.getUser(id));
        }
        return users;
    }
    @Override
    public List<Update> getAllInfo(int follower){
        return zoneMapper.getAllInfo(follower);
    }
    @Override
    public List<Update> getInfoById(int user_Id){
        return zoneMapper.getInfoById(user_Id);
    }
}
