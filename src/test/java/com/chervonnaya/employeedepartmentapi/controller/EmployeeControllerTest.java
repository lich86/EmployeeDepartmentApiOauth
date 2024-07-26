package com.chervonnaya.employeedepartmentapi.controller;

import com.chervonnaya.employeedepartmentapi.model.Employee;
import com.chervonnaya.employeedepartmentapi.model.EmployeeProjection;
import com.chervonnaya.employeedepartmentapi.service.impl.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.chervonnaya.employeedepartmentapi.TestData.employee1;
import static com.chervonnaya.employeedepartmentapi.TestData.employee2;
import static com.chervonnaya.employeedepartmentapi.TestData.employeeDTO;
import static com.chervonnaya.employeedepartmentapi.TestData.employeeProjection1;
import static com.chervonnaya.employeedepartmentapi.TestData.employeeProjection2;
import static com.chervonnaya.employeedepartmentapi.TestData.employeeProjectionURL;
import static com.chervonnaya.employeedepartmentapi.TestData.employeeURL;
import static com.chervonnaya.employeedepartmentapi.TestData.emptyEmployee;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeServiceImpl serviceMock;

    @Test
    void getEmployee_Should_Succeed() throws Exception {
        when(serviceMock.getById(any())).thenReturn(employee1);

        mockMvc.perform(get(employeeURL + "/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.position").value("Developer"))
            .andExpect(jsonPath("$.salary").value("5000.0"))
            .andExpect(jsonPath("$.department.name").value("Backend"));
    }

    @Test
    void getEmployeeProjection_Should_Succeed() throws Exception {
        when(serviceMock.getEmployeeProjectionById(any())).thenReturn(employeeProjection1);

        mockMvc.perform(get(employeeProjectionURL + "/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fullName").value("John Doe"))
            .andExpect(jsonPath("$.position").value("Developer"))
            .andExpect(jsonPath("$.departmentName").value("Backend"));
    }

    @Test
    void getAllEmployees_Should_Succeed() throws Exception {
        Page<Employee> employeesPage = new PageImpl<>(List.of(employee1, employee2), PageRequest.of(0, 10), 2);

        when(serviceMock.findAll(PageRequest.of(0, 10))).thenReturn(employeesPage);

        mockMvc.perform(get(employeeURL)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void getAllEmployeeProjections_Should_Succeed() throws Exception {
        Page<EmployeeProjection> employeesPage = new PageImpl<>(List.of(employeeProjection1, employeeProjection2), PageRequest.of(0, 10), 2);

        when(serviceMock.findAllEmployeeProjections(PageRequest.of(0, 10))).thenReturn(employeesPage);

        mockMvc.perform(get(employeeProjectionURL)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void postEmployee_Should_Succeed() throws Exception {
        when(serviceMock.save(any())).thenReturn(employee1);

        mockMvc.perform(post(employeeURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDTO)))
            .andExpect(status().isOk());
    }

    @Test
    void postEmployeeEmptyJson_Should_Fail() throws Exception {
        mockMvc.perform(post(employeeURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(emptyEmployee)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Please provide the first name")))
            .andExpect(jsonPath("$.message", containsString("Please provide the last name")))
            .andExpect(jsonPath("$.message", containsString("Please provide a position")))
            .andExpect(jsonPath("$.message", containsString("Please provide a salary")))
            .andExpect(jsonPath("$.message", containsString("Please provide department id")));
    }


    @Test
    void putEmployee_Should_Succeed() throws Exception {
        when(serviceMock.save(any())).thenReturn(employee1);

        mockMvc.perform(put(employeeURL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDTO)))
            .andExpect(status().isOk());
    }

    @Test
    void putEmployee_Should_Fail() throws Exception {
        mockMvc.perform(put(employeeURL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(emptyEmployee)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Please provide the first name")))
            .andExpect(jsonPath("$.message", containsString("Please provide the last name")))
            .andExpect(jsonPath("$.message", containsString("Please provide a position")))
            .andExpect(jsonPath("$.message", containsString("Please provide a salary")))
            .andExpect(jsonPath("$.message", containsString("Please provide department id")));
    }

    @Test
    void deleteEmployee_Should_Succeed() throws Exception {
        when(serviceMock.delete(any())).thenReturn(1L);

        mockMvc.perform(delete(employeeURL + "/1"))
            .andExpect(status().isOk());
    }
}

