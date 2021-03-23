package com.jwtredis.demo.jwt;

import com.alibaba.fastjson.JSONObject;
import com.jwtredis.demo.constant.ResponseCode;
import com.jwtredis.demo.pojo.SysUser;
import com.jwtredis.demo.result.RestResult;
import com.jwtredis.demo.security.SecUser;
import com.jwtredis.demo.service.SysUserService;
import com.jwtredis.demo.service.UserRedisService;
import com.jwtredis.demo.util.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthticationFilter implements Filter {

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Resource
    private UserRedisService userRedisService;

    @Resource
    private SysUserService sysUserService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("----------------AuthticationFilter init");
    }
    //过滤功能
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //得到当前的url
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getServletPath();
        if (path.equals("/auth/authenticate")) {
             System.out.println("auth path:"+path);
             //得到请求的post参数
            String username = "";
            String password = "";
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuffer sb=new StringBuffer();
                String s=null;
                while((s=br.readLine())!=null){
                    sb.append(s);
                }
                JSONObject jsonObject = JSONObject.parseObject(sb.toString());
                username = jsonObject.getString("username");
                password = jsonObject.getString("password");
                //System.out.println("name:"+name+" age:"+age);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("username:"+username);
            System.out.println("password:"+password);
            String authResult = "";
            try{
                authResult = authenticate(username,password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("authResult:"+authResult);
            if ("success".equals(authResult)) {
                SysUser oneUser = sysUserService.getOneUserByUsername(username);//数据库查询 看用户是否存在
                final UserDetails userDetails = userDetailsService.loadUserBySysUser(oneUser);
                String origToken = jwtTokenUtil.makeTokenForSave(userDetails.getUsername());
                final String token = jwtTokenUtil.generateTokenByOrig(origToken);

                //保存到redis
                oneUser.setOrigToken(origToken);
                //System.out.println("保存到redis的token:"+origToken);
                userRedisService.setOneUser(oneUser,origToken);
                //return result
                Map<String, String> mapData = new HashMap<String, String>();
                mapData.put("token", token);
                ServletUtil.printRestResult(RestResult.success(mapData));
            } else if ("badcredential".equals(authResult)){
                ServletUtil.printRestResult(RestResult.error(ResponseCode.LOGIN_FAIL));
            } else {
                ServletUtil.printRestResult(RestResult.error(ResponseCode.ERROR));
            }
            return;
        } else {
            System.out.println("not auth path:"+path);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        System.out.println("----------------filter destroy");
    }

    private String authenticate(String username, String password) throws Exception {
        try {
            System.out.println("username:"+username);
            System.out.println("password:"+password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            System.out.println("authenticate:will return success");
            return "success";
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            System.out.println("BadCredentialsException");
            System.out.println(e.toString());
            //throw new Exception("INVALID_CREDENTIALS", e);
            return "badcredential";
        }
    }
}