package com.chervonnaya.employeedepartmentapi.repository;


import com.chervonnaya.employeedepartmentapi.model.Employee;
import com.chervonnaya.employeedepartmentapi.model.EmployeeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT CONCAT(e.firstName, ' ', e.lastName) AS fullName, e.position AS position, e.department.name AS departmentName FROM Employee e")
    Page<EmployeeProjection> findEmployeeProjection(Pageable pageable);

    @Query("SELECT CONCAT(e.firstName, ' ', e.lastName) AS fullName, e.position AS position, e.department.name AS departmentName FROM Employee e WHERE e.id = :id")
    EmployeeProjection findAllEmployeeProjectionById(@Param("id") Long id);
}
