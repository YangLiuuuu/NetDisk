package com.lwzw.cloud.dao;

import com.lwzw.cloud.bean.UFile;
import com.lwzw.cloud.bean.User;
import com.lwzw.cloud.bean.viewObject.FileViewObject;
import com.lwzw.cloud.constant.ServerResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UFileMapper {
    int deleteByPrimaryKey(Integer ufid);

    int insert(UFile record);

    int insertSelective(UFile record);

    UFile selectByPrimaryKey(Integer ufid);

    int updateByPrimaryKeySelective(UFile record);

    int updateByPrimaryKey(UFile record);

    List<UFile> selectAllByUser(Integer uid);

    List<FileViewObject> selectFileViewObject(@Param("user")User user);

    List<FileViewObject> selectPictureFile(@Param("user")User user);

    List<FileViewObject> selectVideoAndMusicFile(@Param("user")User user);

    List<FileViewObject> selectDocFile(@Param("user")User user);
}