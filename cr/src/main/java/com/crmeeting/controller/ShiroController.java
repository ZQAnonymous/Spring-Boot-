package com.crmeeting.controller;

import com.crmeeting.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: zhao quan
 * @Date: 2018/7/12 21:19
 * @PACKAGE_NAME: com.crmeeting.controller
 * @Description:
 */
@Controller
public class ShiroController {

    @Autowired
    private UserService userService;

    @RequestMapping("/testThymeleaf")
    public String testThymeleaf(Model model) {
        model.addAttribute("name", "赵权");
        return "test";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/doRegister")
    public String doRegister(@RequestParam("username") String username,
                             @RequestParam("password") String password) {
        boolean result = userService.insertUser(username,password);
        if(result){
            return "/login";
        }
        return "register";
    }

    @RequestMapping("/add")
    public String add() {
        return "user/add";
    }

    @RequestMapping("/update")
    public String update() {
        return "user/update";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model) {

        /*
         * 使用shiro编写认证操作
         */
        //1. 获取subject
        Subject subject = SecurityUtils.getSubject();
        //2.封装用户数据
        System.out.println("name:"+username);
        System.out.println("password:"+password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        //3.执行登录方法
        try {
            subject.login(token);
            return "redirect:/testThymeleaf";
        } catch (UnknownAccountException e) {
            //登录失败，用户名不存在
            model.addAttribute("msg", "用户名不存在");
            return "login";
        } catch (IncorrectCredentialsException e) {
            //登录失败，密码错误
            model.addAttribute("msg", "密码错误");
            return "login";
        }
    }
    @RequestMapping("/unauthorized")
    public String unauthorized(){
        return "unauthorized";
    }
}
