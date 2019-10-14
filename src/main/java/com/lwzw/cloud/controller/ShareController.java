package com.lwzw.cloud.controller;

import com.lwzw.cloud.bean.Share;
import com.lwzw.cloud.bean.UFile;
import com.lwzw.cloud.bean.User;
import com.lwzw.cloud.bean.Zan;
import com.lwzw.cloud.bean.viewObject.CommonShareViewObject;
import com.lwzw.cloud.bean.viewObject.ShareDetailViewObject;
import com.lwzw.cloud.bean.viewObject.ShareMessageViewObject;
import com.lwzw.cloud.constant.ServerResponse;
import com.lwzw.cloud.dao.ShareMapper;
import com.lwzw.cloud.dao.UFileMapper;
import com.lwzw.cloud.dao.UserMapper;
import com.lwzw.cloud.dao.ZanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/share")
public class ShareController {

    @Autowired
    UFileMapper UFileMapper;

    @Autowired
    ShareMapper shareMapper;

    @Autowired
    UFileMapper uFileMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ZanMapper zanMapper;

    /**
     *
     * @param shareUid 分享对象id
     * @param shareUfid 分享文件id
     * @param type 分享类型(1为私密分享，0为公共分享)
     * @return
     */
    @ResponseBody
    @RequestMapping("/addShare")
    public ServerResponse addShare(@RequestParam(value="shareUid",required = false)String shareUid,
                                   @RequestParam(value = "shareUfid")String shareUfid,
                                   @RequestParam(value = "ranks",required = false)String ranks,
                                   @RequestParam("type")String type,
                                   HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        UFile uFile = UFileMapper.selectByPrimaryKey(Integer.valueOf(shareUfid));
        Share share = new Share();
        share.setFromuid(loginUser.getUid());
        if (shareUid==null){
            share.setTouid(-1);
            share.setLikes(0);
            share.setDislikes(0);
            share.setRanks(Integer.valueOf(ranks));
            share.setIsread(-1);
        }else{
            share.setTouid(Integer.valueOf(shareUid));
            share.setIsread(0);
        }
        share.setFid(uFile.getUfid());
        share.setStatus(Integer.valueOf(type));
        share.setSharedate(new Date());
        System.out.println(share);
        int count = shareMapper.insertSelective(share);
        if (count>0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("内部错误");
    }

    /**
     * 查询分享文件列表
     * @param friendId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryShareMessageList")
    public ServerResponse queryShareMessageList(@RequestParam(value = "friendUid",required = false)String friendId,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("loginUser");
        if (null==friendId||"".equals(friendId)){//如果没有传分享对象id就是查询公共分享
            List<ShareDetailViewObject>messageViewObjects = shareMapper.selectShareDetail();
            for (ShareDetailViewObject shareDetailViewObject:messageViewObjects){
                Zan zan = zanMapper.selectByUidAndSid(user.getUid(),shareDetailViewObject.getSid());
                if (null==zan){
                    shareDetailViewObject.setStatus(0);
                }else{
                    shareDetailViewObject.setStatus(zan.getStatus());
                }
            }
            return ServerResponse.createBySuccess(messageViewObjects);
        }
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        List<ShareMessageViewObject>messageViewObjects = shareMapper.selectByFromAndToUid(loginUser.getUid(),Integer.valueOf(friendId));
        return ServerResponse.createBySuccess(messageViewObjects);
    }

    /**
     * 保存私密分享的文件
     * @param ufids
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveShareFile")
    public ServerResponse saveShareFile(@RequestParam("ufids")String[] ufids,HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        int count = 0;
        for (String ufid:ufids){
            UFile uFile = UFileMapper.selectByUFidAndUid(Integer.valueOf(ufid),loginUser.getUid());
            if (uFile==null){
                uFile = uFileMapper.selectByPrimaryKey(Integer.valueOf(ufid));
                uFile.setUid(loginUser.getUid());
                uFile.setSavedate(new Date());
                uFile.setUfid(null);
                count += UFileMapper.insertSelective(uFile);
            }
        }
        Map<String,Object> result = new HashMap<>();
        result.put("updateCount",count);
        result.put("existCount",ufids.length-count);
        return ServerResponse.createBySuccess(result);
    }

    /**
     * 点赞或取消点赞
     * @return
     */
    @ResponseBody
    @RequestMapping("/zan")
    public ServerResponse zan(@RequestParam("sid")String sid,@RequestParam("type")String type, HttpServletRequest request){
        Share share = shareMapper.selectByPrimaryKey(Integer.valueOf(sid));
        User user = (User) request.getSession().getAttribute("loginUser");
        Zan zan = zanMapper.selectByUidAndSid(user.getUid(),Integer.valueOf(sid));
        int score = user.getScore();
        int likes = share.getLikes();
        if (type.equals("1")){
            share.setLikes(++likes);
            user.setScore(++score);//增加经验
            if (user.getScore()%500==0 && user.getScore()/500 <= 5){//恰好升级
                user.setCapacity(user.getCapacity()+1024);//增加1G容量
                userMapper.updateUserSelective(user);
            }
            if (null==zan){
                zan = new Zan();
                zan.setSid(Integer.valueOf(sid));
                zan.setUid(user.getUid());
                zan.setStatus(1);
                zanMapper.insertSelective(zan);
            }else {
                zan.setStatus(1);
                zanMapper.updateByPrimaryKeySelective(zan);
            }
        }else{
            --likes;
            if (likes<0){
                share.setLikes(0);
            }else{
                share.setLikes(likes);//取消点赞
                user.setScore(--score);//减少经验
                if (score%500==0 && score/500<=5){
                    user.setCapacity(user.getCapacity()-1024);
                    userMapper.updateUserSelective(user);
                }
            }
            zan.setStatus(0);
            zanMapper.updateByPrimaryKeySelective(zan);
        }
        shareMapper.updateByPrimaryKeySelective(share);
        userMapper.updateUserSelective(user);
        return ServerResponse.createBySuccess();
    }

    @ResponseBody
    @RequestMapping("/saveShare")
    public ServerResponse addShare(@RequestParam("sid")String sid,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("loginUser");
        Share share = shareMapper.selectByPrimaryKey(Integer.valueOf(sid));
        if (user.getScore()/500<share.getRanks()){
            return ServerResponse.createBySuccessMessage("等级不足!");
        }
        UFile uFile = UFileMapper.selectByPrimaryKey(share.getFid());
        UFile newFile = uFileMapper.selectByFidAndUid(uFile.getFid(),user.getUid());
        int count ;
        if (null==newFile){
            newFile = new UFile();
            newFile.setSavedate(new Date());
            newFile.setUid(user.getUid());
            newFile.setName(uFile.getName());
            newFile.setFid(uFile.getFid());
            count = uFileMapper.insertSelective(newFile);
            if (count>0){
                return ServerResponse.createBySuccessMessage("保存成功");
            }else{
                return ServerResponse.createBySuccessMessage("保存失败");
            }
        }
        return ServerResponse.createBySuccessMessage("文件已存在");
    }




    /**
     * 公共分享页面跳转
     */
    @RequestMapping("/commonshare")
    public String commonShare(){
        return "/pages/commonshare";
    }

}
