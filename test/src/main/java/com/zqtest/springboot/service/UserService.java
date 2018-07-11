package com.zqtest.springboot.service;

import com.zqtest.springboot.pojo.User;

import java.util.List;

public interface UserService {

    List<User> getAllUser();

    int update();

    User getUser(Integer id);
}
