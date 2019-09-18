package com.lwzw.cloud.dao;

import com.lwzw.cloud.bean.Zan;
import org.apache.ibatis.annotations.Param;

public interface ZanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Zan record);

    int insertSelective(Zan record);

    Zan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Zan record);

    int updateByPrimaryKey(Zan record);

    Zan selectByUidAndSid(@Param("uid")Integer uid,@Param("sid")Integer sid);
}