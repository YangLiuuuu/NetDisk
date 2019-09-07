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
            int fromId = loginUser.getUid();//好友关系一方id
            int toUid = user.getUid();//另一方id
            if (fromId>toUid){//始终是第一方id小
                int t = fromId;
                fromId = toUid;
                toUid = t;
            }
            Relation relation = relationMapper.selectByFromAndToUid(fromId,toUid);
            if (relation==null){
                friendViewObject.setIsFriend(false);
            }else{
                friendViewObject.setIsFriend(true);
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

    /**
     * 查询加好友请求
     * @return
     */
    @ResponseBody
    @RequestMapping("queryfriendrequest")
    public ServerResponse queryFriendRequest(HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        List<Message>messages = messageMapper.selectByUid(loginUser.getUid());
        List<MessageViewObject> messageViewObjects = new ArrayList<>();
        for (Message msg : messages){
            MessageViewObject mv = new MessageViewObject();
            User tempLoginUser = new User(loginUser);//复制一个对象,防止破坏request中对象
            tempLoginUser.setPasswords("");//去掉密码
            if (msg.getFromuid().equals(loginUser.getUid())){//自己是发送方
                mv.setFromUser(tempLoginUser);
                mv.setToUser(userMapper.selectByPrimaryKey(msg.getTouid()));
                mv.setIsSender(1);
            }else {
                mv.setFromUser(userMapper.selectByPrimaryKey(msg.getFromuid()));
                mv.setToUser(tempLoginUser);
                mv.setIsSender(0);
            }
            mv.setMessage(msg);
            messageViewObjects.add(mv);
        }
        return ServerResponse.createBySuccess(messageViewObjects);
    }

    //添加好友
    @ResponseBody
    @RequestMapping("addfriend")
    public ServerResponse addFriend(@Param("msgid")String msgid){
        Message message = messageMapper.selectByPrimaryKey(Integer.valueOf(msgid));
        message.setAccepted(1);
        message.setMsgid(Integer.valueOf(msgid));
        int count = messageMapper.updateByPrimaryKeySelective(message);
        int fromuid = message.getFromuid();
        int touid = message.getTouid();
        int t=0;
        if (fromuid>touid){//如果第一方id较大，交换
            t = fromuid;
            fromuid = touid;
            touid = t;
        }
        Relation relation = relationMapper.selectByFromAndToUid(fromuid,touid);
        if (relation==null){
            relation = new Relation();
            relation.setFromuid(fromuid);
            relation.setTouid(touid);
            relationMapper.insert(relation);
        }
        if (count>0) return ServerResponse.createBySuccess();
        else return ServerResponse.createByError();
    }

    /**
     * 查询好友请求
     * @return
     */
    @ResponseBody
    @RequestMapping("queryfriendlist")
    public ServerResponse queryFriendList(HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        List<Relation> relationList= relationMapper.selectFriends(loginUser.getUid());//找到所有好友用户id
        List<User>friendList = new ArrayList<>();
        //根据好友id查找好友信息
        for (Relation relation:relationList){
            int fromuid = relation.getFromuid();
            int toUid = relation.getTouid();
            User friend = userMapper.selectByPrimaryKey(loginUser.getUid()==fromuid ? toUid : fromuid);
            friend.setPasswords("");
            friendList.add(friend);
        }
        return ServerResponse.createBySuccess(friendList);
    }
}
