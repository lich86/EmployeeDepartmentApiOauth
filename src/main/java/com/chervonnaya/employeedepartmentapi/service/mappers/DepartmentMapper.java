package com.chervonnaya.employeedepartmentapi.service.mappers;

import com.chervonnaya.employeedepartmentapi.dto.DepartmentDTO;
import com.chervonnaya.employeedepartmentapi.dto.EmployeeDTO;
import com.chervonnaya.employeedepartmentapi.model.Department;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

public interface DepartmentMapper extends BaseMapper<Department, DepartmentDTO> {

    @Mapping(source = "employeeIds", target = "employees", qualifiedByName = "mapEmployees")
    Department map(DepartmentDTO dto);

    @Named("mapEmployees")
    static List<Department> mapEmployees(List<EmployeeDTO> dtos) {
        return dtos.stream().map(dto -> {
            Department department = new Department();
            department.setId(dto.getId());
            return department;
        }).toList();
    }
}
