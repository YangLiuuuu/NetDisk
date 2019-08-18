package com.lwzw.cloud.dao;

import com.lwzw.cloud.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    List<User>selectByUsername(@Param("username") String username);

    int updateUserSelective(User record);

    int updateUser(User user);
}