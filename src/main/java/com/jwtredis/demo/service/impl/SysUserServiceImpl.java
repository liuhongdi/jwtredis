package com.jwtredis.demo.service.impl;

import com.jwtredis.demo.mapper.UserMapper;
import com.jwtredis.demo.pojo.SysUser;
import com.jwtredis.demo.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private UserMapper userMapper;
    //根据用户名查询数据库得到用户信息
    @Override
    public SysUser getOneUserByUsername(String username) {
        System.out.println("从数据库查询得到用户信息");
        SysUser user_one = userMapper.selectOneUserByUserName(username);
        return user_one;
    }

}
