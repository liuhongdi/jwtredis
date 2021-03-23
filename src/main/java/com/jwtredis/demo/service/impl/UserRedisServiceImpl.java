package com.jwtredis.demo.service.impl;

import com.jwtredis.demo.jwt.JwtTokenUtil;
import com.jwtredis.demo.pojo.SysUser;
import com.jwtredis.demo.service.UserRedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class UserRedisServiceImpl implements UserRedisService {

    @Resource
    private RedisTemplate redis1Template;

    //从redis查询查询得到用户信息
    @Override
    public SysUser getOneUserByUserToken(String userToken){
        System.out.println("从redis查询得到用户信息");
        SysUser userOne;
        Object usersr = redis1Template.opsForValue().get("jwt_"+userToken);
        if (usersr == null) {
            userOne = null;
        } else {
            if (usersr.equals("-1")) {
                userOne = null;
            } else {
                userOne = (SysUser)usersr;
            }
        }
        return userOne;
    }

    //向redis写入用户信息,保存时长是jwt的配置
    @Override
    public void setOneUser(SysUser user,String userToken){
        long timeLenghth = JwtTokenUtil.JWT_TOKEN_VALIDITY;
        redis1Template.opsForValue().set("jwt_"+userToken,user,timeLenghth, TimeUnit.SECONDS);
    }
}
