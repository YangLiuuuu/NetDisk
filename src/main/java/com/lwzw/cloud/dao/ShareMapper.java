package com.lwzw.cloud.dao;

import com.lwzw.cloud.bean.Share;
import com.lwzw.cloud.bean.viewObject.ShareMessageViewObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShareMapper {
    int deleteByPrimaryKey(Integer sid);

    int insert(Share record);

    int insertSelective(Share record);

    Share selectByPrimaryKey(Integer sid);

    int updateByPrimaryKeySelective(Share record);

    int updateByPrimaryKey(Share record);

    List<ShareMessageViewObject> selectByFromAndToUid(@Param("from")Integer from, @Param("to")Integer to);
}