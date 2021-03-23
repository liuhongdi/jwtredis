package com.jwtredis.demo.jwt;

//import com.fishpro.securityjwt.util.JwtTokenUtil;
import com.jwtredis.demo.constant.ResponseCode;
import com.jwtredis.demo.pojo.SysUser;
import com.jwtredis.demo.result.RestResult;
import com.jwtredis.demo.service.UserRedisService;
import com.jwtredis.demo.util.ServletUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器 用于 Spring Boot Security
 * OncePerRequestFilter 一次请求只通过一次filter，而不需要重复执行
 * */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Resource
    private UserRedisService userRedisService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        // JWT Token 获取请求头部的 Bearer
        System.out.println("filter:header:"+requestTokenHeader);
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            //System.out.println("filter :requestTokenHeader not null and start with bearer");
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            } catch (MalformedJwtException e) {
                System.out.println("JWT Token MalformedJwtException");
            }
        } else {
            //System.out.println("filter :requestTokenHeader is null || not start with bearer");
            //logger.warn("JWT Token does not begin with Bearer String");
        }

        // 验证,
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //System.out.println("-----------get username from client:"+username);
            SysUser oneUser = userRedisService.getOneUserByUserToken(username);
            if (oneUser == null) {
                ServletUtil.printRestResult(RestResult.error(ResponseCode.LOGIN_NEED));
                return;
            }
            //get UserDetails
            UserDetails userDetails = this.jwtUserDetailsService.loadUserBySysUser(oneUser);

            // JWT 验证通过 使用Spring Security 管理
            if (jwtTokenUtil.validateTokenByOrigToken(jwtToken, oneUser.getOrigToken())) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
               System.out.println("jwtTokenUtil.validateToken not success");
            }
        }
        chain.doFilter(request, response);
    }
}