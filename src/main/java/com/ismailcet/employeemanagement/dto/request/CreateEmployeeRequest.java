package com.ismailcet.employeemanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeRequest {
    private String tc;
    private String name;
    private String surname;
    private String password;
    private String email;
    private Integer age;
    private Double salary;
    private String phone;
    private String type;
    private Integer departmentId;
    private Integer positionId;
}
