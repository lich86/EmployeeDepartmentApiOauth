package com.chervonnaya.employeedepartmentapi.controller;

import com.chervonnaya.employeedepartmentapi.dto.EmployeeDTO;
import com.chervonnaya.employeedepartmentapi.model.Employee;
import com.chervonnaya.employeedepartmentapi.model.EmployeeProjection;
import com.chervonnaya.employeedepartmentapi.service.impl.EmployeeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    @Autowired
    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/{id}")
    public Employee getEmployee(@PathVariable(name = "id") Long id) {
        return employeeService.getById(id);
    }

    @GetMapping(value = "/summary/{id}")
    public EmployeeProjection getEmployeeProjection(@PathVariable(name = "id") Long id) {
        return employeeService.getEmployeeProjection(id);
    }

    @GetMapping
    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeService.findAll(pageable);
    }

    @GetMapping(value = "/summary")
    public Page<EmployeeProjection> getEmployeeProjections(Pageable pageable) {
        return employeeService.getAllEmployeeProjections(pageable);
    }

    @PostMapping
    public Employee createEmployee(@Valid @RequestBody EmployeeDTO dto) {
        return employeeService.save(dto);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable(name = "id") Long id, @Valid @RequestBody EmployeeDTO dto) {
        return employeeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public Long deleteEmployee(@PathVariable(name = "id") Long id) {
        return employeeService.delete(id);
    }
}

