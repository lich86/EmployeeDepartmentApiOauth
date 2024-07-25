package com.chervonnaya.employeedepartmentapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO extends BaseDTO{

    private String name;
    private List<Long> employeeIds;

}
