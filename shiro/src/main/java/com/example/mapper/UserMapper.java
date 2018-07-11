package com.example.mapper;

import com.example.domain.User;

/**
 * @Auther: zhao quan
 * @Date: 2018/7/11 11:48
 * @PACKAGE_NAME: com.example.mapper
 * @Description:
 */
public interface UserMapper {

    User findByName(String name);

    User findById(int id);
}
