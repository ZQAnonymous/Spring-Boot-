package com.example.service;

import com.example.domain.User;

/**
 * @Auther: zhao quan
 * @Date: 2018/7/11 14:21
 * @PACKAGE_NAME: com.example.service
 * @Description:
 */
public interface UserService {
    User findByName(String name);

    User findById(Integer id);
}
