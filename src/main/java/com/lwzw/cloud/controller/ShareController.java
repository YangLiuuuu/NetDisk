package com.lwzw.cloud.controller;

import com.lwzw.cloud.bean.Share;
import com.lwzw.cloud.bean.UFile;
import com.lwzw.cloud.bean.User;
import com.lwzw.cloud.constant.ServerResponse;
import com.lwzw.cloud.dao.ShareMapper;
import com.lwzw.cloud.dao.UFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/share")
public class ShareController {

    @Autowired
    UFileMapper UFileMapper;

    @Autowired
    ShareMapper shareMapper;

    /**
     *
     * @param shareUid 分享对象id
     * @param shareUfid 分享文件id
     * @param type 分享类型(1为私密分享，0为公共分享)
     * @return
     */
    @ResponseBody
    @RequestMapping("/addShare")
    public ServerResponse addShare(@RequestParam("shareUid")String shareUid,
                                   @RequestParam("shareUfid")String shareUfid,
                                   @RequestParam("type")String type,
                                   HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        UFile uFile = UFileMapper.selectByPrimaryKey(Integer.valueOf(shareUfid));
        Share share = new Share();
        share.setFromuid(loginUser.getUid());
        share.setTouid(Integer.valueOf(shareUid));
        share.setFid(uFile.getFid());
        share.setIsread(0);
        share.setStatus(Integer.valueOf(type));
        share.setSharedate(new Date());
        int count = shareMapper.insertSelective(share);
        if (count>0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("内部错误");
    }
}
