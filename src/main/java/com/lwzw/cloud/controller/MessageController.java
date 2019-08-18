package com.lwzw.cloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/message")
public class MessageController {

    @RequestMapping("msgpage")
    public String toMessagePage(){
        return "/pages/message";
    }
}
