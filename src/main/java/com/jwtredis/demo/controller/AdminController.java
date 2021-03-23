package com.jwtredis.demo.controller;

import com.jwtredis.demo.result.RestResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    //返回一条hello信息
    @GetMapping("/hello")
    public RestResult hello() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("tip", "this is admin hello api");
        return RestResult.success(data);
    }
}