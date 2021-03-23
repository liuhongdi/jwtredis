package com.jwtredis.demo.util;

import com.alibaba.fastjson.JSON;
import com.jwtredis.demo.result.RestResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * servlet工具
 *
 * @author liuhongdi
 */
public class ServletUtil {

    //获取request
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    //获取response
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    //获取session
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    //获取ServletRequestAttributes
    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    //得到当前的url地址
    public static String getUrl() {
        HttpServletRequest request= getRequestAttributes().getRequest();
        String ip = request.getRemoteAddr();
        String url = request.getRequestURL().toString();
        String param = request.getQueryString();
        //System.out.println("param:"+param);
        String finalurl = ip+"--"+url;
        if (param != null) {
            finalurl= ip+"--"+url+"?"+param;
        }
        return finalurl;
    }

    //把restresult打印到response
    public static void printRestResult(RestResult rest) {
        HttpServletResponse response= getResponse();
        String resp = JSON.toJSONString(rest);
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            //response.getWriter().print(resp);
            out.write(resp);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return null;
    }

    //将字符串渲染到客户端
    public static String printString(String string) {
        try {
            HttpServletResponse response= getResponse();
            response.setContentType("text/html");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //判断请求是否是ajax
    public static boolean isAjax() {
        HttpServletRequest request= getRequest();
        return (request.getHeader("X-Requested-With") != null &&
                "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
    }

}