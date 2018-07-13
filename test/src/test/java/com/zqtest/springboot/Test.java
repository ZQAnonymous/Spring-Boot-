package com.zqtest.springboot;

import com.zqtest.springboot.pojo.User;
import com.zqtest.springboot.service.imp.UserServiceImp;

import java.util.List;

/**
 * @Auther: zhao quan
 * @Date: 2018/7/12 13:57
 * @PACKAGE_NAME: com.zqtest.springboot
 * @Description:
 */
public class Test {

    @org.junit.Test
    public void test(){
        UserServiceImp userServiceImp = new UserServiceImp();
        List<User> allUser = userServiceImp.getAllUser();
        for (User user: allUser){
            System.out.println(user.getId());
        }

    }

}
