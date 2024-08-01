package com.chervonnaya.employeedepartmentapi.controller;

import com.chervonnaya.employeedepartmentapi.TestJwtUtil;
import com.chervonnaya.employeedepartmentapi.model.Employee;
import com.chervonnaya.employeedepartmentapi.model.EmployeeProjection;
import com.chervonnaya.employeedepartmentapi.service.impl.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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

    @Test
    void getEmployee_Should_SucceedWithUserToken() throws Exception {
        String validToken = jwtUtil.generateUserToken();
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
    void getEmployee_Should_SucceedWithModerToken() throws Exception {
        String validToken = jwtUtil.generateModeratorToken();
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
    void getEmployee_Should_SucceedWithAdminToken() throws Exception {
        String validToken = jwtUtil.generateAdminToken();
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

    @Test
    void getEmployeeProjection_Should_SucceedWithUserToken() throws Exception {
        String validToken = jwtUtil.generateUserToken();
        when(serviceMock.getEmployeeProjectionById(any())).thenReturn(employeeProjection1);

        mockMvc.perform(get(employeeProjectionURL + "/1")
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fullName").value("John Doe"))
            .andExpect(jsonPath("$.position").value("Developer"))
            .andExpect(jsonPath("$.departmentName").value("Backend"));
    }

    @Test
    void getEmployeeProjection_Should_SucceedWithModerToken() throws Exception {
        String validToken = jwtUtil.generateModeratorToken();
        when(serviceMock.getEmployeeProjectionById(any())).thenReturn(employeeProjection1);

        mockMvc.perform(get(employeeProjectionURL + "/1")
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fullName").value("John Doe"))
            .andExpect(jsonPath("$.position").value("Developer"))
            .andExpect(jsonPath("$.departmentName").value("Backend"));
    }

    @Test
    void getEmployeeProjection_Should_SucceedWithAdminToken() throws Exception {
        String validToken = jwtUtil.generateAdminToken();
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

    @Test
    void getAllEmployees_Should_SucceedWithUserToken() throws Exception {
        String validToken = jwtUtil.generateUserToken();
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
    void getAllEmployees_Should_SucceedWithModerToken() throws Exception {
        String validToken = jwtUtil.generateModeratorToken();
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
    void getAllEmployees_Should_SucceedWithAdminToken() throws Exception {
        String validToken = jwtUtil.generateAdminToken();
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

    @Test
    void getAllEmployeeProjections_Should_SucceedWithUserToken() throws Exception {
        String validToken = jwtUtil.generateUserToken();
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

    @Test
    void getAllEmployeeProjections_Should_SucceedWithModerToken() throws Exception {
        String validToken = jwtUtil.generateModeratorToken();
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

    @Test
    void getAllEmployeeProjections_Should_SucceedWithAdminToken() throws Exception {
        String validToken = jwtUtil.generateAdminToken();
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

    @Test
    void postEmployee_Should_SucceedWithModerToken() throws Exception {
        String validToken = jwtUtil.generateModeratorToken();
        when(serviceMock.save(any())).thenReturn(employee1);

        mockMvc.perform(post(employeeURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDTO))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk());
    }

    @Test
    void postEmployeeEmptyJson_Should_ReturnBadRequest() throws Exception {
        String validToken = jwtUtil.generateAdminToken();
        mockMvc.perform(post(employeeURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(emptyEmployee))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Please provide the first name")))
            .andExpect(jsonPath("$.message", containsString("Please provide the last name")))
            .andExpect(jsonPath("$.message", containsString("Please provide a position")))
            .andExpect(jsonPath("$.message", containsString("Please provide a salary")))
            .andExpect(jsonPath("$.message", containsString("Please provide department id")));
    }

    @Test
    void postEmployee_Should_BeForbiddenWithUserToken() throws Exception {
        String validToken = jwtUtil.generateUserToken();
        when(serviceMock.save(any())).thenReturn(employee1);

        mockMvc.perform(post(employeeURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDTO))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isForbidden());
    }


    @Test
    void putEmployee_Should_SucceedWithModerToken() throws Exception {
        String validToken = jwtUtil.generateModeratorToken();
        when(serviceMock.save(any())).thenReturn(employee1);

        mockMvc.perform(put(employeeURL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDTO))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"MODERATOR"})
    void putEmployee_Should_ReturnBadRequest() throws Exception {
        String validToken = jwtUtil.generateAdminToken();
        mockMvc.perform(put(employeeURL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(emptyEmployee))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Please provide the first name")))
            .andExpect(jsonPath("$.message", containsString("Please provide the last name")))
            .andExpect(jsonPath("$.message", containsString("Please provide a position")))
            .andExpect(jsonPath("$.message", containsString("Please provide a salary")))
            .andExpect(jsonPath("$.message", containsString("Please provide department id")));
    }

    @Test
    void deleteEmployee_Should_SucceedWithAdminToken() throws Exception {
        String validToken = jwtUtil.generateAdminToken();
        when(serviceMock.delete(any())).thenReturn(1L);

        mockMvc.perform(delete(employeeURL + "/1")
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk());
    }
}

