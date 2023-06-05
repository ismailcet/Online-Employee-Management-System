package com.ismailcet.employeemanagement.service;

import com.ismailcet.employeemanagement.JWT.EmployeeDetailsService;
import com.ismailcet.employeemanagement.JWT.JwtUtil;
import com.ismailcet.employeemanagement.dto.request.CreateEmployeeRequest;
import com.ismailcet.employeemanagement.dto.request.LoginEmployeeRequest;
import com.ismailcet.employeemanagement.entity.Employee;
import com.ismailcet.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmployeeDetailsService employeeDetailsService;
    private final JwtUtil jwtUtil;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmployeeDetailsService employeeDetailsService, JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.employeeDetailsService = employeeDetailsService;
        this.jwtUtil = jwtUtil;
    }


    public String register(CreateEmployeeRequest createEmployeeRequest) {
        Employee employee = Employee.builder()
                .name(createEmployeeRequest.getName())
                .surname(createEmployeeRequest.getSurname())
                .role("EMPLOYEE")
                .tc(createEmployeeRequest.getTc())
                .password(passwordEncoder.encode(createEmployeeRequest.getPassword()))
                .build();
        employeeRepository.save(employee);
        return "Successfully Employee Added";
    }

    public String login(LoginEmployeeRequest loginEmployeeRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginEmployeeRequest.getTc(), loginEmployeeRequest.getPassword())
        );
        if(auth.isAuthenticated()){
            return "Token : " + jwtUtil.generateToken(employeeDetailsService.getEmployeeDetail().getTc(),
                    employeeDetailsService.getEmployeeDetail().getRole().toString());
        }else{
            return "Something went wrong";
        }
    }
}

