package com.ismailcet.employeemanagement.JWT;

import com.ismailcet.employeemanagement.entity.Employee;
import com.ismailcet.employeemanagement.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class EmployeeDetailsService implements UserDetailsService {

    final EmployeeRepository employeeRepository;

    public EmployeeDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private com.ismailcet.employeemanagement.entity.Employee employeeDetail;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadEmployeeByTc {}", username);
        employeeDetail = employeeRepository
                .findByTc(username);
        if(!Objects.isNull(employeeDetail)){
            return new User(employeeDetail.getTc(),employeeDetail.getPassword(),new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("Employee Not Found . ");
        }
    }

    public com.ismailcet.employeemanagement.entity.Employee getEmployeeDetail(){
        return employeeDetail;
    }
}
