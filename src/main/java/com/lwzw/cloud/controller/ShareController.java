package com.lwzw.cloud.controller;

import com.lwzw.cloud.bean.File;
import com.lwzw.cloud.bean.Share;
import com.lwzw.cloud.bean.UFile;
import com.lwzw.cloud.bean.User;
import com.lwzw.cloud.bean.viewObject.ShareMessageViewObject;
import com.lwzw.cloud.constant.ServerResponse;
import com.lwzw.cloud.dao.ShareMapper;
import com.lwzw.cloud.dao.UFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/share")
public class ShareController {

    @Autowired
    UFileMapper UFileMapper;

    @Autowired
    ShareMapper shareMapper;

    @Autowired
    UFileMapper uFileMapper;

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
    public ServerResponse queryShareMessageList(@RequestParam("friendUid")String friendId,HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        System.out.println(friendId);
//        Integer friendUid = Integer.valueOf(friendId);
        List<ShareMessageViewObject>messageViewObjects = shareMapper.selectByFromAndToUid(loginUser.getUid(),Integer.valueOf(friendId));
        return ServerResponse.createBySuccess(messageViewObjects);
    }
}
