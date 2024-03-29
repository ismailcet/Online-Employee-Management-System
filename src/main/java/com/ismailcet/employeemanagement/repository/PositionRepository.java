package com.ismailcet.employeemanagement.repository;

import com.ismailcet.employeemanagement.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
}
