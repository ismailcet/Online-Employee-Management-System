package com.ismailcet.employeemanagement.controller;

import com.ismailcet.employeemanagement.dto.DepartmentDto;
import com.ismailcet.employeemanagement.dto.PositionDto;
import com.ismailcet.employeemanagement.dto.request.CreateDepartmentRequest;
import com.ismailcet.employeemanagement.dto.request.CreatePositionRequest;
import com.ismailcet.employeemanagement.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping()
    public ResponseEntity<DepartmentDto> createPosition(@RequestBody CreateDepartmentRequest createDepartmentRequest){
        return new ResponseEntity<>(
                departmentService.createPosition(createDepartmentRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updatePositionById(@RequestBody CreateDepartmentRequest createDepartmentRequest, @PathVariable Integer id){
        return ResponseEntity.ok(departmentService.updatePositionById(createDepartmentRequest, id));
    }
    @GetMapping()
    public ResponseEntity<List<DepartmentDto>> getAllPosition(){
        return ResponseEntity.ok(departmentService.getAllPosition());
    }
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getPositionById(@PathVariable Integer id){
        return ResponseEntity.ok(departmentService.getPositionById(id));
    }
}
