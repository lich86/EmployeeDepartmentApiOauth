package com.chervonnaya.employeedepartmentapi.repository;

import com.chervonnaya.employeedepartmentapi.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
