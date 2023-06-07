package com.ismailcet.employeemanagement.service;

import com.ismailcet.employeemanagement.JWT.JwtFilter;
import com.ismailcet.employeemanagement.dto.DepartmentDto;
import com.ismailcet.employeemanagement.dto.PositionDto;
import com.ismailcet.employeemanagement.dto.converter.DepartmentDtoConverter;
import com.ismailcet.employeemanagement.dto.request.CreateDepartmentRequest;
import com.ismailcet.employeemanagement.entity.Department;
import com.ismailcet.employeemanagement.entity.Position;
import com.ismailcet.employeemanagement.exception.AuthenticationNotFoundException;
import com.ismailcet.employeemanagement.exception.PositionNotFoundException;
import com.ismailcet.employeemanagement.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final JwtFilter jwtFilter;
    private final DepartmentDtoConverter departmentDtoConverter;
    public DepartmentService(DepartmentRepository departmentRepository, JwtFilter jwtFilter, DepartmentDtoConverter departmentDtoConverter) {
        this.departmentRepository = departmentRepository;
        this.jwtFilter = jwtFilter;
        this.departmentDtoConverter = departmentDtoConverter;
    }

    public DepartmentDto createPosition(CreateDepartmentRequest createDepartmentRequest) {
        try{
            if(jwtFilter.isSupervisor()){
                Department department = Department.builder()
                        .name(createDepartmentRequest.getName())
                        .build();
                departmentRepository.save(department);
                return departmentDtoConverter.convert(department);
            }else{
                throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }

    }

    public DepartmentDto updatePositionById(CreateDepartmentRequest createDepartmentRequest, Integer id) {
        try{
            Department department  =
                    departmentRepository.findById(id).orElseThrow(()->new PositionNotFoundException("Department Id does not valid ! "));
            if(jwtFilter.isSupervisor()){
                department.setName(createDepartmentRequest.getName());
                departmentRepository.save(department);
                return departmentDtoConverter.convert(department);
            }else{
                throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }

    }

    public List<DepartmentDto> getAllPosition() {
        try{
            if(jwtFilter.isManager() || jwtFilter.isSupervisor()){
                List<DepartmentDto> departments = departmentRepository.findAll()
                        .stream().map(departmentDtoConverter::convert)
                        .collect(Collectors.toList());
                return departments;
            }else{
                throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public DepartmentDto getPositionById(Integer id) {
        try{
            Department department = departmentRepository.findById(id)
                    .orElseThrow(()-> new PositionNotFoundException("Department Id does not valid ! "));

            if(jwtFilter.isManager() || jwtFilter.isSupervisor()){
                return departmentDtoConverter.convert(department);
            }else{
                throw new AuthenticationNotFoundException("Unauthenticated Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }
}
