package com.chervonnaya.employeedepartmentapi.service;

import com.chervonnaya.employeedepartmentapi.model.Department;
import com.chervonnaya.employeedepartmentapi.repository.DepartmentRepository;
import com.chervonnaya.employeedepartmentapi.service.impl.DepartmentServiceImpl;
import com.chervonnaya.employeedepartmentapi.service.mappers.DepartmentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.Optional;

import static com.chervonnaya.employeedepartmentapi.TestData.department1;
import static com.chervonnaya.employeedepartmentapi.TestData.department2;
import static com.chervonnaya.employeedepartmentapi.TestData.departmentDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper mapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void getById_Should_Succeed() {
        when(departmentRepository.findById(any())).thenReturn(Optional.of(department1));

        assertEquals(department1, departmentService.getById(1L));
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void getAll_Should_Succeed() {
        Page<Department> departmentsPage = new PageImpl<>(List.of(department1, department2), PageRequest.of(0, 10), 2);
        when(departmentRepository.findAll(PageRequest.of(0, 10))).thenReturn(departmentsPage);

        assertEquals(departmentsPage, departmentService.findAll(PageRequest.of(0, 10)));
        verify(departmentRepository, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    void save_Should_Succeed() {
        when(mapper.map(departmentDTO)).thenReturn(department1);
        when(departmentRepository.save(any())).thenReturn(department1);

        departmentService.save(departmentDTO);

        verify(departmentRepository, times(1)).save(any());
    }

    @Test
    void update_Should_Succeed() {
        when(departmentRepository.findById(any())).thenReturn(Optional.of(department1));
        when(mapper.map(departmentDTO)).thenReturn(department1);
        when(departmentRepository.save(any())).thenReturn(department1);

        departmentService.update(1L, departmentDTO);

        verify(departmentRepository, times(1)).save(any());
    }

    @Test
    void delete_Should_Succeed() {
        department1.setId(1L);
        doNothing().when(departmentRepository).deleteById(department1.getId());

        departmentService.delete(department1.getId());

        verify(departmentRepository, times(1)).deleteById(department1.getId());
    }
}

