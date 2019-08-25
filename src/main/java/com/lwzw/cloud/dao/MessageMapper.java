package com.lwzw.cloud.dao;

import com.lwzw.cloud.bean.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    int deleteByPrimaryKey(Integer msgid);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer msgid);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    List<Message> selectByFromAndToUid(@Param("from")Integer from,@Param("to")Integer to);

    List<Message> selectByFromUid(@Param("from")Integer from);
}