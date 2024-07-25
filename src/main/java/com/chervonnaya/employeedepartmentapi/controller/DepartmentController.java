package com.chervonnaya.employeedepartmentapi.controller;


import com.chervonnaya.employeedepartmentapi.dto.DepartmentDTO;
import com.chervonnaya.employeedepartmentapi.model.Department;
import com.chervonnaya.employeedepartmentapi.service.impl.DepartmentServiceImpl;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentServiceImpl departmentService;

    @Autowired
    public DepartmentController(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping(value = "/{id}")
    public Department getDepartment(@PathVariable(name = "id") Long id) {
        return departmentService.getById(id);
    }

    @GetMapping
    public Page<Department> getDepartments(Pageable pageable) {
        return departmentService.findAll(pageable);
    }

    @PostMapping
    public Department createDepartment(@Valid @RequestBody DepartmentDTO dto) {
        return departmentService.save(dto);
    }

    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable(name = "id") Long id, @Valid @RequestBody DepartmentDTO dto) {
        return departmentService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public Long deleteDepartment(@PathVariable(name = "id") Long id) {
        return departmentService.delete(id);
    }
}

