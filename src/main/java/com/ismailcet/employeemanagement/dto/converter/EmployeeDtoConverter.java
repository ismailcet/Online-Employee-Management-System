package com.ismailcet.employeemanagement.dto.converter;

import com.ismailcet.employeemanagement.dto.EmployeeDto;
import com.ismailcet.employeemanagement.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDtoConverter {


    public EmployeeDto convert(Employee employee){
        return EmployeeDto.builder()
                .name(employee.getName())
                .surname(employee.getSurname())
                .tc(employee.getTc())
                .email(employee.getEmail())
                .age(employee.getAge())
                .salary(employee.getSalary())
                .phone(employee.getPhone())
                .type(employee.getType())
                .departmentName(employee.getDepartment().getName())
                .positionName(employee.getPosition().getName())
                .build();
    }

}
