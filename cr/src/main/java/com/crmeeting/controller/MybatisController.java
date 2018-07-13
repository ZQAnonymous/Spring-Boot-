package com.crmeeting.controller;

import com.crmeeting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: zhao quan
 * @Date: 2018/7/12 14:37
 * @PACKAGE_NAME: com.crmeeting.controller
 * @Description:
 */
@Controller
public class MybatisController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    @ResponseBody
    public Object index(){
        return userService.findAllUser();
    }

    @RequestMapping("/findUser/{id}")
    @ResponseBody
    public Object index(@PathVariable("id") Integer id){
        return userService.findUserById(id);
    }
}
