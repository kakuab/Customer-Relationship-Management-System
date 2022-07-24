package com.kakuab.service.impl;

import com.kakuab.mapper.UserMapper;
import com.kakuab.pojo.User;
import com.kakuab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User selectUserLoginActandLoginPwd(Map<String, Object> map) {
        return userMapper.selectUserLoginActandPwd(map);
    }

    @Override
    public List<User> selectUser() {
        return userMapper.selectUser();
    }
}
