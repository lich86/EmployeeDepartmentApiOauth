package com.chervonnaya.employeedepartmentapi.service.impl;

import com.chervonnaya.employeedepartmentapi.dto.DepartmentDTO;
import com.chervonnaya.employeedepartmentapi.model.Department;
import com.chervonnaya.employeedepartmentapi.repository.DepartmentRepository;
import com.chervonnaya.employeedepartmentapi.service.mappers.BaseMapper;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends CrudServiceImpl<Department, DepartmentDTO, DepartmentRepository> {

    public DepartmentServiceImpl(DepartmentRepository repository, BaseMapper mapper) {
        super(repository, Department.class, mapper);
    }
}
