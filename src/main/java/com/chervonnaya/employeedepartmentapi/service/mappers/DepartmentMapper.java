package com.chervonnaya.employeedepartmentapi.service.mappers;

import com.chervonnaya.employeedepartmentapi.dto.DepartmentDTO;
import com.chervonnaya.employeedepartmentapi.model.Department;
import com.chervonnaya.employeedepartmentapi.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DepartmentMapper extends BaseMapper<Department, DepartmentDTO> {

    @Mapping(source = "employeeIds", target = "employees", qualifiedByName = "mapEmployees")
    Department map(DepartmentDTO dto);

    @Named("mapEmployees")
    static Set<Employee> mapEmployees(Set<Long> ids) {
        if(ids == null || ids.isEmpty()) {
            return null;
        }
        return ids.stream().map(id -> {
            Employee employee = new Employee();
            employee.setId(id);
            return employee;
        }).collect(Collectors.toSet());
    }
}
