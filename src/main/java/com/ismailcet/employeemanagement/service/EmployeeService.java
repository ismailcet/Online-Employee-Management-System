package com.ismailcet.employeemanagement.service;

import com.ismailcet.employeemanagement.JWT.EmployeeDetailsService;
import com.ismailcet.employeemanagement.JWT.JwtFilter;
import com.ismailcet.employeemanagement.JWT.JwtUtil;
import com.ismailcet.employeemanagement.dto.request.*;
import com.ismailcet.employeemanagement.dto.EmployeeDto;
import com.ismailcet.employeemanagement.dto.converter.EmployeeDtoConverter;
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
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private final EmailSenderService emailSenderService;


    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmployeeDetailsService employeeDetailsService, JwtUtil jwtUtil, JwtFilter jwtFilter, DepartmentRepository departmentRepository, PositionRepository positionRepository, EmployeeDtoConverter employeeDtoConverter, EmailSenderService emailSenderService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.employeeDetailsService = employeeDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.employeeDtoConverter = employeeDtoConverter;
        this.emailSenderService = emailSenderService;
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
                            .email(createEmployeeRequest.getEmail())
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
                            .email(createEmployeeRequest.getEmail())
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
                return jwtUtil.generateToken(employeeDetailsService.getEmployeeDetail().getTc(),
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
            return employeeDtoConverter.convert(current);
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
                        .map(employeeDtoConverter::convert)
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
                            .email(employee.get().getEmail())
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
    public EmployeeDto getEmployeeByTc(String tc) {
        try{
            Employee employee =
                    employeeRepository.findByTc(tc);
            if(!Objects.isNull(employee)){
                if(jwtFilter.isManager() || jwtFilter.isSupervisor() || employee.getTc().equals(jwtFilter.getCurrentEmployee())){
                    EmployeeDto response = EmployeeDto.builder()
                            .name(employee.getName())
                            .surname(employee.getSurname())
                            .tc(employee.getTc())
                            .type(employee.getType())
                            .email(employee.getEmail())
                            .age(employee.getAge())
                            .salary(employee.getSalary())
                            .phone(employee.getPhone())
                            .departmentName(employee.getDepartment().getName())
                            .positionName(employee.getPosition().getName())
                            .build();
                    return employeeDtoConverter.convert(employee);
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
                            .email(employee.get().getEmail())
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

    public String changeEmployeeRoleByEmployeeId(ChangeRoleRequest changeRoleRequest, Integer id) {
        try{
            Employee employee =
                    employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException("Employee Id does not valid ! "));
            if(jwtFilter.isSupervisor()){
                employee.setType(changeRoleRequest.getType());
                employeeRepository.save(employee);
                return "Employee Role is successfully changed ! ";
            }else{
                throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public Map<String, String> forgotPasswordCreateResetPasswordToken(CreateResetPasswordTokenRequest createResetPasswordTokenRequest) {
        try{
            Employee employee =
                    employeeRepository.findByTc(createResetPasswordTokenRequest.getTc());
            if(!Objects.isNull(employee)){
                Integer token = 10000+new Random(System.currentTimeMillis()).nextInt(20000);

                employee.setResetPasswordToken(String.valueOf(token));
                String subject = "Password Reset Token : " + String.valueOf(token);
                Map<String, String> response = new HashMap<>();

                response.put("token",BCrypt.hashpw(String.valueOf(token),BCrypt.gensalt(10)));
                response.put("tc",employee.getTc());

                employeeRepository.save(employee);
                emailSenderService.sendEmail(employee.getEmail(),subject,"Email Reset Code");

                return response;
            }else{
                throw new EmployeeNotFoundException("Employee is not valid !");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public String changePasswordWithResetToken(ChangePasswordWithToken changePasswordWithToken) {
        try{
            Employee employee  =
                    employeeRepository.findByTc(changePasswordWithToken.getTc());
            if(!Objects.isNull(employee)){
                if(employee.getResetPasswordToken().equals(changePasswordWithToken.getToken())){
                    employee.setPassword(passwordEncoder.encode(changePasswordWithToken.getPassword()));
                    employee.setResetPasswordToken(null);

                    employeeRepository.save(employee);

                    return "Password is changed successfully";
                }else{
                    throw new EmployeeNotFoundException("Reset Token is not correct ! ");
                }
            }else{
                throw new EmployeeNotFoundException("Employee is not valid !");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

}

