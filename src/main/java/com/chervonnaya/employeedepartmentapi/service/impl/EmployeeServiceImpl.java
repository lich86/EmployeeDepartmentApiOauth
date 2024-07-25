package com.chervonnaya.employeedepartmentapi.service.impl;

import com.chervonnaya.employeedepartmentapi.dto.EmployeeDTO;
import com.chervonnaya.employeedepartmentapi.model.Employee;
import com.chervonnaya.employeedepartmentapi.model.EmployeeProjection;
import com.chervonnaya.employeedepartmentapi.repository.EmployeeRepository;
import com.chervonnaya.employeedepartmentapi.service.mappers.BaseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl extends CrudServiceImpl<Employee, EmployeeDTO, EmployeeRepository> {

    public EmployeeServiceImpl(EmployeeRepository repository, BaseMapper<Employee, EmployeeDTO> mapper) {
        super(repository, Employee.class, mapper);
    }

    public EmployeeProjection getEmployeeProjection(Long id) {
        return repository.findAllEmployeeProjectionById(id);
    }

    public Page<EmployeeProjection> getAllEmployeeProjections(Pageable pageable) {
        return repository.findEmployeeProjection(pageable);
    }
}
