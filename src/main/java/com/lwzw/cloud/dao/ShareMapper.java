package com.lwzw.cloud.dao;

import com.lwzw.cloud.bean.Share;

public interface ShareMapper {
    int deleteByPrimaryKey(Integer sid);

    int insert(Share record);

    int insertSelective(Share record);

    Share selectByPrimaryKey(Integer sid);

    int updateByPrimaryKeySelective(Share record);

    int updateByPrimaryKey(Share record);
}