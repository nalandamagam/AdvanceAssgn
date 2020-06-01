package com.crm.office.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crm.office.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {

}
