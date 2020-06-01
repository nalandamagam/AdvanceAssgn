package com.crm.office.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crm.office.errorhandling.EntityNotFoundException;
import com.crm.office.model.Department;
import com.crm.office.repositories.DepartmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.validation.Valid;

@RestController
public class DepartmentController {
	@Autowired
	DepartmentRepository departmentRepository;
	
	private final ObjectMapper objectMapper;
	
	@Autowired
	public DepartmentController(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@GetMapping("/departments")
	public List<Department> getDepartments() throws EntityNotFoundException{
		return departmentRepository.findAll();
	}
	@RequestMapping(value = "/departments",method = RequestMethod.POST)
	public Department addDepartment(@Valid @RequestBody Department department) {
		return departmentRepository.save(department);
	}
	@PutMapping("/departments/{departmentId}")
	public Department editDepartment(@PathVariable(value="departmentId") long id, @Valid @RequestBody Department department )
	throws Exception{
		Optional<Department> oldDepartment = departmentRepository.findById(id);
		if(oldDepartment.isPresent()) {
			oldDepartment.get().setOrderslist(department.getOrderslist());
			oldDepartment.get().setDeptName(department.getDeptName());
			oldDepartment.get().setArea(department.getArea());
			
			return departmentRepository.save(oldDepartment.get());
			
		}else {
			throw new Exception("Invalid");
		}
	}
	
	@DeleteMapping("/departments/{departmentId}")
	public String  deleteDepartment(@PathVariable(value = "departmentId") long id) {
		departmentRepository.deleteById(id);
		return "Deleted";
	}
	
	@PatchMapping(path = "/departments/{id}", consumes = "application/json-patch+json")
	public ResponseEntity<Department> updateCustomer(@PathVariable Long id, @RequestBody JsonPatch patch) {
	    try {
	    	Optional<Department> department = departmentRepository.findById(id);
	    	JsonStructure target = objectMapper.convertValue(department.get(), JsonStructure.class);
	    	JsonValue patchedDepartment = patch.apply(target);
	    	Department updatedDepartment = objectMapper.convertValue(patchedDepartment, Department.class);
	        return ResponseEntity.ok(updatedDepartment);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
}
