package com.chervonnaya.employeedepartmentapi.service;

import com.chervonnaya.employeedepartmentapi.model.Employee;
import com.chervonnaya.employeedepartmentapi.repository.EmployeeRepository;
import com.chervonnaya.employeedepartmentapi.service.impl.EmployeeServiceImpl;
import com.chervonnaya.employeedepartmentapi.service.mappers.EmployeeMapper;
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

import static com.chervonnaya.employeedepartmentapi.TestData.employee1;
import static com.chervonnaya.employeedepartmentapi.TestData.employee2;
import static com.chervonnaya.employeedepartmentapi.TestData.employeeDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper mapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getById_Should_Succeed() {
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee1));

        assertEquals(employee1, employeeService.getById(1L));
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getAll_Should_Succeed() {
        Page<Employee> employeesPage = new PageImpl<>(List.of(employee1, employee2), PageRequest.of(0, 10), 2);
        when(employeeRepository.findAll(PageRequest.of(0, 10))).thenReturn(employeesPage);

        assertEquals(employeesPage, employeeService.findAll(PageRequest.of(0, 10)));
        verify(employeeRepository, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    void save_Should_Succeed() {
        when(mapper.map(employeeDTO)).thenReturn(employee1);
        when(employeeRepository.save(any())).thenReturn(employee1);

        employeeService.save(employeeDTO);

        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    void update_Should_Succeed() {
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee1));
        when(mapper.map(employeeDTO)).thenReturn(employee1);
        when(employeeRepository.save(any())).thenReturn(employee1);

        employeeService.update(1L, employeeDTO);

        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    void delete_Should_Succeed() {
        employee1.setId(1L);
        doNothing().when(employeeRepository).deleteById(employee1.getId());

        employeeService.delete(employee1.getId());

        verify(employeeRepository, times(1)).deleteById(employee1.getId());
    }
}


