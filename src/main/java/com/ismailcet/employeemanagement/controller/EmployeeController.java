package com.ismailcet.employeemanagement.controller;

import com.ismailcet.employeemanagement.dto.request.CreateEmployeeRequest;
import com.ismailcet.employeemanagement.dto.request.LoginEmployeeRequest;
import com.ismailcet.employeemanagement.entity.Employee;
import com.ismailcet.employeemanagement.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {


    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateEmployeeRequest createEmployeeRequest){
        return new ResponseEntity<>(
                employeeService.register(createEmployeeRequest),HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginEmployeeRequest loginEmployeeRequest){
       return ResponseEntity.ok(employeeService.login(loginEmployeeRequest));
    }
}
