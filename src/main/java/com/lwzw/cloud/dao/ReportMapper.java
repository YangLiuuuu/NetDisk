package com.lwzw.cloud.dao;

import com.lwzw.cloud.bean.Report;
import org.apache.ibatis.annotations.Param;

public interface ReportMapper {
    int deleteByPrimaryKey(Integer rid);

    int insert(Report record);

    int insertSelective(Report record);

    Report selectByPrimaryKey(Integer rid);

    int updateByPrimaryKeySelective(Report record);

    int updateByPrimaryKey(Report record);

    Report selectByUserAndSid(@Param("uid") Integer uid, @Param("sid") Integer sid);
}