package com.crmeeting.service.imp;

import com.crmeeting.entity.User;
import com.crmeeting.mapper.UserMapper;
import com.crmeeting.service.UserService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @Auther: zhao quan
 * @Date: 2018/7/12 14:39
 * @PACKAGE_NAME: com.crmeeting.service.imp
 * @Description:
 */
@Service
public class UserServiceImp implements UserService{

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAllUser() {
        List<User> userList = (List<User>) redisTemplate.opsForValue().get("users");
        if (null == userList){
            synchronized (this){
                userList = (List<User>) redisTemplate.opsForValue().get("users");
                if (null == userList){
                    userList = userMapper.selectAll();
                    redisTemplate.opsForValue().set("users",userList);
                    System.out.println("mysql 赋值");
                }
            }
        }else{
            System.out.println("redis 赋值");
        }
        return userList;
    }

    @Override
    public User findUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User findByName(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public boolean insertUser(String username, String password) {
        // 将用户名作为盐值
        ByteSource salt = ByteSource.Util.bytes(username);
        /*
        * MD5加密：
        * 使用SimpleHash类对原始密码进行加密。
        * 第一个参数代表使用MD5方式加密
        * 第二个参数为原始密码
        * 第三个参数为盐值，即用户名
        * 第四个参数为加密次数
        * 最后用toHex()方法将加密后的密码转成String
        * */
        String newPs = new SimpleHash("MD5", password, salt, 1024).toHex();

        User user = new User();
        user.setId(7);
        user.setUsername(username);
        user.setPassword(newPs);
        user.setPerms("user:add");

        if(null == findByName(username)){
            userMapper.insert(user);
            return true;
        }

        return false;
    }


}
