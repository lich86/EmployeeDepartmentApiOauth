package com.chervonnaya.employeedepartmentapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO extends BaseDTO {

    private String firstName;
    private String lastName;
    private String position;
    private BigDecimal salary;
    private Long departmentId;
}
