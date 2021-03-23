package com.jwtredis.demo.pojo;

import java.util.List;

public class SysUser {
    //用户id
    private Integer userId;
    //用户名
    private String userName;
    //密码
    private String password;
    //角色
    private List<String> roles;
    //昵称
    private String nickName;
    //当前保存的token
    private String origToken;

    public Integer getUserId() {
        return this.userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return this.roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOrigToken() {
        return origToken;
    }
    public void setOrigToken(String origToken) {
        this.origToken = origToken;
    }
}