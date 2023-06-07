package com.ismailcet.employeemanagement.repository;

import com.ismailcet.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findByTc(@Param("tc") String tc);

    @Query(
            value = "Select * FROM EMPLOYEE e WHERE e.tc =?1 ",
            nativeQuery = true
    )
    Employee findByTcNumber(String tc);

    Employee findByResetPasswordToken(String token);
}
