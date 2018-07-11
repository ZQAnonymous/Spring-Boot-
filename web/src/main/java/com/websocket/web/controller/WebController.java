package com.websocket.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @Auther: zhao quan
 * @Date: 2018/7/9 11:07
 * @PACKAGE_NAME: com.websocket.web.controller
 * @Description:
 */
@Controller
public class WebController {

    private Logger logger = LoggerFactory.getLogger(WebController.class);

    @RequestMapping("/webSocket/{roomName}/{name}")
    public String toIndex(@PathVariable("roomName") String roomName,@PathVariable("name") String name , Model model){
        logger.info("跳转到websocket的页面上");
        model.addAttribute("username",name);
        model.addAttribute("roomName",roomName);
        return "index";
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
