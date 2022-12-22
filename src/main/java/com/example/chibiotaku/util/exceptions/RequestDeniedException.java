package com.example.chibiotaku.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "your request was denied")
public class RequestDeniedException extends RuntimeException {
    public RequestDeniedException() {
        super("your request was denied");
    }
}
