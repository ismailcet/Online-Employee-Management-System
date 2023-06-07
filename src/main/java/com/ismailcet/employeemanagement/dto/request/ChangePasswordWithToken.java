package com.ismailcet.employeemanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordWithToken {

    @NotNull
    private String password;
    @NotNull
    private String tc;
    @NotNull
    private String token;
}
