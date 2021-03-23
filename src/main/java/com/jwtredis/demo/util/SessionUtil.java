package com.jwtredis.demo.util;

import com.jwtredis.demo.security.SecUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
//import org.springframework.security.core.userdetails.User;

public class SessionUtil {
    //得到当前登录的用户名
    public static String getCurrentUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }
    //得到当前登录的用户
    public static SecUser getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                SecUser currentuser = (SecUser) principal;
                return currentuser;
            }
            return null;
        }
        return null;
    }
}