package com.jwtredis.demo.controller;

import com.jwtredis.demo.result.RestResult;
import com.jwtredis.demo.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class HomeController {
    //session详情
    @GetMapping("/session")
    @ResponseBody
    public RestResult session() {
        System.out.println("-------begin get session:");
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", SessionUtil.getCurrentUserName());
        data.put("userid", String.valueOf(SessionUtil.getCurrentUser().getUserid()));
        data.put("nickname", SessionUtil.getCurrentUser().getNickname());
        data.put("roles", SessionUtil.getCurrentUser().getAuthorities().toString());
        return RestResult.success(data);
    }
    //显示getsession页面
    @GetMapping("/getsession")
    public String get() {
        return "home/getsession";
    }
    //显示login页面
    @GetMapping("/login")
    public String login() {
        return "home/login";
    }
}