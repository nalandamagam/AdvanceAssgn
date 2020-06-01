package com.crm.office.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.crm.office.model.Employee;

@Entity
@Table(name = "departments")
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String deptName;
	private String area;
	
	@OneToMany(mappedBy = "department" , fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private Set<Employee> orderslist = new HashSet<>();
	
	@Basic(optional = false)
	@CreationTimestamp
	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt = new Date(); // initialize created date

	@UpdateTimestamp
	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt = new Date(); // initialize updated date
	
	public Set<Employee> getOrderslist() {
		return orderslist;
	}
	public void setOrderslist(Set<Employee> orderslist) {
		this.orderslist = orderslist;
	}
	public Department() {
		
	}
	public Department(String deptName, String area) {
		super();
		this.deptName = deptName;
		this.area = area;
	}
//	public long getId() {
//		return id;
//	}
//	public void setId(long id) {
//		this.id = id;
//	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	@Override
	public String toString() {
		return "Department [id=" + id + ", DeptName=" + deptName + ", Area=" + area + "]";
	}
	
	@PreUpdate
	public void setUpdatedAt() {  
	    this.updatedAt= new Date(); 
	}
	
}
