package com.ismailcet.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private String name;
    private String surname;
    private String tc;
    private Integer age;
    private Double salary;
    private String phone;
    private String type;
    private String departmentName;
    private String positionName;

}
