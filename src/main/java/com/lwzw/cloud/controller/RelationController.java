package com.lwzw.cloud.controller;

import com.lwzw.cloud.bean.Message;
import com.lwzw.cloud.bean.Relation;
import com.lwzw.cloud.bean.User;
import com.lwzw.cloud.bean.viewObject.FriendViewObject;
import com.lwzw.cloud.bean.viewObject.MessageViewObject;
import com.lwzw.cloud.constant.ServerResponse;
import com.lwzw.cloud.dao.MessageMapper;
import com.lwzw.cloud.dao.RelationMapper;
import com.lwzw.cloud.dao.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/relation")
public class RelationController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RelationMapper relationMapper;

    @Autowired
    MessageMapper messageMapper;

    @RequestMapping("friend")
    public String toFriendPage(){
        return "/pages/friendmanage";
    }

    //查找好友
    @ResponseBody
    @RequestMapping("queryfriend")
    public ServerResponse queryFriend(@RequestParam("account")String account, HttpServletRequest request){
        List<User> users = userMapper.selectByUsername(account);
        FriendViewObject friendViewObject = new FriendViewObject();
        if (users!=null&&users.size()>0){
            User user = users.get(0);//user为查找到的用户
            user.setPasswords("");
            friendViewObject.setUser(user);
            friendViewObject.setIsFriend(false);

            //接下来判断查找到的用户是否已经是好友
            User loginUser = (User) request.getSession().getAttribute("loginUser");//loginUser为当前用户
            Integer fromId = loginUser.getUid();//好友关系一方id
            Integer toUid = user.getUid();//另一方id
            if (fromId>toUid){//始终是第一方id小
                Integer t = fromId;
                fromId = toUid;
                toUid = t;
            }
            List<Relation> relations = relationMapper.selectByFromId(fromId);
            for (Relation relation:relations){
                if (relation.getTouid().equals(toUid)){
                    friendViewObject.setIsFriend(true);//已经是好友
                    break;
                }
            }
            return ServerResponse.createBySuccess(friendViewObject);
        }else{
            return ServerResponse.createByErrorMessage("没有查找到用户");
        }
    }

    /**
     * 发加好友请求
     * @param username 对方用户名
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/addfriendrequest")
    public ServerResponse addFriendRequest(@RequestParam("account")String username,HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        User toUser = userMapper.selectByUsername(username).get(0);
        if (loginUser.getUid().equals(toUser.getUid())){
            return ServerResponse.createByErrorMessage("不准添加自己为好友");
        }
        Message message = new Message();
        message.setFromuid(loginUser.getUid());
        message.setTouid(toUser.getUid());
        message.setAccepted(0);
        if (messageMapper.selectByFromAndToUid(loginUser.getUid(),toUser.getUid()).size()<=0){
            messageMapper.insert(message);
        }
        return ServerResponse.createBySuccess();
    }

    /** todo修改查询加好友逻辑
     * 查询加好友请求
     * @return
     */
    @ResponseBody
    @RequestMapping("queryfriendrequest")
    public ServerResponse queryFriendRequest(HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        List<Message>messages = messageMapper.selectByFromUid(loginUser.getUid());
        List<MessageViewObject> messageViewObjects = new ArrayList<>();
        for (Message msg : messages){
            User user1 = userMapper.selectByPrimaryKey(msg.getTouid());
            User user2 = loginUser;
            user2.setPasswords("");//去掉密码
            user1.setPasswords("");
            MessageViewObject mv = new MessageViewObject();
            mv.setMessage(msg);

            if (msg.getFromuid().equals(loginUser.getUid())){
                mv.setFromUser(user2);
                mv.setToUser(user1);
                mv.setIsSender(1);
            }else {
                mv.setIsSender(0);
                mv.setFromUser(user1);
                mv.setToUser(user2);
            }
            messageViewObjects.add(mv);
        }
        return ServerResponse.createBySuccess(messageViewObjects);
    }

    @ResponseBody
    @RequestMapping("addfriend")
    public ServerResponse addFriend(@Param("msgid")String msgid){
        Message message = new Message();
        message.setAccepted(1);
        message.setMsgid(Integer.valueOf(msgid));
        int count = messageMapper.updateByPrimaryKey(message);
        if (count>0) return ServerResponse.createBySuccess();
        else return ServerResponse.createByError();
    }
}
