package com.chervonnaya.employeedepartmentapi.controller;


import com.chervonnaya.employeedepartmentapi.TestJwtUtil;
import com.chervonnaya.employeedepartmentapi.model.Department;
import com.chervonnaya.employeedepartmentapi.service.impl.DepartmentServiceImpl;
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


import static com.chervonnaya.employeedepartmentapi.TestData.department1;
import static com.chervonnaya.employeedepartmentapi.TestData.department2;
import static com.chervonnaya.employeedepartmentapi.TestData.departmentDTO;
import static com.chervonnaya.employeedepartmentapi.TestData.departmentURL;
import static com.chervonnaya.employeedepartmentapi.TestData.emptyDepartment;
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
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestJwtUtil jwtUtil;

    @MockBean
    private DepartmentServiceImpl serviceMock;



    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "moderator@example.com", "admin@example.com"})
    void getDepartment_Should_SucceedWithValidToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        when(serviceMock.getById(any())).thenReturn(department1);

        mockMvc.perform(get(departmentURL + "/1")
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Backend"));
    }


    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "moderator@example.com", "admin@example.com"})
    void getAllDepartments_Should_SucceedWithValidToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        Page<Department> departmentsPage = new PageImpl<>(List.of(department1, department2), PageRequest.of(0, 10), 2);

        when(serviceMock.findAll(PageRequest.of(0, 10))).thenReturn(departmentsPage);

        mockMvc.perform(get(departmentURL)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2));
    }


    @ParameterizedTest
    @ValueSource(strings = {"moderator@example.com", "admin@example.com"})
    void postDepartment_Should_SucceedWithModerToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        when(serviceMock.save(any())).thenReturn(department1);

        mockMvc.perform(post(departmentURL)
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(departmentDTO)))
            .andExpect(status().isOk());
    }


    @Test
    void postDepartment_Should_BeForbiddenWithUserToken() throws Exception {
        String validToken = jwtUtil.generateToken("user@example.com");
        when(serviceMock.save(any())).thenReturn(department1);

        mockMvc.perform(post(departmentURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(departmentDTO))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isForbidden());
    }

    @Test
    void postDepartmentEmptyJson_Should_ReturnBadRequest() throws Exception {
        String validToken = jwtUtil.generateToken("admin@example.com");
        mockMvc.perform(post(departmentURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(emptyDepartment))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Please provide a department name")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"moderator@example.com", "admin@example.com"})
    void putDepartment_Should_SucceedWithModerToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        when(serviceMock.save(any())).thenReturn(department1);

        mockMvc.perform(put(departmentURL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(departmentDTO))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk());
    }


    @Test
    void putDepartment_Should_BeForbiddenWithoutToken() throws Exception {
        when(serviceMock.save(any())).thenReturn(department1);

        mockMvc.perform(put(departmentURL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(departmentDTO)))
            .andExpect(status().isForbidden());
    }

    @Test
    void putDepartmentEmptyJson_Should_ReturnBadRequest() throws Exception {
        String validToken = jwtUtil.generateToken("admin@example.com");
        mockMvc.perform(put(departmentURL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(emptyDepartment))
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("Please provide a department name")));
    }

    @Test
    void deleteDepartment_Should_SucceedWithAdminToken() throws Exception {
        String validToken = jwtUtil.generateToken("admin@example.com");
        when(serviceMock.delete(any())).thenReturn(1L);

        mockMvc.perform(delete(departmentURL + "/1")
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isOk());
    }

    @Test
    void deleteDepartment_Should_BeForbiddenWithoutToken() throws Exception {
        when(serviceMock.delete(any())).thenReturn(1L);

        mockMvc.perform(delete(departmentURL + "/1"))
            .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "moderator@example.com"})
    void deleteDepartment_Should_BeForbiddenWithNoAdminToken(String email) throws Exception {
        String validToken = jwtUtil.generateToken(email);
        when(serviceMock.delete(any())).thenReturn(1L);

        mockMvc.perform(delete(departmentURL + "/1")
                .header("Authorization", "Bearer " + validToken))
            .andExpect(status().isForbidden());
    }

}
