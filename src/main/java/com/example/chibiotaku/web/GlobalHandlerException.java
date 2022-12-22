package com.example.chibiotaku.web;

import com.example.chibiotaku.util.exceptions.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
@ControllerAdvice
public class GlobalHandlerException {




    @ExceptionHandler(value = {ObjectNotFoundException.class, UsernameNotFoundException.class})
    public String handlerUsernameNotFound(){

        return "/error/404";
    }

}
