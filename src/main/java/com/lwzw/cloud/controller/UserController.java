package com.lwzw.cloud.controller;

import com.lwzw.cloud.bean.User;
import com.lwzw.cloud.constant.ServerResponse;
import com.lwzw.cloud.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    UserMapper userMapper;

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ServerResponse login(@RequestParam("username")String username,
                                @RequestParam("password")String password,
                                HttpServletRequest request){
        List<User> users = userMapper.selectByUsername(username);
        if(users!=null&&users.size()!=0){
            if(users.get(0).getPasswords().equals(password)){
                request.getSession().setAttribute("loginUser",users.get(0));
                return ServerResponse.createBySuccess();
            }
        }
        return ServerResponse.createByError();
    }

    @ResponseBody
    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    public ServerResponse signUp(@RequestParam("username")String username,
                                 @RequestParam("password")String password,
                                 @RequestParam("nickname")String nickname){
        if (!validateInformation(username,password,nickname)){
            return ServerResponse.createByErrorCodeMessage(10,"注册信息非法");
        }
        if (userMapper.selectByUsername(username).size()>0){
            return ServerResponse.createByErrorCodeMessage(11,"账号已被注册");
        }
        User user = new User();
        user.setNickname(nickname);
        user.setUsername(username);
        user.setPasswords(password);
        user.setScore(0);
        user.setCapacity(500d);
        userMapper.insert(user);
        return ServerResponse.createBySuccess();
    }

    @ResponseBody
    @RequestMapping(value = "isUsernameValid")
    public ServerResponse isUsernameRegistered(@RequestParam("username")String username){
        List<User> users = userMapper.selectByUsername(username);
        if (users==null||users.size()>0){
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess();
    }

    private boolean validateInformation(String username, String password, String nickname) {
        if (username.length()<=8||username.length()>=12){
            return false;
        }
        if (password.length()<=5 || password.length()>=12){
            return false;
        }
        if (nickname.length()<=0||nickname.length()>=10){
            return false;
        }
        return true;
    }
}
