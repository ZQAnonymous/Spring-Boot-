package com.zqtest.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class JSPController {


    /**
     *
     * 功能描述: 
     *
     * @Param:
     * @Return: 
     * @Auther: zhaoquan
     * @Date: 2018/7/5 9:01
     */
    @GetMapping("/boot/index")
    public String index(){
        return "index";
    }
}
