package com.example.controller;

import com.example.service.UserService;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: zhao quan
 * @Date: 2018/7/11 09:34
 * @PACKAGE_NAME: com.example.controller
 * @Description:
 */
@Controller
public class TestController {

    @RequestMapping("/testThymeleaf")
    public String testThymeleaf(Model model) {
        model.addAttribute("name", "赵权");
        return "test";
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
    public String login(String name, String password, Model model) {
        /*
         * 使用shiro编写认证操作
         */
        //1. 获取subject
        Subject subject = SecurityUtils.getSubject();
        //2.封装用户数据
        System.out.println("name:"+name);
        System.out.println("password:"+password);
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);

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
        } finally {

        }
    }
    @RequestMapping("/unauthorized")
    public String unauthorized(){
        return "unauthorized";
    }
}
