package com.zqtest.springboot.controller;

import com.zqtest.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyBatisController {

    @Autowired
    private UserService userService;

    @GetMapping("/boot/users")
    public Object users(){
        return userService.getAllUser();
    }
    @GetMapping("/boot/update")
    public Object update(){
        return userService.update();
    }
}
