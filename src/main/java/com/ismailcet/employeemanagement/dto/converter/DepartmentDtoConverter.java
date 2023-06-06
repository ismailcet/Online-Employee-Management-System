package com.ismailcet.employeemanagement.dto.converter;

import com.ismailcet.employeemanagement.dto.DepartmentDto;
import com.ismailcet.employeemanagement.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentDtoConverter {

    public DepartmentDto convert(Department department){
        return DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }

}
