package com.example.chibiotaku.util;

import com.example.chibiotaku.util.exceptions.NotAuthenticatedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class UserChecker {


    public static void checkUserIsLogged(){

        Principal principal = (Principal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();


        if(principal == null){
            throw new NotAuthenticatedException();
        }
    }

}
