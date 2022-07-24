package com.kakuab.service;

import com.kakuab.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User selectUserLoginActandLoginPwd(Map<String,Object> map);
    List<User> selectUser();
}
