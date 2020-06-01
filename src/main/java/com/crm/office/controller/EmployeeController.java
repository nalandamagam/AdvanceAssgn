package com.crm.office.controller;

import java.util.List;

import java.util.Optional;

import com.crm.office.errorhandling.EntityNotFoundException;
import javax.persistence.criteria.Order;
import javax.validation.Valid;
import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crm.office.model.Employee;
import com.crm.office.model.Employee;
import com.crm.office.model.Department;
import com.crm.office.model.Department;

import com.crm.office.repositories.DepartmentRepository;
import com.crm.office.repositories.EmployeeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class EmployeeController {
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	DepartmentRepository departmentRepository;

	private final ObjectMapper objectMapper;
	
	@Autowired
	public EmployeeController(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@GetMapping("/departments/{employeeId}/employees")
	public List<Employee> getAllEmployees(@PathVariable(value = "employeeId") Long id) throws EntityNotFoundException{
		Optional<Department> department = departmentRepository.findById(id);
		return employeeRepository.findByDepartment(department.get());
	}

	@PostMapping("/departments/{departmentId}/employees")
	public Employee addOrder(@PathVariable(value = "departmentId") Long id, @Valid @RequestBody Employee employee)
			throws Exception {
		Optional<Department> department = departmentRepository.findById(id);
		if (department.isPresent()) {
			employee.setDepartment(department.get());
			return employeeRepository.save(employee);
		} else {
			throw new Exception("Invalid");
		}
	}

	@PutMapping("/departments/{departmentId}/employees/{employeeId}")
	public Employee editEmployee(@PathVariable(value = "departmentId") Long departmentId,
			@PathVariable(value = "employeeId") Long employeeId, @Valid @RequestBody Employee employee) throws Exception {
		Optional<Employee> oldEmployee = employeeRepository.findById(employeeId);
		Optional<Department> department = departmentRepository.findById(departmentId);
		if (department.isPresent()) {
			if (oldEmployee.isPresent()) {
				oldEmployee.get().setFirstName(employee.getFirstName());
				oldEmployee.get().setLastName(employee.getLastName());
				oldEmployee.get().setEmailId(employee.getEmailId());
				oldEmployee.get().setSalary(employee.getSalary());
				return employeeRepository.save(oldEmployee.get());
			}else {
				throw new Exception("Invalid");
			}
		}else {
			throw new Exception("Invalid");
		}
	}
	
	@DeleteMapping("/departments/{departmentId}/employees/{employeeId}")
	public String deleteEmployee(@PathVariable(value = "departmentId") Long departmentId,
			@PathVariable(value = "employeeId") Long employeeId) {
		Optional<Department> department = departmentRepository.findById(departmentId);
		Optional<Employee> employee = employeeRepository.findById(employeeId);
		if(department.isPresent()) {
			if(employee.isPresent()) {
				employeeRepository.deleteById(employeeId);
				return "deleted";
			}else {
				return "not deleted";
			}
		}else {
			return "not deleted";
		}
	}
	
	@PatchMapping(path = "/employees/{id}", consumes = "application/json-patch+json")
	public ResponseEntity<Employee> updateCustomer(@PathVariable Long id, @RequestBody JsonPatch patch) {
	    	Employee employee = employeeRepository.findById(id).get();
	    	JsonStructure target = objectMapper.convertValue(employee, JsonStructure.class);
	    	JsonValue patchedEmployee = patch.apply(target);
	    	Employee updatedEmployee = objectMapper.convertValue(patchedEmployee, Employee.class);
	        return ResponseEntity.ok(updatedEmployee);
	}

}
