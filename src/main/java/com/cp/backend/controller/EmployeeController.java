package com.cp.backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cp.backend.entity.Employee;
import com.cp.backend.exception.DuplicateResourceException;
import com.cp.backend.exception.ResourceNotFoundException;
import com.cp.backend.repo.EmployeeRepo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRepo employeeRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody @Valid Employee request) {

        Optional<Employee> optionalEmployeeEntity = employeeRepository.findOneByName(request.getName());
        if (optionalEmployeeEntity.isPresent()) {
            throw new DuplicateResourceException();
        }

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setSupervisors(request.getSupervisors());
        employee.setPhoneNumber(request.getPhoneNumber());
        employeeRepository.save(employee);

        return employee;

    }

    @GetMapping
    public Iterable<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable("id") long id) {
        Optional<Employee> optionalEmployeeEntity = employeeRepository.findOneById(id);

        if (optionalEmployeeEntity.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return optionalEmployeeEntity.get();
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable("id") long id, @RequestBody Employee request) {

        Optional<Employee> optionalEmployeeEntity = employeeRepository.findOneById(id);

        if (optionalEmployeeEntity.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        Employee employee = optionalEmployeeEntity.get();

        employee.setName(request.getName());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setSupervisors(request.getSupervisors());
        employeeRepository.save(employee);

        return employee;

    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable("id") long id) {

        Optional<Employee> optionalEmployeeEntity = employeeRepository.findOneById(id);

        if (optionalEmployeeEntity.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        employeeRepository.delete(optionalEmployeeEntity.get());
    }

}
