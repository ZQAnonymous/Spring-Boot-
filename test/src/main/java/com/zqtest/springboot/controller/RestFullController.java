package com.zqtest.springboot.controller;

import com.zqtest.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestFullController {

    @Autowired
    private UserService userService;

    @RequestMapping("/boot/user/{id}")
    public Object user(@PathVariable("id")Integer id){
        return userService.getUser(id);
    }
}
