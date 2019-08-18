package com.lwzw.cloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/relation")
public class RelationController {

    @RequestMapping("friend")
    public String toFriendPage(){
        return "/pages/friendmanage";
    }
}
