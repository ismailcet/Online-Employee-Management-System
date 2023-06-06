package com.ismailcet.employeemanagement.service;

import com.ismailcet.employeemanagement.JWT.EmployeeDetailsService;
import com.ismailcet.employeemanagement.JWT.JwtFilter;
import com.ismailcet.employeemanagement.JWT.JwtUtil;
import com.ismailcet.employeemanagement.dto.request.UpdateEmployeeRequest;
import com.ismailcet.employeemanagement.dto.EmployeeDto;
import com.ismailcet.employeemanagement.dto.converter.EmployeeDtoConverter;
import com.ismailcet.employeemanagement.dto.request.ChangePasswordRequest;
import com.ismailcet.employeemanagement.dto.request.CreateEmployeeRequest;
import com.ismailcet.employeemanagement.dto.request.LoginEmployeeRequest;
import com.ismailcet.employeemanagement.entity.Department;
import com.ismailcet.employeemanagement.entity.Employee;
import com.ismailcet.employeemanagement.entity.Position;
import com.ismailcet.employeemanagement.exception.AuthenticationNotFoundException;
import com.ismailcet.employeemanagement.exception.EmployeeNotFoundException;
import com.ismailcet.employeemanagement.repository.DepartmentRepository;
import com.ismailcet.employeemanagement.repository.EmployeeRepository;
import com.ismailcet.employeemanagement.repository.PositionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmployeeDetailsService employeeDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtFilter jwtFilter;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeDtoConverter employeeDtoConverter;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmployeeDetailsService employeeDetailsService, JwtUtil jwtUtil, JwtFilter jwtFilter, DepartmentRepository departmentRepository, PositionRepository positionRepository, EmployeeDtoConverter employeeDtoConverter) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.employeeDetailsService = employeeDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.employeeDtoConverter = employeeDtoConverter;
    }

    public String createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        try{
            //if(jwtFilter.isManager()){
                Optional <Department> department =
                        departmentRepository.findById(createEmployeeRequest.getDepartmentId());
                Optional <Position> position =
                        positionRepository.findById(createEmployeeRequest.getPositionId());

                if(!department.isEmpty() || !position.isEmpty()){
                    Employee employee = Employee.builder()
                            .name(createEmployeeRequest.getName())
                            .surname(createEmployeeRequest.getSurname())
                            .type(createEmployeeRequest.getType())
                            .tc(createEmployeeRequest.getTc())
                            .phone(createEmployeeRequest.getPhone())
                            .age(createEmployeeRequest.getAge())
                            .salary(createEmployeeRequest.getSalary())
                            .position(position.get())
                            .department(department.get())
                            .password(passwordEncoder.encode(createEmployeeRequest.getPassword()))
                            .build();
                    employeeRepository.save(employee);
                    return "Successfully Employee Added";
                }else{
                    throw new EmployeeNotFoundException("Department Id or Position Id does not valid! ");
                }
           // }
            /*else{
                throw new AuthenticationNotFoundException("Not Authentication ! ");
            }*/
        }catch (Exception ex){
            throw new EmployeeNotFoundException(ex.getMessage());
        }
    }
    public String createManager(CreateEmployeeRequest createEmployeeRequest) {
        try{
            if(jwtFilter.isSupervisor()){
                Optional <Department> department =
                        departmentRepository.findById(createEmployeeRequest.getDepartmentId());
                Optional <Position> position =
                        positionRepository.findById(createEmployeeRequest.getPositionId());

                if(!department.isEmpty() || !position.isEmpty()){
                    Employee employee = Employee.builder()
                            .name(createEmployeeRequest.getName())
                            .surname(createEmployeeRequest.getSurname())
                            .type(createEmployeeRequest.getType())
                            .tc(createEmployeeRequest.getTc())
                            .phone(createEmployeeRequest.getPhone())
                            .age(createEmployeeRequest.getAge())
                            .salary(createEmployeeRequest.getSalary())
                            .position(position.get())
                            .department(department.get())
                            .password(passwordEncoder.encode(createEmployeeRequest.getPassword()))
                            .build();
                    employeeRepository.save(employee);
                    return "Successfully Employee Added";
                }else{
                    throw new EmployeeNotFoundException("Department Id or Position Id does not valid! ");
                }
            }else{
                throw new AuthenticationNotFoundException("Not Authentication ! ");
            }
        }catch (Exception ex){
            throw new EmployeeNotFoundException(ex.getMessage());
        }
    }

    public String login(LoginEmployeeRequest loginEmployeeRequest) {
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginEmployeeRequest.getTc(), loginEmployeeRequest.getPassword())
            );
            if(auth.isAuthenticated()){
                return "Token : " + jwtUtil.generateToken(employeeDetailsService.getEmployeeDetail().getTc(),
                        employeeDetailsService.getEmployeeDetail().getType().toString());
            }else{
                throw new EmployeeNotFoundException("Something went wrong");
            }
        }catch (Exception ex){
            throw new EmployeeNotFoundException(ex.getMessage());
        }
    }

    public EmployeeDto getEmployeeDetailFromToken() {
        String employeeTc =
                jwtFilter.getCurrentEmployee();
        Employee current =
                employeeRepository.findByTc(employeeTc);

        if(!Objects.isNull(current)){
            EmployeeDto response = EmployeeDto.builder()
                    .name(current.getName())
                    .surname(current.getSurname())
                    .tc(current.getTc())
                    .type(current.getType())
                    .age(current.getAge())
                    .salary(current.getSalary())
                    .phone(current.getPhone())
                    .departmentName(current.getDepartment().getName())
                    .positionName(current.getPosition().getName())
                    .build();
            return response;
        }
        throw new EmployeeNotFoundException("Employee Not Found ! ");
    }

    public void deleteEmployeeById(Integer id) {
        try{
            if(jwtFilter.isManager() || jwtFilter.isSupervisor()){
                Optional <Employee> employee = employeeRepository.findById(id);
                if(!employee.isEmpty()){
                    employeeRepository.deleteById(id);
                }else{
                    throw new EmployeeNotFoundException("Employee Id does not valid ! ");
                }
            }else{
                throw new AuthenticationNotFoundException("Unauthorized Access ! ");
            }
        }catch(Exception ex){
            log.info(ex.getMessage());
        }
    }

    public List<EmployeeDto> getAllEmployees() {
        try{
            if(jwtFilter.isManager() || jwtFilter.isSupervisor()){
                List<EmployeeDto> employees = employeeRepository.findAll()
                        .stream()
                        .map( e -> EmployeeDto.builder()
                                .name(e.getName())
                                .surname(e.getSurname())
                                .tc(e.getTc())
                                .type(e.getType())
                                .age(e.getAge())
                                .salary(e.getSalary())
                                .phone(e.getPhone())
                                .departmentName(e.getDepartment().getName())
                                .positionName(e.getPosition().getName())
                                .build())
                        .collect(Collectors.toList());
                return employees;
            }else{
                throw new AuthenticationNotFoundException("Unauthorized Access ! ");
            }
        }catch (Exception ex){
            throw new AuthenticationNotFoundException(ex.getMessage());
        }
    }

    public EmployeeDto getEmployeeById(Integer id) {
        try{
            Optional<Employee> employee =
                    employeeRepository.findById(id);
            if(!employee.isEmpty()){
                if(jwtFilter.isManager() || jwtFilter.isSupervisor() || employee.get().getTc().equals(jwtFilter.getCurrentEmployee())){
                    EmployeeDto response = EmployeeDto.builder()
                            .name(employee.get().getName())
                            .surname(employee.get().getSurname())
                            .tc(employee.get().getTc())
                            .type(employee.get().getType())
                            .age(employee.get().getAge())
                            .salary(employee.get().getSalary())
                            .phone(employee.get().getPhone())
                            .departmentName(employee.get().getDepartment().getName())
                            .positionName(employee.get().getPosition().getName())
                            .build();
                    return employeeDtoConverter.convert(employee.get());
                }else{
                    throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
                }
            }else{
                throw new EmployeeNotFoundException("Employee Id does not valid ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public EmployeeDto updateEmployeeById(UpdateEmployeeRequest updateEmployeeRequest, Integer id) {
        try{
            Optional<Employee> employee =
                    employeeRepository.findById(id);
            Optional <Department> department =
                    departmentRepository.findById(updateEmployeeRequest.getDepartmentId());
            Optional <Position> position =
                    positionRepository.findById(updateEmployeeRequest.getPositionId());

            if(!employee.isEmpty()){

                if(jwtFilter.isManager() || jwtFilter.isSupervisor() || employee.get().getTc().equals(jwtFilter.getCurrentEmployee())){
                    Employee response = Employee.builder()
                            .id(employee.get().getId())
                            .tc(employee.get().getTc())
                            .password(employee.get().getPassword())
                            .name(updateEmployeeRequest.getName())
                            .surname(updateEmployeeRequest.getSurname())
                            .phone(updateEmployeeRequest.getPhone())
                            .age(updateEmployeeRequest.getAge())
                            .salary(updateEmployeeRequest.getSalary())
                            .department(department.get())
                            .position(position.get())
                            .build();
                    employeeRepository.save(response);
                    return employeeDtoConverter.convert(response);
                }else{
                    throw new AuthenticationNotFoundException("unauthenticated access ! ");
                }
            }else{
                throw new EmployeeNotFoundException("Employee Id does not valid ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public String changePassword(ChangePasswordRequest changePasswordRequest, Integer id) {
        try{
            Employee employee =
                    employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException("Employee Id does not valid !"));
            if(employee.getTc().equals(jwtFilter.getCurrentEmployee())){
                employee.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
                employeeRepository.save(employee);
                return "Password Successfully Changed ! ";
            }else{
                throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public String changeEmployeeRoleByEmployeeId(String role,Integer id) {
        try{
            Employee employee =
                    employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException("Employee Id does not valid ! "));
            if(jwtFilter.isSupervisor()){
                employee.setType(role);
                employeeRepository.save(employee);
                return "Employee Role is successfully changed ! ";
            }else{
                throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }
}

