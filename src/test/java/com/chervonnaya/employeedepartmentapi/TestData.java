package com.chervonnaya.employeedepartmentapi;

import com.chervonnaya.employeedepartmentapi.dto.DepartmentDTO;
import com.chervonnaya.employeedepartmentapi.dto.EmployeeDTO;
import com.chervonnaya.employeedepartmentapi.model.Department;
import com.chervonnaya.employeedepartmentapi.model.Employee;
import com.chervonnaya.employeedepartmentapi.model.EmployeeProjection;

import java.math.BigDecimal;

public class TestData {

    public static final Department department1 = new Department();
    public static final Department department2 = new Department();
    public static final Employee employee1 = new Employee();
    public static final Employee employee2 = new Employee();
    public static final DepartmentDTO departmentDTO = new DepartmentDTO();
    public static final EmployeeDTO employeeDTO = new EmployeeDTO();
    public static final Department emptyDepartment = new Department();
    public static final EmployeeDTO emptyEmployee = new EmployeeDTO();
    public static final String departmentURL = "/api/department";
    public static final String employeeURL = "/api/employee";
    public static final String departmentProjectionURL = "/api/department/summary";
    public static final String employeeProjectionURL = "/api/employee/summary";
    public static final EmployeeProjection employeeProjection1 = new EmployeeProjection() {
        @Override
        public String getFullName() {
            return "John Doe";
        }

        @Override
        public String getPosition() {
            return "Developer";
        }

        @Override
        public String getDepartmentName() {
            return "Backend";
        }
    };

    public static final EmployeeProjection employeeProjection2 = new EmployeeProjection() {
        @Override
        public String getFullName() {
            return "Jane Doe";
        }

        @Override
        public String getPosition() {
            return "Developer";
        }

        @Override
        public String getDepartmentName() {
            return "Backend";
        }
    };

    static {
        department1.setName("Backend");
        department2.setName("Frontend");

        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setPosition("Developer");
        employee1.setSalary(new BigDecimal("5000.00"));
        employee1.setDepartment(department1);

        employee2.setFirstName("Jane");
        employee2.setLastName("Doe");
        employee2.setPosition("Developer");
        employee2.setSalary(new BigDecimal("5000.00"));
        employee2.setDepartment(department1);

        departmentDTO.setName("Backend");

        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setPosition("Developer");
        employeeDTO.setSalary(new BigDecimal("5000.00"));
        employeeDTO.setDepartmentId(1L);

    }
}
