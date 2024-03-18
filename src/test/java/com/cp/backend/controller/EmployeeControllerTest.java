package com.cp.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cp.backend.entity.Employee;
import com.cp.backend.exception.DuplicateResourceException;
import com.cp.backend.exception.ResourceNotFoundException;
import com.cp.backend.repo.EmployeeRepo;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeRepo employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void createEmployee_WithValidEmployee_ReturnsCreatedEmployee() {

        Employee request = new Employee();
        request.setName("John Doe");
        request.setPhoneNumber("1234567890");
        String supervisors = "Supervisor 1";
        request.setSupervisors(supervisors);

        when(employeeRepository.findOneByName("John Doe")).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee createdEmployee = employeeController.createEmployee(request);

        assertNotNull(createdEmployee);
        assertEquals("John Doe", createdEmployee.getName());
        assertEquals("1234567890", createdEmployee.getPhoneNumber());
        assertEquals(supervisors, createdEmployee.getSupervisors());
    }

    @Test
    void createEmployee_WithDuplicateName_ThrowsDuplicateResourceException() {

        Employee request = new Employee();
        request.setName("John Doe");
        request.setPhoneNumber("1234567890");
        String supervisors = "Supervisor 1";
        request.setSupervisors(supervisors);

        when(employeeRepository.findOneByName("John Doe")).thenReturn(Optional.of(new Employee()));

        assertThrows(DuplicateResourceException.class, () -> employeeController.createEmployee(request));
    }

    @Test
    void getAllEmployees_ReturnsAllEmployees() {

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1L, "John Doe", "1234567890", null));
        employees.add(new Employee(2L, "Jane Smith", "9876543210", null));

        when(employeeRepository.findAll()).thenReturn(employees);

        Iterable<Employee> result = employeeController.getAllEmployees();

        assertThat(result).isNotNull().hasSize(2).containsExactlyElementsOf(employees);
    }

    @Test
    void getEmployeeById_WithValidId_ReturnsEmployee() {

        long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("John Doe");

        when(employeeRepository.findOneById(id)).thenReturn(Optional.of(employee));

        Employee result = employeeController.getEmployeeById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("John Doe", result.getName());
    }

    @Test
    void getEmployeeById_WithInvalidId_ThrowsResourceNotFoundException() {

        long id = 1L;

        when(employeeRepository.findOneById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeController.getEmployeeById(id));
    }

    @Test
    void updateEmployee_WithValidId_ReturnsUpdatedEmployee() {

        long id = 1L;
        Employee existingEmployee = new Employee();
        existingEmployee.setId(id);
        existingEmployee.setName("John Doe");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("Jane Smith");

        when(employeeRepository.findOneById(id)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = employeeController.updateEmployee(id, updatedEmployee);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Jane Smith", result.getName());
    }

    @Test
    void deleteEmployee_WithValidId_DeletesEmployee() {

        long id = 1L;
        Employee existingEmployee = new Employee();
        existingEmployee.setId(id);
        existingEmployee.setName("John Doe");

        when(employeeRepository.findOneById(id)).thenReturn(Optional.of(existingEmployee));

        assertDoesNotThrow(() -> employeeController.deleteEmployee(id));

        verify(employeeRepository, times(1)).delete(existingEmployee);
    }

    @Test
    void deleteEmployee_WithInvalidId_ThrowsResourceNotFoundException() {

        long id = 1L;

        when(employeeRepository.findOneById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeController.deleteEmployee(id));
    }
}
