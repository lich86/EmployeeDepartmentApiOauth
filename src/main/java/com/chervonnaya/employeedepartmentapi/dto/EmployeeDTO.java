package com.chervonnaya.employeedepartmentapi.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO extends BaseDTO {
    @NotBlank(message = "{firstName.notBlank}")
    private String firstName;
    @NotBlank(message = "{lastName.notBlank}")
    private String lastName;
    @NotBlank(message = "{position.notBlank}")
    private String position;
    @NotNull(message = "{salary.notNull}")
    @DecimalMin(value = "0.0", message = "{sum.Min}")
    @DecimalMax(value = "9999999999999.99", message = "{sum.Max}")
    private BigDecimal salary;
    @NotNull(message = "{department.notNull}")
    private Long departmentId;
}
