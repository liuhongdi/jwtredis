package com.jwtredis.demo.service;

import com.jwtredis.demo.pojo.SysUser;

public interface SysUserService {
    public SysUser getOneUserByUsername(String Username);
}
