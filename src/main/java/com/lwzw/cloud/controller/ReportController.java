package com.lwzw.cloud.controller;

import com.lwzw.cloud.bean.Report;
import com.lwzw.cloud.bean.User;
import com.lwzw.cloud.constant.ServerResponse;
import com.lwzw.cloud.dao.ReportMapper;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("report")
public class ReportController {

    @Autowired
    ReportMapper reportMapper;

    @ResponseBody
    @RequestMapping("/addReport")
    public ServerResponse addReport(@RequestParam("sid")String sid, @RequestParam("reason")String reason, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("loginUser");
        Report report = reportMapper.selectByUserAndSid(user.getUid(),Integer.valueOf(sid));
        if (report!=null){
            return ServerResponse.createBySuccessMessage("请勿重复举报");
        }
        report = new Report();
        report.setDescription(reason);
        report.setSid(Integer.valueOf(sid));
        report.setUid(user.getUid());
        report.setIsread(false);
        int count = reportMapper.insertSelective(report);
        if (count>0){
            return ServerResponse.createBySuccessMessage("举报成功");
        }
        return ServerResponse.createBySuccessMessage("举报失败");
    }

}
