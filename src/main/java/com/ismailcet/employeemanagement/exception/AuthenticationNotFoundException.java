package com.ismailcet.employeemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationNotFoundException extends RuntimeException{
    public AuthenticationNotFoundException(String message){
        super(message);
    }
}
