package com.jwtredis.demo.mapper;

import com.jwtredis.demo.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    public SysUser selectOneUserByUserName(String username);
}