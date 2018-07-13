package com.crmeeting.service;

import com.crmeeting.entity.User;

import java.util.List;

/**
 * @Auther: zhao quan
 * @Date: 2018/7/12 14:39
 * @PACKAGE_NAME: com.crmeeting.service
 * @Description:
 */
public interface UserService {
    List<User> findAllUser();

    User findUserById(Integer id);

    User findByName(String username);

    boolean insertUser(String username, String password);
}
