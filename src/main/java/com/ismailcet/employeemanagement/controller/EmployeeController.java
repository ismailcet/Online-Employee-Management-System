package com.ismailcet.employeemanagement.controller;

import com.ismailcet.employeemanagement.dto.EmployeeDto;
import com.ismailcet.employeemanagement.dto.request.ChangePasswordRequest;
import com.ismailcet.employeemanagement.dto.request.CreateEmployeeRequest;
import com.ismailcet.employeemanagement.dto.request.LoginEmployeeRequest;
import com.ismailcet.employeemanagement.dto.request.UpdateEmployeeRequest;
import com.ismailcet.employeemanagement.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {


    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginEmployeeRequest loginEmployeeRequest){
        return ResponseEntity.ok(employeeService.login(loginEmployeeRequest));
    }
    @PostMapping("/addEmployee")
    public ResponseEntity<String> createEmployee(@Valid @RequestBody CreateEmployeeRequest createEmployeeRequest){
        return new ResponseEntity<>(
                employeeService.createEmployee(createEmployeeRequest),HttpStatus.CREATED
        );
    }
    @PostMapping("/addManager")
    public ResponseEntity<String> createManager(CreateEmployeeRequest createEmployeeRequest){
        return new ResponseEntity<>(
                employeeService.createManager(createEmployeeRequest),
                HttpStatus.CREATED
        );
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable Integer id){
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/employeeDetails")
    public ResponseEntity<EmployeeDto> getEmployeeDetailFromToken(){
        return ResponseEntity.ok( employeeService.getEmployeeDetailFromToken());
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Integer id){
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeDto> updateEmployeeById(@RequestBody UpdateEmployeeRequest updateEmployeeRequest, @PathVariable Integer id){
        return new ResponseEntity<>(employeeService.updateEmployeeById(updateEmployeeRequest, id), HttpStatus.OK);
    }
    @PutMapping("/update/password/{id}")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @PathVariable Integer id){
        return ResponseEntity.ok(employeeService.changePassword(changePasswordRequest, id));
    }
    @PutMapping("/update/change/type/{id}")
    public ResponseEntity<String> changeEmployeeRoleByEmployeeId(@RequestBody String role,@PathVariable Integer id){
        return ResponseEntity.ok(employeeService.changeEmployeeRoleByEmployeeId(role,id));
    }
}
