package com.crm.office.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crm.office.model.Department;
import com.crm.office.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

	public List<Employee> findByDepartment(Department department);

}
