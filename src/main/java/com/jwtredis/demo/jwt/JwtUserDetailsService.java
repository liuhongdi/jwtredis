package com.jwtredis.demo.jwt;

import com.jwtredis.demo.constant.ResponseCode;
import com.jwtredis.demo.pojo.SysUser;
import com.jwtredis.demo.result.RestResult;
import com.jwtredis.demo.security.SecUser;
import com.jwtredis.demo.service.SysUserService;
import com.jwtredis.demo.service.UserRedisService;
import com.jwtredis.demo.util.ServletUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Resource
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("-----loadUserByUsername");
        SysUser oneUser = sysUserService.getOneUserByUsername(username);//数据库查询 看用户是否存在
        String encodedPassword = oneUser.getPassword();
        Collection<GrantedAuthority> collection = new ArrayList<>();//权限集合
        //用户角色role前面要添加ROLE_
        List<String> roles = oneUser.getRoles();
        System.out.println(roles);
        for (String roleone : roles) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+roleone);
            collection.add(grantedAuthority);
        }
        //给用户增加用户id和昵称
        SecUser user = new SecUser(username,encodedPassword,collection);
        user.setUserid(oneUser.getUserId());
        user.setNickname(oneUser.getNickName());
        return user;
    }

    public UserDetails loadUserBySysUser(SysUser oneUser) throws UsernameNotFoundException {
        System.out.println("-----loadUserByUser");
        //SysUser oneUser = sysUserService.getOneUserByUsername(username);//数据库查询 看用户是否存在
        String encodedPassword = oneUser.getPassword();
        Collection<GrantedAuthority> collection = new ArrayList<>();//权限集合
        //用户角色role前面要添加ROLE_
        List<String> roles = oneUser.getRoles();
        System.out.println(roles);
        for (String roleone : roles) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+roleone);
            collection.add(grantedAuthority);
        }
        //给用户增加用户id和昵称
        SecUser user = new SecUser(oneUser.getUserName(),encodedPassword,collection);
        user.setUserid(oneUser.getUserId());
        user.setNickname(oneUser.getNickName());
        return user;
    }
}