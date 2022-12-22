package com.example.chibiotaku.interceptor;

import com.example.chibiotaku.util.exceptions.NotAuthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticatorInterceptor implements HandlerInterceptor {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticatorInterceptor.class);
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView){

        Object userDetails = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (userDetails instanceof UserDetails){
            LOGGER.info("User is logged");
            return;
        }

        throw new NotAuthenticatedException();
    }
}
