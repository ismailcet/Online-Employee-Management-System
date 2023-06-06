package com.ismailcet.employeemanagement.exception;

import com.ismailcet.employeemanagement.entity.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GeneralExceptionAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Object> handlerEmployeeNotFoundException(EmployeeNotFoundException ex){
        List <String> details = new ArrayList<>();

        details.add(ex.getMessage());

        ExceptionResponse err = new ExceptionResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                "Employee Not Found",
                details);

        return new ResponseEntity<>(err, err.getStatus());
    }

    @ExceptionHandler(AuthenticationNotFoundException.class)
    public ResponseEntity<Object> handlerAuthenticationNotFoundException(AuthenticationNotFoundException ex){
        List <String> details = new ArrayList<>();

        details.add(ex.getMessage());

        ExceptionResponse err = new ExceptionResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED,
                "Unauthorized Access ! ",
                details);
        return new ResponseEntity<>(err, err.getStatus());
    }

    @ExceptionHandler(PositionNotFoundException.class)
    public ResponseEntity<Object> handlerPositionNotFoundException(PositionNotFoundException ex){
        List <String> details = new ArrayList<>();

        details.add(ex.getMessage());

        ExceptionResponse err = new ExceptionResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED,
                "Position is not valid ! ",
                details);
        return new ResponseEntity<>(err, err.getStatus());
    }
}
