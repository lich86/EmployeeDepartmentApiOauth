package com.chervonnaya.employeedepartmentapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO extends BaseDTO{
    @NotNull (message = "{departmentName.NotNull}")
    private String name;
    private Set<Long> employeeIds;

}
