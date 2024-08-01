package com.chervonnaya.employeedepartmentapi.controller;


import com.chervonnaya.employeedepartmentapi.TestJwtUtil;
import com.chervonnaya.employeedepartmentapi.model.Department;
import com.chervonnaya.employeedepartmentapi.service.impl.DepartmentServiceImpl;
import com.chervonnaya.employeedepartmentapi.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.chervonnaya.employeedepartmentapi.TestData.department1;
import static com.chervonnaya.employeedepartmentapi.TestData.department2;
import static com.chervonnaya.employeedepartmentapi.TestData.departmentDTO;
import static com.chervonnaya.employeedepartmentapi.TestData.departmentURL;
import static com.chervonnaya.employeedepartmentapi.TestData.emptyDepartment;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestJwtUtil jwtUtil;

    @MockBean
    private DepartmentServiceImpl serviceMock;

    private String validToken;


    @Test
    void getDepartment_Should_Succeed() throws Exception {
        when(serviceMock.getById(any())).thenReturn(department1);

        mockMvc.perform(get(departmentURL + "/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Backend"));
    }

    @Test
    void getAllDepartments_Should_Succeed() throws Exception {
        Page<Department> departmentsPage = new PageImpl<>(List.of(department1, department2), PageRequest.of(0, 10), 2);

        when(serviceMock.findAll(PageRequest.of(0, 10))).thenReturn(departmentsPage);

        mockMvc.perform(get(departmentURL)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @WithMockUser(username = "admin2@example.com", roles = {"ADMIN"})
    void postDepartment_Should_Succeed() throws Exception {
        validToken = jwtUtil.generateToken();
        when(serviceMock.save(any())).thenReturn(department1);

        mockMvc.perform(post(departmentURL)
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(departmentDTO)))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"MODERATOR"})
    void postDepartment_Should_BeForbiddenWhenNoRole() throws Exception {
        when(serviceMock.save(any())).thenReturn(department1);

        mockMvc.perform(post(departmentURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(departmentDTO)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"MODERATOR"})
    void postDepartmentEmptyJson_Should_ReturnBadRequest() throws Exception {
        mockMvc.perform(post(departmentURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(emptyDepartment)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Please provide a department name")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"MODERATOR"})
    void putDepartment_Should_Succeed() throws Exception {
        when(serviceMock.save(any())).thenReturn(department1);

        mockMvc.perform(put(departmentURL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(departmentDTO)))
            .andExpect(status().isOk());
    }

    @Test
    void putDepartment_Should_BeForbiddenWhenNoRole() throws Exception {
        when(serviceMock.save(any())).thenReturn(department1);

        mockMvc.perform(put(departmentURL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(departmentDTO)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"MODERATOR"})
    void putDepartment_Should_ReturnBadRequest() throws Exception {
        mockMvc.perform(put(departmentURL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(emptyDepartment)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Please provide a department name")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void deleteDepartment_Should_Succeed() throws Exception {
        when(serviceMock.delete(any())).thenReturn(1L);

        mockMvc.perform(delete(departmentURL + "/1"))
            .andExpect(status().isOk());
    }

    @Test
    void deleteDepartment_Should_BeForbiddenWhenNoRole() throws Exception {
        when(serviceMock.delete(any())).thenReturn(1L);

        mockMvc.perform(delete(departmentURL + "/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"MODERATOR"})
    void deleteDepartment_Should_BeForbiddenWhenNoAdminRole() throws Exception {
        when(serviceMock.delete(any())).thenReturn(1L);

        mockMvc.perform(delete(departmentURL + "/1"))
            .andExpect(status().isForbidden());
    }
}
