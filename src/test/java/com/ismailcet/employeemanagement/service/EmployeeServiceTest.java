package com.ismailcet.employeemanagement.service;

import com.ismailcet.employeemanagement.JWT.EmployeeDetailsService;
import com.ismailcet.employeemanagement.JWT.JwtFilter;
import com.ismailcet.employeemanagement.JWT.JwtUtil;
import com.ismailcet.employeemanagement.dto.converter.EmployeeDtoConverter;
import com.ismailcet.employeemanagement.dto.request.CreateEmployeeRequest;
import com.ismailcet.employeemanagement.entity.Department;
import com.ismailcet.employeemanagement.entity.Employee;
import com.ismailcet.employeemanagement.entity.Position;
import com.ismailcet.employeemanagement.repository.DepartmentRepository;
import com.ismailcet.employeemanagement.repository.EmployeeRepository;
import com.ismailcet.employeemanagement.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    EmployeeService employeeService;
    EmployeeRepository employeeRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    EmployeeDetailsService employeeDetailsService;
    JwtUtil jwtUtil;
    JwtFilter jwtFilter;
    DepartmentRepository departmentRepository;
    PositionRepository positionRepository;
    EmployeeDtoConverter employeeDtoConverter;
    EmailSenderService emailSenderService;

    @BeforeEach
    public void setUp(){
        employeeRepository = mock(EmployeeRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);
        employeeDetailsService = mock(EmployeeDetailsService.class);
        jwtUtil = mock(JwtUtil.class);
        jwtFilter = mock(JwtFilter.class);
        departmentRepository = mock(DepartmentRepository.class);
        positionRepository = mock(PositionRepository.class);
        employeeDtoConverter = mock(EmployeeDtoConverter.class);
        emailSenderService = mock(EmailSenderService.class);

        employeeService = new EmployeeService(
                employeeRepository,
                passwordEncoder,
                authenticationManager,
                employeeDetailsService,
                jwtUtil,
                jwtFilter,
                departmentRepository,
                positionRepository,
                employeeDtoConverter,
                emailSenderService
        );
    }

    @Test
    public void testCreateEmployee_whenJwtFilterIsManagerAndDepartmentIdExistAndPositionIdIsExist_shouldCreateEmployeeSuccessfullyAndReturnString(){

        Department department = Department.builder()
                .id(1)
                .name("test-department")
                .build();
        Position position = Position.builder()
                .id(1)
                .name("test-position")
                .build();

        String password = "test-password";

        CreateEmployeeRequest request = CreateEmployeeRequest
                .builder()
                .name("test-name")
                .surname("test-surname")
                .type("test-type")
                .tc("test-tc")
                .email("test-email")
                .phone("test-phone")
                .age(16)
                .salary(20.0)
                .positionId(position.getId())
                .departmentId(department.getId())
                .password(password)
                .build();

        Employee savedEmployee = Employee.builder()
                .name("test-name")
                .surname("test-surname")
                .type("test-type")
                .tc("test-tc")
                .email("test-email")
                .phone("test-phone")
                .age(16)
                .salary(20.0)
                .position(position)
                .department(department)
                .password(passwordEncoder.encode(password))
                .build();

        String expected = "Successfully Employee Added";

        when(jwtFilter.isManager()).thenReturn(true);
        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(positionRepository.findById(department.getId())).thenReturn(Optional.of(position));
        //doNothing().when(employeeRepository).save(savedEmployee);

        String actual = employeeService.createEmployee(request);

        assertEquals(expected, actual);
        assertTrue(actual.contains("Successfully"));


        verify(jwtFilter).isManager();
        verify(departmentRepository).findById(any(Integer.class));
        verify(positionRepository).findById(any(Integer.class));

    }

}