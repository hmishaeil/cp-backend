package com.cp.backend.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cp.backend.entity.Employee;

@Repository
public interface EmployeeRepo extends CrudRepository<Employee, Long> {

    Optional<Employee> findOneByName(String name);

    Optional<Employee> findOneById(long id);

}
