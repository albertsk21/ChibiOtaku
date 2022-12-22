package com.example.chibiotaku.util.exceptions;

public class NotAuthenticatedException extends RuntimeException {


    public NotAuthenticatedException() {
        super("You are not authenticated");
    }
}
