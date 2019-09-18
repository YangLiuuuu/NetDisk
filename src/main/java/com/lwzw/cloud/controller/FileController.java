package com.lwzw.cloud.controller;

import com.lwzw.cloud.bean.UFile;
import com.lwzw.cloud.bean.User;
import com.lwzw.cloud.bean.viewObject.FileViewObject;
import com.lwzw.cloud.constant.Constant;
import com.lwzw.cloud.constant.ServerResponse;
import com.lwzw.cloud.dao.FileMapper;
import com.lwzw.cloud.dao.UFileMapper;
import com.lwzw.cloud.dao.UserMapper;
import com.lwzw.cloud.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("file")
public class FileController  {

    @Autowired
    UserMapper userMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    UFileMapper uFileMapper;

    @RequestMapping("fileuploadpage")
    public String uploadPage(){
        return "/pages/fileupload";
    }


    /**
     * 文件上传，涉及一系列表操作，开启事务
     * @param multipartFile
     * @param request
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/upload")
    public ServerResponse fileUpload(@RequestParam("file")MultipartFile multipartFile, HttpServletRequest request){
        if (multipartFile.isEmpty()){
            return ServerResponse.createByErrorMessage("文件为空");
        }

        String fileName = multipartFile.getOriginalFilename();
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String path = Constant.homePath+loginUser.getUsername();
        double fileSize = (double)multipartFile.getSize()/1024/1024;//上传文件大小:MB
        double userCapacity = loginUser.getCapacity();//用户剩余空间
        if (fileSize>userCapacity){
            return ServerResponse.createByErrorMessage("用户空间不足");
        }
        assert fileName != null;
        String dbFileName =new Date().getTime()+"_"+(fileMapper.selectMaxFileId()-1)+fileName.substring(fileName.lastIndexOf("."));
        System.out.println("file:"+fileSize+"MB"+"  "+path+"\\"+dbFileName);
        File directory = new File(path+"\\");
        if (!directory.exists()){
            directory.mkdirs();
        }
        File file = new File(path,dbFileName);
        try {
            multipartFile.transferTo(file);//保存文件
        } catch (IOException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("主机保存文件失败");
        }
        loginUser.setScore(loginUser.getScore()+1);//增加经验
        loginUser.setCapacity(userCapacity-fileSize);//减容量
//        System.out.println(loginUser);
        userMapper.updateUserSelective(loginUser);
        request.setAttribute ("loginUser",loginUser);//更新request里保存的user
        com.lwzw.cloud.bean.File tableFile = new com.lwzw.cloud.bean.File
                (loginUser.getUid(),dbFileName,file.getPath(),fileSize, new Date());
        fileMapper.insert(tableFile);//更新文件表

        UFile userFile = new UFile(loginUser.getUid(),tableFile.getFid(),fileName,new Date());
        uFileMapper.insert(userFile);//在用户使用目录下添加文件
        return ServerResponse.createBySuccess();
    }

    /**
     * 检查是否能下载
     * @param ufid
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isCapableDownload",method = RequestMethod.POST)
    public ServerResponse fileDownLoad(@RequestParam("ufid")String ufid,HttpServletRequest request){
        UFile uFile = uFileMapper.selectByPrimaryKey(Integer.valueOf(ufid));
        User user = (User) request.getSession().getAttribute("loginUser");
        com.lwzw.cloud.bean.File fileData = fileMapper.selectByPrimaryKey(uFile.getFid());
        File file = new File(fileData.getUrl());
        if (!file.exists()){
            return ServerResponse.createByErrorMessage("内部错误");
        }
        return ServerResponse.createBySuccess();
    }

    /**
     * 文件下载
     * @param ufid 用户文件id
     * @param response
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/download")
    public ServerResponse fileDownload(@RequestParam("ufid")String ufid, HttpServletResponse response,HttpServletRequest request) throws IOException {
        UFile uFile = uFileMapper.selectByPrimaryKey(Integer.valueOf(ufid));
        User user = (User) request.getSession().getAttribute("loginUser");
        if (!user.getUid().equals(uFile.getUid())){
            response.sendRedirect("/illegalDownload");
            return ServerResponse.createByError();
        }
        com.lwzw.cloud.bean.File fileData = fileMapper.selectByPrimaryKey(uFile.getFid());
        File file = new File(fileData.getUrl());

        //进行断点续传
        ServletContext context = request.getServletContext();
        // get MIME type of the file
        String mimeType = context.getMimeType(fileData.getUrl());
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        // set content attributes for the response
        response.setContentType(mimeType);

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", URLEncoder.encode(uFile.getName(),"UTF-8"));
        response.setHeader(headerKey, headerValue);
        // 解析断点续传相关信息
        response.setHeader("Accept-Ranges", "bytes");
        long downloadSize = file.length();
        long fromPos = 0, toPos = 0;
        if (request.getHeader("Range") == null) {
            response.setHeader("Content-Length", downloadSize + "");
        } else {
            // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            String range = request.getHeader("Range");
            String bytes = range.replaceAll("bytes=", "");
            String[] ary = bytes.split("-");
            fromPos = Long.parseLong(ary[0]);
            if (ary.length == 2) {
                toPos = Long.parseLong(ary[1]);
            }
            int size;
            if (toPos > fromPos) {
                size = (int) (toPos - fromPos);
            } else {
                size = (int) (downloadSize - fromPos);
            }
            response.setHeader("Content-Length", size + "");
            downloadSize = size;
        }
        // Copy the stream to the response's output stream.
        RandomAccessFile in = null;
        OutputStream out = null;
        try {
            in = new RandomAccessFile(file, "rw");
            // 设置下载起始位置
            if (fromPos > 0) {
                in.seek(fromPos);
            }
            // 缓冲区大小
            int bufLen = (int) (downloadSize < 2048 ? downloadSize : 2048);
            byte[] buffer = new byte[bufLen];
            int num;
            int count = 0; // 当前写到客户端的大小
            out = response.getOutputStream();
            while ((num = in.read(buffer)) != -1) {
                out.write(buffer, 0, num);
                count += num;
                //处理最后一段，计算不满缓冲区的大小
                if (downloadSize - count < bufLen) {
                    bufLen = (int) (downloadSize-count);
                    if(bufLen==0){
                        break;
                    }
                    buffer = new byte[bufLen];
                }
            }
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


//        System.out.println("id compare "+user.getUid() + "    "+uFile.getUid());
//        if (!user.getUid().equals(uFile.getUid())){
//            System.out.println("redirect");
//            try {
//                response.sendRedirect("/file/illegaldownload");
//                return ServerResponse.createByError();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        response.setHeader("Content-Disposition","attachment;filename="+file.getName());//设置响应头，激活下载
//        byte[] buff = new byte[1024];
//        BufferedInputStream bis = null;
//        OutputStream os = null;
//        try {
//            os = response.getOutputStream();
//            bis = new BufferedInputStream(new FileInputStream(file));
//            int i = bis.read(buff);
//            while (i != -1){
//                os.write(buff,0,buff.length);
//                os.flush();
//                i = bis.read(buff);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return ServerResponse.createBySuccess();
    }

    /**
     * 文件批量下载
     * @return
     */
    @RequestMapping("/batchDownload")
    public ServerResponse fileBatchDownload(@RequestParam("ufids")String ufids,HttpServletResponse response){
        /**
         * 查询出文件存储路径，存储在fileUrls中
         */
        String headerKey = "Content-Disposition";
        String headerValue = null;
        List<File> fileUrls = new ArrayList<>();
        if (ufids.length()<=0){
            return ServerResponse.createByErrorMessage("未选择文件");
        }

        for (String ufid:ufids.split("-")){
            fileUrls.add(new File(uFileMapper.selectFileDownloadUrl(Integer.valueOf(ufid))));
        }

        UFile firstFile = uFileMapper.selectByPrimaryKey(Integer.valueOf(ufids.split("-")[0]));//获取第一个文件名，返回给用户弹窗下载时做文件名
        String filename = firstFile.getName();//若文件有后缀的话去掉文件后缀
        if (filename.lastIndexOf(".")!=-1){
            filename = filename.substring(0,filename.lastIndexOf("."));
        }

        //如果只选择了一个文件，那就单文件下载
        if (fileUrls.size()==1){
            OutputStream os = null;
            BufferedInputStream bis = null;
            byte[] buff = new byte[1024];
            int i;
            try {
                headerValue = String.format("attachment; filename=\"%s\"", URLEncoder.encode(firstFile.getName(),"UTF-8"));
                response.setHeader(headerKey, headerValue);
                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(fileUrls.get(0)));
                i = bis.read(buff);
                while (i != -1){
                    os.write(buff,0,buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
                response.flushBuffer();
            }catch (Exception e){
                e.printStackTrace();
                return ServerResponse.createByError();
            }finally {
                if (null!=os){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return ServerResponse.createBySuccess();
        }

        OutputStream os  = null;
        try {
            headerValue = String.format("attachment; filename=\"%s\"", URLEncoder.encode("[批量下载]"+filename+"等.zip","UTF-8"));
            response.setHeader(headerKey, headerValue);

            os = response.getOutputStream();
            FileUtils.toZip(fileUrls,os);//将压缩文件写入输出流
            response.flushBuffer();//刷新缓冲区
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("压缩文件出错");
        }finally {
            if (null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ServerResponse.createBySuccess();
    }

    /**
     * 修改文件名字
     * @param filename
     * @param ufid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "modifyFileName",method = RequestMethod.POST)
    public  ServerResponse modifyFileName(@RequestParam("filename")String filename,@RequestParam("ufid")String ufid){
        UFile uFile = new UFile();
        uFile.setUfid(Integer.valueOf(ufid));
        uFile.setName(filename);
        if (uFileMapper.updateByPrimaryKeySelective(uFile)>0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    /**
     * 删除文件
     * @param filename
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping(value = "deleteFile",method = RequestMethod.POST)
    public ServerResponse deleteFile(@RequestParam("ufid")String filename,HttpServletRequest request){
        UFile uFile = uFileMapper.selectByPrimaryKey(Integer.valueOf(filename));
        int count = uFileMapper.deleteByPrimaryKey(Integer.valueOf(filename));
        com.lwzw.cloud.bean.File file = fileMapper.selectByPrimaryKey(uFile.getFid());
        User user = (User) request.getSession().getAttribute("loginUser");
        if (count>0){
            double originalCapacity = user.getCapacity();
            user.setCapacity(originalCapacity+file.getSize());
            userMapper.updateUserSelective(user);//增加容量
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @RequestMapping("/filemanage")
    public String toFileManagePage(Model model,HttpServletRequest request){
        String fileType = request.getQueryString();//获取url查询字符串，返回相应文件
        if (null!=fileType){
            model.addAttribute("fileType",fileType.substring(fileType.indexOf("=")+1));
        }else{
            model.addAttribute("fileType","default");
        }
        return "/pages/filelist";
    }

    /**
     * 文件数据显示
     * @param fileType
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/filelist/{fileType}")
    public ServerResponse<List<FileViewObject>> fileList(@PathVariable(value = "fileType",required = false) String fileType,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("loginUser");
        List<FileViewObject> fileViewObjects = null;
        System.out.println("fileType:"+fileType);
        if (fileType.equals("pic")){
            fileViewObjects = uFileMapper.selectPictureFile(user);
        }else if (fileType.equals("mv")){
            fileViewObjects = uFileMapper.selectVideoAndMusicFile(user);
        }else if (fileType.equals("doc")){
            fileViewObjects =  uFileMapper.selectDocFile(user);
        }else{
            fileViewObjects = uFileMapper.selectFileViewObject(user);
        }
        return ServerResponse.createBySuccess(fileViewObjects);
    }

    /**
     * 非法下载请求(手动填写下载url发请求)
     * @return
     */
    @RequestMapping("/illegalDownload")
    public String illegalDownload(){
        return "/illegalDownload";
    }


}
