package com.pjh.community.service;

import com.pjh.community.dao.UserMapper;
import com.pjh.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User selectById(int id){
        return userMapper.selectById(id);
    }
}
