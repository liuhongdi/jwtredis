package com.jwtredis.demo.service;

import com.jwtredis.demo.pojo.SysUser;

public interface UserRedisService {
    public SysUser getOneUserByUserToken(String userToken);
    public void setOneUser(SysUser user,String userToken);
}
