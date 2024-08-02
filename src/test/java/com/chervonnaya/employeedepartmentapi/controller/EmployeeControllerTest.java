package com.chervonnaya.employeedepartmentapi.controller;

import com.chervonnaya.employeedepartmentapi.TestJwtUtil;
import com.chervonnaya.employeedepartmentapi.model.Employee;
import com.chervonnaya.employeedepartmentapi.model.EmployeeProjection;
import com.chervonnaya.employeedepartmentapi.service.impl.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestJwtUtil jwtUtil;

    @MockBean
    private EmployeeServiceImpl serviceMock;

    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "moderator@example.com", "admin@example.com"})
    void getEmployee_Should_SucceedWithValidToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        when(serviceMock.getById(any())).thenReturn(employee1);

        mockMvc.perform(get(employeeURL + "/1")
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.position").value("Developer"))
            .andExpect(jsonPath("$.salary").value("5000.0"))
            .andExpect(jsonPath("$.department.name").value("Backend"));
    }

    @Test
    void getEmployee_Should_BeForbiddenWithoutToken() throws Exception {
        when(serviceMock.getById(any())).thenReturn(employee1);

        mockMvc.perform(get(employeeURL + "/1"))
            .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "moderator@example.com", "admin@example.com"})
    void getEmployeeProjection_Should_SucceedWithValidToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        when(serviceMock.getEmployeeProjectionById(any())).thenReturn(employeeProjection1);

        mockMvc.perform(get(employeeProjectionURL + "/1")
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fullName").value("John Doe"))
            .andExpect(jsonPath("$.position").value("Developer"))
            .andExpect(jsonPath("$.departmentName").value("Backend"));
    }

    @Test
    void getEmployeeProjection_Should_BeForbiddenWithoutToken() throws Exception {
        when(serviceMock.getEmployeeProjectionById(any())).thenReturn(employeeProjection1);

        mockMvc.perform(get(employeeProjectionURL + "/1"))
            .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "moderator@example.com", "admin@example.com"})
    void getAllEmployees_Should_SucceedWithValidToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        Page<Employee> employeesPage = new PageImpl<>(List.of(employee1, employee2),
            PageRequest.of(0, 10), 2);

        when(serviceMock.findAll(PageRequest.of(0, 10))).thenReturn(employeesPage);

        mockMvc.perform(get(employeeURL)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void getAllEmployees_Should_BeForbiddenWithoutToken() throws Exception {
        Page<Employee> employeesPage = new PageImpl<>(List.of(employee1, employee2),
            PageRequest.of(0, 10), 2);

        when(serviceMock.findAll(PageRequest.of(0, 10))).thenReturn(employeesPage);

        mockMvc.perform(get(employeeURL)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "moderator@example.com", "admin@example.com"})
    void getAllEmployeeProjections_Should_SucceedWithValidToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        Page<EmployeeProjection> employeesPage = new PageImpl<>(List.of(employeeProjection1, employeeProjection2),
            PageRequest.of(0, 10), 2);

        when(serviceMock.findAllEmployeeProjections(PageRequest.of(0, 10)))
            .thenReturn(employeesPage);

        mockMvc.perform(get(employeeProjectionURL)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2));
    }


    @ParameterizedTest
    @ValueSource(strings = {"moderator@example.com", "admin@example.com"})
    void postEmployee_Should_SucceedWithValidToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        when(serviceMock.save(any())).thenReturn(employee1);

        mockMvc.perform(post(employeeURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDTO))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk());
    }


    @Test
    void postEmployee_Should_BeForbiddenWithUserToken() throws Exception {
        String validToken = jwtUtil.generateToken("user@example.com");
        when(serviceMock.save(any())).thenReturn(employee1);

        mockMvc.perform(post(employeeURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDTO))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isForbidden());
    }


    @ParameterizedTest
    @ValueSource(strings = {"moderator@example.com", "admin@example.com"})
    void putEmployee_Should_SucceedWithModerToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        when(serviceMock.save(any())).thenReturn(employee1);

        mockMvc.perform(put(employeeURL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDTO))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk());
    }


    @Test
    void deleteEmployee_Should_SucceedWithAdminToken() throws Exception {
        String validToken = jwtUtil.generateToken("admin@example.com");
        when(serviceMock.delete(any())).thenReturn(1L);

        mockMvc.perform(delete(employeeURL + "/1")
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "moderator@example.com"})
    void deleteEmployee_Should_BeForbiddenWithNoAdminToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        when(serviceMock.delete(any())).thenReturn(1L);

        mockMvc.perform(delete(employeeURL + "/1")
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isForbidden());
    }
}

