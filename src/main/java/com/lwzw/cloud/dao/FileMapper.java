package com.lwzw.cloud.dao;

import com.lwzw.cloud.bean.File;

import java.util.List;

public interface FileMapper {
    int deleteByPrimaryKey(Integer fid);

    int insert(File record);

    int insertSelective(File record);

    File selectByPrimaryKey(Integer fid);

    int updateByPrimaryKeySelective(File record);

    int updateByPrimaryKey(File record);

    int selectMaxFileId();
}