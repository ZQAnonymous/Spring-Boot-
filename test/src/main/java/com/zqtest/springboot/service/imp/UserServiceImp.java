package com.zqtest.springboot.service.imp;

import com.zqtest.springboot.mapper.UserMapper;
import com.zqtest.springboot.pojo.User;
import com.zqtest.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAllUser() {
        List<User> userList = (List<User>) redisTemplate.opsForValue().get("users");
        if (null == userList){
            synchronized (this){
                userList = (List<User>) redisTemplate.opsForValue().get("users");
                if (null == userList){
                    userList = userMapper.selectAllUser();
                    redisTemplate.opsForValue().set("users",userList);
                    System.out.println("mysql 赋值");
                }
            }
        }else{
            System.out.println("redis 赋值");
        }
        return userList;
    }

    @Transactional
    @Override
    public int update() {
        User user = new User();
        user.setId(1);
        user.setUsername("zq");
        user.setPassword("1234556");
        int update = userMapper.updateByPrimaryKeySelective(user);
        System.out.println("更新结果："+update);
//        int a = 10/0;
        return update;
    }

    @Override
    public User getUser(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
