package com.ismailcet.employeemanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequest {

    private String name;
    private String surname;
    private Integer age;
    private Double salary;
    private String phone;
    private Integer departmentId;
    private Integer positionId;
}
